package com.example.myproject.Admain;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Show_tournament_images extends AppCompatActivity {
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    Image_Adapter adapter;
    List<Image_model> list;
    private FirebaseFirestore db;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("TOURNAMENT_IMAGES");
    SwipeRefreshLayout swipe;
    String match_title,match_id,check_user="novalue";
    Toolbar toolbar;
    int sub_count;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    Bundle bundle=new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tournament_images);
        toolbar = findViewById(R.id.image_view_tool);
        setSupportActionBar(toolbar);

      //  Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
///////////////////////////////////////////////prograssBar///////////////////////////////////////////////////////////
        progressBar = findViewById(R.id.progrssbar_show_image);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        TextView toptitle = findViewById(R.id.topmessage);
        TextView suptitle = findViewById(R.id.submassage);
        ImageView imageView = findViewById(R.id.nointernet);
        TextView tryagin = findViewById(R.id.tryagin);
        if (isConnect(Show_tournament_images.this)) {
            toptitle.setVisibility(View.VISIBLE);
            suptitle.setVisibility(View.VISIBLE);
            tryagin.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            tryagin.setOnClickListener(view -> {
                if (isConnect(this)) {
                    toptitle.setVisibility(View.VISIBLE);
                    suptitle.setVisibility(View.VISIBLE);
                    tryagin.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);

                } else {
                    startActivity(new Intent(this,Show_tournament_images.class));
                }
            });
        }else{

            toptitle.setVisibility(View.INVISIBLE);
            suptitle.setVisibility(View.INVISIBLE);
            tryagin.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
/////////////////////////////////////recyclerView/////////////////////////////////////////////////////////////////////
            recyclerView = findViewById(R.id.show_match_images);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            list = new ArrayList<>();
            adapter = new Image_Adapter(this, list);
            recyclerView.setAdapter(adapter);
            db = FirebaseFirestore.getInstance();

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                match_title=bundle.getString("Mtitle");
                match_id=bundle.getString("Mid");
                check_user=bundle.getString("User");
                sub_count=bundle.getInt("sub_collection");
toolbar.setTitle("Title : "+match_title);

            }else{

            }
///////////////////////////////////////////////floatactionbutton///////////////////////////////////////////////////////
            floatingActionButton=findViewById(R.id.add_new_image);
            if(Objects.equals(check_user, "user"))
                floatingActionButton.setVisibility(View.INVISIBLE);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bundle.putString("Mid",match_id);
                    bundle.putString("Mtitle",match_title);
                    bundle.putInt("sub_collection",sub_count);
                    Intent intent=new Intent(Show_tournament_images.this, Tournament_related_images.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            swipe = findViewById(R.id.swiping);
            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Show_tournament_images.this.show_image_fierstore();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipe.setRefreshing(false);
                        }
                    }, 500);
                    // Toast.makeText(ShowActivity_image_viws.this, "Refreshing...", Toast.LENGTH_SHORT).show();
                }
            });
            show_image_fierstore();
        }

    }
    public void getcount(){
        sub_count--;
        db.collection("tournament_data").document(match_id).update("about_match_collection",String.valueOf(sub_count));

    }
    public void show_image_fierstore(){
        progressBar.setVisibility(View.VISIBLE);
        db.collection("Tournament_related_images").whereEqualTo("equaltitle",match_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                progressBar.setVisibility(View.INVISIBLE);
                for (DocumentSnapshot snapshot : task.getResult()) {
                    Image_model module = new Image_model(snapshot.getString("Image_url"), snapshot.getString("Image_Title"),snapshot.getString("Image_description"),snapshot.getString("Image_id"));
                    list.add(module);
                }
                adapter.notifyDataSetChanged();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Show_tournament_images.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();

            }
        });
    }
    public void getpermission(String url,String imageTitle){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String []permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_STORAGE_CODE);

            }else {
                startDownload(url,imageTitle);
            }
        }else {
            startDownload(url,imageTitle);
        }
    }

    private void startDownload(String geturl,String imageTitle) {
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(geturl));
        String title= URLUtil.guessFileName(geturl,null,null);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.allowScanningByMediaScanner();
        request.setTitle(imageTitle);
        request.setDescription("DOWNLOADING....");
        String cookie = CookieManager.getInstance().getCookie(geturl);
        request.addRequestHeader("cookie",cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);
        DownloadManager downloadManager=(DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
        Toast.makeText(this, "DOWNLOAD SUCCESSFULL!", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISSION_STORAGE_CODE:
            {
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Download again!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "PERMISSION_DENIED!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onBackPressed() {

        bundle.putString("User",check_user);
        Intent intent=new Intent(this, Upload_matchs_admain.class);
        intent.putExtras(bundle);
        startActivity(intent);


        //startActivity(new Intent(this,Upload_matchs_admain.class));
    }
    private boolean isConnect (Show_tournament_images mainActivity){
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilconnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wificonnect == null || !wificonnect.isConnected()) && (mobilconnect == null || !mobilconnect.isConnected());

    }





}