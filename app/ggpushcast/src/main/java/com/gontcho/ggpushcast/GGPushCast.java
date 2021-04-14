package com.gontcho.ggpushcast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.util.Locale;


public class GGPushCast {
    private RequestQueue requestQueue;
    private String insertUrl = "https://ggpushcast.com/androidsubscribe";
    private String responseURl;
    private String check;
    private String brand, model,  language, country,  versionCode,  versionName,  sdk,  manufacturer;
    public void sendNotification(RemoteMessage remoteMessage, Context context,  Class<? extends Activity> ActivityToOpen, int urlImage){
        requestQueue  = Volley.newRequestQueue(context);
        Log.e("Message :", remoteMessage.getData().get("title"));

        responseURl ="https://ggpushcast.com/webpush_confirm_read/"+remoteMessage.getData().get("delivery_id")+"/"+remoteMessage.getData().get("message_key");
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, ActivityToOpen);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";
        sendBackResponse(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(urlImage)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(remoteMessage.getData().get("body"))
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);


        notificationManager.notify(1, notificationBuilder.build());


    }
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

    public void sendBackResponse(final Context context){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, responseURl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}


