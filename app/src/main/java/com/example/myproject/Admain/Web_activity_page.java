package com.example.myproject.Admain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myproject.R;

import java.util.Objects;

public class Web_activity_page extends AppCompatActivity {
    WebView webView;
String url;
String tool_title;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);
        ///////////bund tha url and toolTitle//////////////////////////
        Bundle bundle=getIntent().getExtras();
        url=bundle.getString("url1");
        tool_title=bundle.getString("Tool_title");
        ///////////////////////////////////////////////////////////////
        ///////////////////////////TOOL BAR FUNCTION///////////////
        Toolbar toolbar = findViewById(R.id.hockey_webpage_tool);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(tool_title);
        ///////////////////////////////////////////////////////////////
        /////////////////////SWIN FUNCTION/////////////////////////////
        SwipeRefreshLayout swipe=findViewById(R.id.reload_ewb);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                    }
                }, 500);

                Toast.makeText(Web_activity_page.this, "Refreshing...", Toast.LENGTH_SHORT).show();
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        TextView toptitle=findViewById(R.id.topmessage);
        TextView suptitle=findViewById(R.id.submassage);
        ImageView wify=findViewById(R.id.nointernet);
        TextView tryagin=findViewById(R.id.tryagin);
        ProgressBar progressBar=findViewById(R.id.progrss_web);
        webView=findViewById(R.id.hockey_wbpages);
        if(isConnect(Web_activity_page.this)){
            toptitle.setVisibility(View.VISIBLE);
            suptitle.setVisibility(View.VISIBLE);
            wify.setVisibility(View.VISIBLE);

            tryagin.setVisibility(View.VISIBLE);
            webView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            tryagin.setOnClickListener(view ->{
                if(isConnect(Web_activity_page.this)){
                    toptitle.setVisibility(View.VISIBLE);
                    suptitle.setVisibility(View.VISIBLE);
                    wify.setVisibility(View.VISIBLE);

                    tryagin.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.INVISIBLE);
                }else{
                    Intent intent=new Intent(this,Admain_Mainpage.class);
                    startActivity(intent);
                }
            });


        }else {
            toptitle.setVisibility(View.INVISIBLE);
            suptitle.setVisibility(View.INVISIBLE);
            wify.setVisibility(View.INVISIBLE);
            tryagin.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.VISIBLE);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                    setTitle("loading...");

                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                }
            });

            webView.loadUrl(url);
            WebSettings webSettings=webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }

    }
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }

    }
    private boolean isConnect(Web_activity_page mainActivity){
        ConnectivityManager connectivityManager=(ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonnect=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilconnect=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wificonnect == null || !wificonnect.isConnected()) && (mobilconnect == null || !mobilconnect.isConnected());


    }
}