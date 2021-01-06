package com.gontcho.ggpushcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GGPushCast {

    private String insertUrl = "https://ggpushcast.com/androidsubscribe";
    private String check;
    private String brand, model,  language, country,  versionCode,  versionName,  sdk,  manufacturer;

    public void checkSubscriptionForNotification(final Context context, final String deviceToken, final String senderID) {
        Log.e("Start = > ","start");
        getPhoneDetails();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("registration_token", deviceToken);
            jsonBody.put("brand", brand);
            jsonBody.put("model", model);
            jsonBody.put("language_name", language);
            jsonBody.put("country_name", country);
            jsonBody.put("version_code", versionCode);
            jsonBody.put("version_name", versionName);
            jsonBody.put("sdk", sdk);
            jsonBody.put("manufacturer", manufacturer);
            jsonBody.put("sender_id", senderID);
            jsonBody.put("action", "check");

            JsonObjectRequest request_json = new JsonObjectRequest(insertUrl, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                check = response.getString("success");
                                Log.e("Response = > ","start");
                                Toast.makeText(context, check, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("Error = > ","start "+e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    int code = error.networkResponse.statusCode;
                    if(code == 404){
                        subscriptionForNotification(context,deviceToken,brand,model,language,country,versionCode,versionName,sdk,manufacturer,senderID);
                    }
                    check = code+"";
                    Log.e("Error = > ","start");
                    Toast.makeText(context, check, Toast.LENGTH_SHORT).show();

                }

            });
            requestQueue.add(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    // Get phone information
    public void getPhoneDetails(){

        brand = Build.BRAND;
        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        Field[] fields = Build.VERSION_CODES.class.getFields();
        versionName = "UNKNOWN";
        for (Field field : fields) {
            try {
                if (field.getInt(Build.VERSION_CODES.class) == Build.VERSION.SDK_INT) {
                    versionName = field.getName();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        versionCode = Build.VERSION.RELEASE+"";
        sdk = Build.VERSION.SDK_INT+"";
        language =java.util.Locale.getDefault().getDisplayName();
        country = Locale.getDefault().getDisplayCountry();

      }

    public void subscriptionForNotification(final Context context, String deviceToken, String brand, String model, String language, String country, String versionCode, String versionName, String sdk, String manufacturer, String senderID){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("registration_token", deviceToken);
            jsonBody.put("brand", brand);
            jsonBody.put("model", model);
            jsonBody.put("language_name", language);
            jsonBody.put("country_name", country);
            jsonBody.put("version_code", versionCode);
            jsonBody.put("version_name", versionName);
            jsonBody.put("sdk", sdk);
            jsonBody.put("manufacturer", manufacturer);
            jsonBody.put("sender_id", senderID);
            jsonBody.put("action","");

            JsonObjectRequest request_json = new JsonObjectRequest(insertUrl,jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                check = response.getString("success");
                                Toast.makeText(context, check, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    int code = error.networkResponse.statusCode;
                    if(code == 409){
                        Toast.makeText(context, "This Device already register", Toast.LENGTH_SHORT).show();
                    }

                }

            });
            requestQueue.add(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}


