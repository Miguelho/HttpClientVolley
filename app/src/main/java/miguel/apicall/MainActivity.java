package miguel.apicall;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
        url = "http://192.168.0.119:3000/authors";
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
    public void hacerPost(View view) {
        //Body params
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("first_name", first_name.getText().toString());
            jsonBody.put("biography", biography.getText().toString());
            jsonBody.put("DoB", DoB.getText().toString());
            jsonBody.put("nationality", nationality.getText().toString());
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