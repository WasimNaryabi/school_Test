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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GGPushCast {

    private String insertUrl = "https://test.ggpushcast.com/androidsubscribe";
    private String check;
    public String checkSubscriptionForNotification(Context context){
        sendDeviceDetails(context);
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



}
