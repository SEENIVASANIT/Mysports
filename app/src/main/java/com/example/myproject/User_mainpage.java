package com.example.myproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myproject.Admain.Admain_Mainpage;
import com.example.myproject.Admain.Upload_matchs_admain;
import com.example.myproject.Admain.Web_activity_page;
import com.example.myproject.Players.Show_biodata;

public class User_mainpage extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    Toolbar toolbar;
    FirebaseMessagingService firebaseMessagingService=new FirebaseMessagingService();
    Intent exit_app=new Intent(Intent.ACTION_MAIN);
    SharedPreferences sharedPreferences;
    TextView upload_match,Upload_match1,players_data,Players_data1,sports_equipment,Sports_equipment1,notification_cout;
    public int previous_count_data,received_message_count,current_data_count;
    public  String str,check_user=null;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mainpage);
        toolbar = findViewById(R.id.hockey_tollbar);
        setSupportActionBar(toolbar);
        //Intent intent1=new Intent(this, Admain_Mainpage.class);

        ////////////check user////////////////////////////////////
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            check_user=bundle.getString("User");
        }
        /////////////////////SWIN FUNCTION/////////////////////////////
        SwipeRefreshLayout swipe=findViewById(R.id.admain_page_swip);
        swipe.setOnRefreshListener(() -> {
            //startActivity(intent1);
            Bundle bundle1 =new Bundle();
            bundle1.putString("User","user");
            Intent intent=new Intent(User_mainpage.this, User_mainpage.class);
            intent.putExtras(bundle1);
            startActivity(intent);
            new Handler().postDelayed(() -> swipe.setRefreshing(false), 500);

            Toast.makeText(User_mainpage.this, "Refreshing...", Toast.LENGTH_SHORT).show();
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////textview/////////////////////////////////////////////////////////////////
        sharedPreferences=getSharedPreferences("count",MODE_PRIVATE);
        notification_cout=findViewById(R.id.notification_count);

        received_message_count=firebaseMessagingService.getCount();

        if(received_message_count!=0){
            previous_count_data=sharedPreferences.getInt("previous_count",0);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt("current_count",received_message_count+previous_count_data);
            editor.apply();
            putcount();
        }else{
            current_data_count=sharedPreferences.getInt("current_count",0);

            str= String.valueOf(current_data_count);
            if(current_data_count==0){
                notification_cout.setVisibility(View.INVISIBLE);
            }else{
                notification_cout.setVisibility(View.VISIBLE);
                notification_cout.setText(str);
            }

        }

        upload_match=findViewById(R.id.go_match_page);
        Upload_match1=findViewById(R.id.go_match_page1);
        players_data=findViewById(R.id.go_playersdata_page);
        Players_data1=findViewById(R.id.players1);
        sports_equipment=findViewById(R.id.go_sports_euipment_page);
        Sports_equipment1=findViewById(R.id.go_sports_euipment_page1);



////////////////////////////////////Onclick moethod////////////////////////////////////////////////////////////////////////////
        upload_match.setOnClickListener(view -> {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt("previous_count",0);
            editor.apply();
            SharedPreferences.Editor editor1=sharedPreferences.edit();
            editor.putInt("current_count",0);
            editor.apply();
            Bundle bundle12 =new Bundle();
            bundle12.putString("User",check_user);
            Intent intent=new Intent(User_mainpage.this, Upload_matchs_admain.class);
            intent.putExtras(bundle12);
            startActivity(intent);
        });
        Upload_match1.setOnClickListener(view -> {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt("previous_count",0);
            editor.apply();
            SharedPreferences.Editor editor1=sharedPreferences.edit();
            editor.putInt("current_count",0);
            editor.apply();
            Intent intent=new Intent(User_mainpage.this,Upload_matchs_admain.class);
            startActivity(intent);


        });
        players_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(User_mainpage.this, Show_biodata.class));
            }
        });
        Players_data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_mainpage.this, Show_biodata.class));

            }
        });
        sports_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(User_mainpage.this, "Coming Soon!!!", Toast.LENGTH_SHORT).show();

            }
        });
        Sports_equipment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(User_mainpage.this, "Coming Soon!!!", Toast.LENGTH_SHORT).show();

            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        webView= findViewById(R.id.today_news);
        TextView toptitle=findViewById(R.id.topmessage);
        TextView suptitle=findViewById(R.id.submassage);
        ImageView wify=findViewById(R.id.nointernet);
        TextView tryagin=findViewById(R.id.tryagin);
        progressBar=findViewById(R.id.progrss_web);

        if(isConnect(this)){
            toptitle.setVisibility(View.VISIBLE);
            suptitle.setVisibility(View.VISIBLE);
            wify.setVisibility(View.VISIBLE);

            tryagin.setVisibility(View.VISIBLE);
            webView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            tryagin.setOnClickListener(view -> {
                if(isConnect(User_mainpage.this)){
                    toptitle.setVisibility(View.VISIBLE);
                    suptitle.setVisibility(View.VISIBLE);
                    wify.setVisibility(View.VISIBLE);

                    tryagin.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.INVISIBLE);
                }else{
                    startActivity(new Intent(getApplicationContext(), Admain_Mainpage.class));
                }
            });


        }else{
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

            webView.loadUrl("https://www.hockeyindia.org/");
            WebSettings webSettings=webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }


    }


    int back_count=0;
    @Override
    public void onBackPressed() {
        back_count++;
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            if(back_count>=2){

                exit_app.addCategory(Intent.CATEGORY_HOME);
                exit_app.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(exit_app);
            }else {
                Toast.makeText(this, "Pross agin to exit!!!", Toast.LENGTH_SHORT).show();
            }


        }

    }
    ///////////////CHECK NET WORK/////////////////////////////////
    private boolean isConnect(User_mainpage mainActivity){
        ConnectivityManager connectivityManager=(ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonnect=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilconnect=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wificonnect == null || !wificonnect.isConnected()) && (mobilconnect == null || !mobilconnect.isConnected());


    }
    ////////////////////////////////////////////////////////menu item///////////////////////////////////////////////////////////
