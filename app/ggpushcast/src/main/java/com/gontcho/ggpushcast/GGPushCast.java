package com.gontcho.ggpushcast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
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
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GGPushCast {

    Context context;
    Class<? extends Activity> ActivityToOpen;
    int urlImage;

    private String insertUrl = "https://ggpushcast.com/androidsubscribe";
    private String check;
    private String brand, model,  language, country,  versionCode,  versionName,  sdk,  manufacturer;

    com.squareup.picasso.Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Toast.makeText(context, "Image loaded", Toast.LENGTH_SHORT).show();
            sendNotification(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private void sendNotification(Bitmap bitmap){


        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, ActivityToOpen);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(urlImage)
                .setContentTitle(Config.title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(Config.content)
                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setLargeIcon(bitmap)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);


        notificationManager.notify(1, notificationBuilder.build());


    }


    public void getImage(final RemoteMessage remoteMessage, Context context,  Class<? extends Activity> ActivityToOpen, int urlImage) {

        Map<String, String> data = remoteMessage.getData();
        Config.title = data.get("title");
        Config.content = data.get("content");
        Config.imageUrl = data.get("imageUrl");
        Config.gameUrl = data.get("gameUrl");

        context=this.context;
        ActivityToOpen =this.ActivityToOpen;
        urlImage = this.urlImage;

        //Create thread to fetch image from notification
        if(remoteMessage.getData()!=null){
            Toast.makeText(context, "Notification", Toast.LENGTH_SHORT).show();
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Get image from data Notification
                    Picasso.get()
                            .load(Config.imageUrl)
                            .into(target);

                }
            }) ;
        }
    }

    public void receiveNotification(Context context, final Class<? extends Activity> ActivityToOpen, String notificationTitle, String notificationMessage, int iconUrl){
        Intent intent = new Intent(context,ActivityToOpen);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,
                intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "123456";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(iconUrl)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationMessage)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
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


}


