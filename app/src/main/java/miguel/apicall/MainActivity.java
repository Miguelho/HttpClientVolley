package miguel.apicall;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import miguel.apicall.Utils.JSONhandler;
import miguel.apicall.Utils.URLhandler;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView responseView;
    EditText emailText, first_name, biography, nationality, DoB;
    String email, message;
    public static String API_KEY = "e35ca63a3a483d65";
    public static String API_URL = "https://api.fullcontact.com/v2/person.json?";
    RetrieveFeedTask retrieveFeedTask;
    String url;

    //New Era
    RequestQueue mRequestQueue;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        responseView = (TextView) findViewById(R.id.responseView);
        emailText = (EditText) findViewById(R.id.emailText);
        first_name = (EditText) findViewById(R.id.first_name);
        biography = (EditText) findViewById(R.id.biography);
        nationality = (EditText) findViewById(R.id.nationality);
        DoB = (EditText) findViewById(R.id.DoB);
        url = this.getResources().getString(R.string.API_POSICIONES_URL);
        mRequestQueue = Volley.newRequestQueue(this);
        //Instanciación clase AsyncTask
        retrieveFeedTask = new RetrieveFeedTask();

        /*Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);*/


    }

    @Override
    protected void onStop() {
        super.onStop();
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return false;
            }
        });
    }

    public void buscarInfo(View view) {
        //Recoge el email
        email = emailText.getText().toString();
        //retrieveFeedTask.onPreExecute();
        retrieveFeedTask.execute(); // Lanza los 4 métodos de la clase AsyncTask en orden

    }

    //Plain Old School HttpClient
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private Exception exception;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        @Override
        protected String doInBackground(Void... urls) {
            try {
                //Formación dinámica en función del email de la URL de la API
                //URL url = new URL( API_URL + "email=" +email+ "&apiKey=" + API_KEY);
                URL url = new URL("http://192.168.0.119:3000/authors");
                //Abro conexión a la URL
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));//Llamada GET
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null)
                response = "ha habido algún problema";

            progressBar.setVisibility(View.INVISIBLE);
            /*try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                String requestID = object.getString("requestId");
                int likelihood = object.getInt("likelihood");
                JSONArray photos = object.getJSONArray("photos");
            } catch (JSONException e) {
                // Appropriate error handling code
            }*/

            responseView.setText(response);

        }
    }

    class PostAsyncTask extends AsyncTask<Void, Void, String> {
        private Exception exception;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        @Override
        protected String doInBackground(Void... urls) {
            try {
                //Formación dinámica en función del email de la URL de la API
                URL url = new URL(API_URL + "email=" + "&apiKey=" + API_KEY);

                //Abro conexión a la URL
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));//Llamada GET
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null)
                response = "ha habido algún problema";

            progressBar.setVisibility(View.INVISIBLE);
            responseView.setText(response);
        }
    }

    //New Era of Volley
    public void hacerGet(View view) {
        if (isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);

            stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.length() == 0)
                        response = "ha habido algún problema";
                    responseView.setText(response);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    responseView.setText(error.getMessage());
                }
            });
            stringRequest.setTag("GET"); //Permite que continue la petición GET a pesar de cambiar de actividad
            mRequestQueue.add(stringRequest);
        }else {
            Toast.makeText(this, "Error de conexi�n", Toast.LENGTH_LONG).show();
        }
    }
    //New Era

    /*
    * Estructura Objeto JSON Posicion en colección mongoDB
    *{
    "geometry": {
        "type" : "Point",
        "coordinates" : [
            "-3.6729744.18",
            "40.4766598"
        ]
    },
    "ts" : mydate2,
    "acc" : 7,
    "usuario_id" : {
        "$ref" : "usuarios",
        "$id" : ObjectId("5732d55d925edb981031e5a4"),
        "$db" : "geodata"
    },
    "restaurante_id" : {
        "$ref" : "restaurantes",
        "$id" : ObjectId("5730825ffebe630e1d5af8eb"),
        "$db" : "geodata"
    }
});
    * */
    //Se podría hacer dejando default muchos cmapos para evitar más tráfico del necesario
    public void postPosition(View view) {
        //Body params
        JSONObject jsonBody = new JSONObject();
        JSONObject geometry = new JSONObject();
        JSONObject usuario_id = new JSONObject();
        JSONObject restaurante_id = new JSONObject();
        String[] coordenadas = {"-3.6729744.18","40.4766598"}; // Coordenadas LocProvider
        String coords=  "-3.6729744.18,40.4766598";
        try {
            //Setear objeto geometry
            geometry.put("type", "Point");
            //geometry.put("coordinates", coordenadas);
            geometry.put("coordinates", coords);



            //Setear objeto usuario_id
            usuario_id.put("$ref","usuarios");
            usuario_id.put("id","5732d55d925edb981031e5a4" );//ID del documento del usuario en Mongodb
            usuario_id.put("$db","geodata" );//ID del documento del usuario en Mongodb

            //Setear objeto usuario_id
            restaurante_id.put("$ref","restaurantes");
            restaurante_id.put("id","5732e5ebfebe630e1d5af8ee" );//ID del documento del restaurante en Mongodb
            restaurante_id.put("$db","geodata" );//ID del documento del usuario en Mongodb

            jsonBody.put("geometry", geometry);
            jsonBody.putOpt("ts",null); // El server marca el timestamp
            jsonBody.put("acc",7); // Hardcoded, el terminal determina la precisión.
            jsonBody.put("usuario_id",usuario_id);
            jsonBody.put("restaurante_id",restaurante_id);

            Log.v("Json",jsonBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonBody.toString();

        if (isNetworkAvailable()) {
            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                /*try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String id = jsonResponse.getString("_id"),
                            network = jsonResponse.getString("nombre");
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                    String response1 = " haha " + response;
                    responseView.setText(response1);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
        /*{//getParams() cuando queramos pasar parametros por la URL
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                //Parametros post
                params.put("nombre","Rogelio");
                params.put("biografia","Madrid");
                params.put("fecha_de_nacimiento","1983");
                params.put("nacionalidad","español");
                return params;

            }
        };*/
            stringRequest.setTag("POST"); //Permite que continue la petición POST a pesar de cambiar de actividad
            mRequestQueue.add(stringRequest);
        }else {
                Toast.makeText(this, "Error de conexi�n", Toast.LENGTH_LONG).show();
        }
    }


    /*
    * Estructura Objeto JSON Visita en colección mongoDB
    *{
    {
    "_id" : ObjectId("573ad4b1438ab2f8078ee886"),
    "usuario_id" : ObjectId("573071f8febe630e1d5af8e6"),
    "restaurante_id" : ObjectId("573076a5febe630e1d5af8e7"),
    "horaSalida" : null,
    "horaEntrada" : ISODate("2016-05-17T08:22:09.057Z"),
    "opinion" : {
        "fecha" : ISODate("2016-05-17T08:22:09.058Z")
    },
    "__v" : 0
}
});
    * */

    public void postVisit(View view) {
        //Body params
        JSONObject jsonBody = new JSONObject();
        JSONObject opinion = new JSONObject();
        try {
            //Setear objeto opinion, en la coleccion tiene Titulo,fecha,comentario,valoracion
            opinion.put("comentario", "Me gustó mucho este restaurante");

            jsonBody.put("usuario_id","5732d55d925edb981031e5a4");
            jsonBody.put("restaurante_id","5732e5ebfebe630e1d5af8ee");
            jsonBody.put("horaEntrada", "null");// El server marca el timestamp
            //jsonBody.put("horaSalida"," ");
            jsonBody.put("opinion",opinion);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonBody.toString();

        if (isNetworkAvailable()) {
            stringRequest = new StringRequest(Request.Method.POST, this.getResources().getString(R.string.API_VISIT_URL), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    String response1 = " haha " + response;
                    responseView.setText(response1);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            stringRequest.setTag("POST"); //Permite que continue la petición POST a pesar de cambiar de actividad
            mRequestQueue.add(stringRequest);
        }else {
            Toast.makeText(this, "Error de conexi�n", Toast.LENGTH_LONG).show();
        }
    }
    public void putVisit(View view) {
        //Body params
        JSONObject jsonBody = new JSONObject();
        JSONObject opinion = new JSONObject();

        try {
            //Setear objeto opinion, en la coleccion tiene Titulo,fecha,comentario,valoracion
            opinion.put("comentario", "Me gustó mucho este restaurante");

            jsonBody.put("usuario_id","5732d55d925edb981031e5a4");
            jsonBody.put("restaurante_id","5732e5ebfebe630e1d5af8ee");
            jsonBody.put("horaEntrada", "null");// El server marca el timestamp
            jsonBody.put("horaSalida"," ");
            jsonBody.put("opinion",opinion);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonBody.toString();

        if (isNetworkAvailable()) {
            stringRequest = new StringRequest(Request.Method.PUT, this.getResources().getString(R.string.API_VISIT_URL), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    String response1 = " haha " + response;
                    responseView.setText(response1);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            stringRequest.setTag("PUT"); //Permite que continue la petición PUT a pesar de cambiar de actividad
            mRequestQueue.add(stringRequest);
        }else {
            Toast.makeText(this, "Error de conexi�n", Toast.LENGTH_LONG).show();
        }
    }

    public void getTime(View view){
        url= this.getResources().getString(R.string.API_VISIT_URL)+"/operations/getTime";
        String param1= "_id";
        String objectId= "573b2e3b36dc3508088d8929";//ID de la visita de la que se desea conocer el dato
        //String uri = String.format(url+"?"+param1+"=%1$s&param2=%2$s", Construccion URL con dos parametros ( param1="xxx"&param2="yyy")
        /*String uri = String.format(url+"?"+param1+"=%1$s", objectId);*/
        String[] params= {param1};
        String[] values= {objectId};
        url= URLhandler.generateURL(url,params,values);

        if (isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);

            stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.length() == 0)
                        response = "ha habido algún problema";
                    try {
                        JSONObject objeto = new JSONObject(response).getJSONArray("elapsedTime").getJSONObject(0);
                        responseView.setText(JSONhandler.getHoursMinutesSecondsFromDateDifference(objeto.getLong("dateDifference")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    responseView.setText(error.getMessage());
                }
            }){

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        try {
                            responseString = new String (response.data, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        };

                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };
            stringRequest.setTag("GET"); //Permite que continue la petición GET a pesar de cambiar de actividad
            mRequestQueue.add(stringRequest);
        }else {
            Toast.makeText(this, "Error de conexi�n", Toast.LENGTH_LONG).show();
        }
    }
    private boolean isNetworkAvailable() {
        //Gestor de conectividad
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //Objeto que recupera la informaci�n de la red
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
//        Si la informaci�n de red no es nula y estamos conectados
//        la red est� disponible
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;


    }

}