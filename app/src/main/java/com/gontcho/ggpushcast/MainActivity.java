package com.gontcho.ggpushcast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final GGPushCast pushCast = new GGPushCast();
        pushCast.checkSubscriptionForNotification(this,
                "cGSBv6XFLKi:APA91bH6mSHrEdB3B_r5_DXKzLwsGXxf2MrVpi33KWVflx27hNmDO_dNK0In9Eq8fBh9BTN07Ogbrmc5s0vfl__jOAJQeyFQm6W6Ru7fMKihLTi72Uh15VyieQnnykTFqVIEDdh5qZEl",
                "Realme",
                "RMX1821",
                "English (United States)",
                "United States",
                "29",
                "Q",
                "27",
                "Realme",
                "216528704956");





    }




}
