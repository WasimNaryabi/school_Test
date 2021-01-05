package com.gontcho.ggpushcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GGPushCast {

    private String insertUrl = "https://test.ggpushcast.com/androidsubscribe";
    private String check;
    public String checkSubscriptionForNotification(Context context){
        checkDevice(context);
        return check;
    }

    public String subscriptionForNotification(Context context){
        registerDevice(context);
        return check;
    }

    private void sendDeviceDetails(final Context context){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject object = new JSONObject(response);
                            check = object.getString("success");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            check="Error";

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        check="Error";
                    }
                }){

            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("registration_token","");
                params.put("brand","");
                params.put("model","");
                params.put("language_name","");
                params.put("country_name","");
                params.put("version_code","");
                params.put("version_name","");
                params.put("sdk","");
                params.put("manufacturer","");
                params.put("sender_id","");
                params.put("action","");

                return  params;
            }

        };

        Handler.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void checkDevice(final Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("registration_token","cGSBv6XFLKi:APA91bH6mSHaEdB3B_r5_DXKzLwsGXxf2MrVpi33KWVflx27hNmDO_dNK0In9Eq8fBh9BTN07Ogbrmc5s0vfl__jOAJQeyFQm6W6Ru7fMKihLTi72Uh15VyieQnnykTFqVIEDdh5qZEl");
            jsonBody.put("brand","Realme");
            jsonBody.put("model","RMX1821");
            jsonBody.put("language_name","English (United States)");
            jsonBody.put("country_name","United States");
            jsonBody.put("version_code","29");
            jsonBody.put("version_name","Q");
            jsonBody.put("sdk","27");
            jsonBody.put("manufacturer","Realme");
            jsonBody.put("sender_id","216528704956");
            jsonBody.put("action","check");

            JsonObjectRequest request_json = new JsonObjectRequest(insertUrl,jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                check = response.getString("success");

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    int code = error.networkResponse.statusCode;
                }

            });
            requestQueue.add(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void registerDevice(final Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("registration_token","cGSBv6XFLKi:APA91bH6mSHaEdB3B_r5_DXKzLwsGXxf2MrVpi33KWVflx27hNmDO_dNK0In9Eq8fBh9BTN07Ogbrmc5s0vfl__jOAJQeyFQm6W6Ru7fMKihLTi72Uh15VyieQnnykTFqVIEDdh5qZEl");
            jsonBody.put("brand","Realme");
            jsonBody.put("model","RMX1821");
            jsonBody.put("language_name","English (United States)");
            jsonBody.put("country_name","United States");
            jsonBody.put("version_code","29");
            jsonBody.put("version_name","Q");
            jsonBody.put("sdk","27");
            jsonBody.put("manufacturer","Realme");
            jsonBody.put("sender_id","216528704956");
            jsonBody.put("action","check");

            JsonObjectRequest request_json = new JsonObjectRequest(insertUrl,jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                check = response.getString("success");

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    int code = error.networkResponse.statusCode;
                }

            });
            requestQueue.add(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
