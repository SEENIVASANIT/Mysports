package com.example.myproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if(!isConnect(this)){
            showInternetDialog();
        }else{
            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    Intent start_app = new Intent(MainActivity.this, Common_login.class);
                    startActivity(start_app);
                    finish();
                }
            }, 2500);

        }

    }
    private void showInternetDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view= LayoutInflater.from(this).inflate(R.layout.no_internet_dialog,findViewById(R.id.nointernetlayout));
        view.findViewById(R.id.tryagin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnect(MainActivity.this)){
                    showInternetDialog();
                }else{
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        });
        builder.setView(view);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private boolean isConnect(MainActivity mainActivity){
        ConnectivityManager connectivityManager=(ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonnect=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilconnect=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wificonnect !=null && wificonnect.isConnected()) || (mobilconnect !=null && mobilconnect.isConnected());


    }
}