//    news:https://www.hockeyindia.org/
//    about:https://en.m.wikipedia.org/wiki/Field_hockey#
//    hockeyvideo:https://www.youtube.com/c/fihockey/featured
//    photos:https://www.hockeyindia.org/media/photos
//    workout:https://www.ahockeyworld.net/category/how-to/
//    hockeymark:https://www.harrodsport.com/advice-and-guides/hockey-goal-field-dimensions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_admain_munu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Bundle bundle=new Bundle();
        if (id == R.id.about_hockey) {

            bundle.putString("url1","https://en.m.wikipedia.org/wiki/Field_hockey#");
            bundle.putString("Tool_title","About hockey game!");
            Intent intent=new Intent(this, Web_activity_page.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }else if(id==R.id.video_hockey){
            bundle.putString("url1","https://www.youtube.com/c/fihockey/featured");
            bundle.putString("Tool_title","HOCKEY VIDEOS...");
            Intent intent=new Intent(this, Web_activity_page.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(id==R.id.photo_hockey){
            bundle.putString("url1","https://www.hockeyindia.org/media/photos");
            bundle.putString("Tool_title","HOCKEY PHOTOS...");
            Intent intent=new Intent(this, Web_activity_page.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }else if(id==R.id.workout_hockey){
            bundle.putString("url1","https://www.ahockeyworld.net/category/how-to/");
            bundle.putString("Tool_title","HOCKEY WORKOUTS...");
            Intent intent=new Intent(this, Web_activity_page.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }else if(id==R.id.ground_marking){
            bundle.putString("url1","https://www.harrodsport.com/advice-and-guides/hockey-goal-field-dimensions");
            bundle.putString("Tool_title","HOCKEY GROUND MARKING...");
            Intent intent=new Intent(this, Web_activity_page.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }else if(id==R.id.exit_hockey) {
            startActivity(exit_app);

            Toast.makeText(this, "Exit app", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.sthc_hockey_club){
            bundle.putString("url1","https://www.facebook.com/SivakasiTownHockeyClub/");
            bundle.putString("Tool_title","Sivakasi Town Hockey Club...");
            Intent intent=new Intent(this, Web_activity_page.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    protected void putcount() {


        current_data_count=sharedPreferences.getInt("current_count",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("previous_count",current_data_count);
        editor.apply();
        firebaseMessagingService.putcount();
        received_message_count=0;
        str= String.valueOf(current_data_count);
        if(current_data_count==0){
            notification_cout.setVisibility(View.INVISIBLE);
        }else{
            notification_cout.setVisibility(View.VISIBLE);
            notification_cout.setText(str);
        }
    }
}