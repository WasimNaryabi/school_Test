package com.gontcho.ggpushcast;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;


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
