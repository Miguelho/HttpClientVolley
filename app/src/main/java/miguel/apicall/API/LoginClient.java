package miguel.apicall.API;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
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
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;

import miguel.apicall.R;

/**
 * Created by Miguel on 5/9/2016.
 */
public class LoginClient {
    public String API_URL;
    Context mContext;
    private String first_name,password, messageFromServer;

    //New Era
    RequestQueue mRequestQueue;
    StringRequest stringRequest;

    public String getMessageFromServer(){return messageFromServer;}
    public void setMessageFromServer(String MessageFromServer) { this.messageFromServer=MessageFromServer;
    }
    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public LoginClient(Context context){
        mContext=context;
        API_URL = mContext.getApplicationContext().getResources().getString(R.string.API_URL);
        mRequestQueue= Volley.newRequestQueue(mContext);
    }


    public void login() {
        //Body params
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", first_name);
            jsonBody.put("password", password);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = jsonBody.toString();

            stringRequest = new StringRequest(
                    Request.Method.POST,
                    API_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        Log.v("Respuesta Server", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    })
            {
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
                        //responseString = String.valueOf(response.statusCode);
                        responseString = String.valueOf(response.headers.get("Content-Length"));
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            stringRequest.setTag("POST"); //Permite que continue la petición POST a pesar de cambiar de actividad
            mRequestQueue.add(stringRequest);
        }
    }




