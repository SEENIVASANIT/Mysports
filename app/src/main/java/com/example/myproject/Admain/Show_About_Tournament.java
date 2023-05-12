package com.example.myproject.Admain;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Show_About_Tournament extends AppCompatActivity {
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    About_Adapter adapter;
    List<About_model> list;
    private FirebaseFirestore db;
    SwipeRefreshLayout swipe;
    String match_title,match_id,check_user=null;
    int sub_count;
    TextView textView;
        Toolbar toolbar;
    Bundle bundle=new Bundle();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_show_tournament_images);
            toolbar = findViewById(R.id.image_view_tool);
            setSupportActionBar(toolbar);
            if(Objects.equals(check_user, "user")) {
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            }
            textView=findViewById(R.id.noitem);
///////////////////////////////////////////////prograssBar///////////////////////////////////////////////////////////
            progressBar = findViewById(R.id.progrssbar_show_image);
            progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            TextView toptitle = findViewById(R.id.topmessage);
            TextView suptitle = findViewById(R.id.submassage);
            ImageView imageView = findViewById(R.id.nointernet);
            TextView tryagin = findViewById(R.id.tryagin);
            if (isConnect(Show_About_Tournament.this)) {
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
                        startActivity(new Intent(this,Show_About_Tournament.class));
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
                adapter = new About_Adapter(this, list);
                recyclerView.setAdapter(adapter);
                db = FirebaseFirestore.getInstance();
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    match_title=bundle.getString("Mtitle");
                    match_id=bundle.getString("Mid");
                    check_user=bundle.getString("User");
                    sub_count=bundle.getInt("sub_collection");
                    toolbar.setTitle(match_title);


                }else{
                }

///////////////////////////////////////////////floatactionbutton///////////////////////////////////////////////////////

                floatingActionButton=findViewById(R.id.add_new_image);
                if(Objects.equals(check_user, "user")){
                    floatingActionButton.setVisibility(View.GONE);
                }
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    int count=1;
                    @Override
                    public void onClick(View view) {

                        bundle.putString("Mid",match_id);
                        bundle.putString("Mtitle",match_title);
                        bundle.putInt("sub_collection",sub_count);
                        bundle.putInt("count",count);
                        Intent intent=new Intent(Show_About_Tournament.this, About_tournament.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
/////////////////////////////////////////////////swipe////////////////////////////////////////////////////////////////////////////
                swipe = findViewById(R.id.swiping);
                swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Show_About_Tournament.this.show_image_fierstore();
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

public  void getcount(){
            sub_count--;
    db.collection("tournament_data").document(match_id).update("about_match_collection",String.valueOf(sub_count));

}
    public void show_image_fierstore(){
        if(adapter.getItemCount()==0){
            textView.setVisibility(View.VISIBLE);
        }else{
            textView.setVisibility(View.INVISIBLE);
        }
        progressBar.setVisibility(View.VISIBLE);
        db.collection("About_tournament_data").whereEqualTo("equaltitle",match_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                progressBar.setVisibility(View.INVISIBLE);

                for (DocumentSnapshot snapshot : task.getResult()) {
                    About_model module = new About_model(snapshot.getString("equaltitle"),snapshot.getString("id"), snapshot.getString("match_position"),snapshot.getString("competition"),snapshot.getString("competition_date"),snapshot.getString("match_goal"),snapshot.getString("match_status"),snapshot.getString("match_description"));
                    list.add(module);
                }
                adapter.notifyDataSetChanged();
                Collections.reverse(list);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Show_About_Tournament.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        bundle.putString("User",check_user);
        Intent intent=new Intent(this, Upload_matchs_admain.class);
        intent.putExtras(bundle);
        startActivity(intent);
        //startActivity(new Intent(this,Upload_matchs_admain.class));
    }
    private boolean isConnect (Show_About_Tournament mainActivity){
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilconnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wificonnect == null || !wificonnect.isConnected()) && (mobilconnect == null || !mobilconnect.isConnected());


    }


}