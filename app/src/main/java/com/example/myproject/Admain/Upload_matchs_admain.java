package com.example.myproject.Admain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myproject.R;
import com.example.myproject.User_mainpage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Upload_matchs_admain extends AppCompatActivity {
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
RecyclerView recyclerView;
Adapter_class adapter_class;
List<Model_class> list;
private FirebaseFirestore db;
  TextView win_match_only,wining_match_bottom_text,notwin_match_only,notwining_match_bottom_text;
  Toolbar toolbar;
String only_win_match_type=null,only_lose_match_type=null,check_user="novalue";
SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_matchs_admain);
        toolbar = findViewById(R.id.admain_maths_serch_tool);
        setSupportActionBar(toolbar);
        if(Objects.equals(check_user, "user")) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        TextView toptitle = findViewById(R.id.topmessage);
        TextView suptitle = findViewById(R.id.submassage);
        ImageView imageView = findViewById(R.id.nointernet);
        TextView tryagin = findViewById(R.id.tryagin);
        win_match_only=findViewById(R.id.win_match_only);
        wining_match_bottom_text=findViewById(R.id.wining_match_bottom_text);
        notwin_match_only=findViewById(R.id.not_win_match_only);
        notwining_match_bottom_text=findViewById(R.id.not_wining_match_bottom_text);
        progressBar = findViewById(R.id.progrssbar_upload_page);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        if (isConnect(Upload_matchs_admain.this)) {
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
                    startActivity(new Intent(this,Upload_matchs_admain.class));
                }
            });
        } else {

            toptitle.setVisibility(View.INVISIBLE);
            suptitle.setVisibility(View.INVISIBLE);
            tryagin.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
////////////////////////////////////recyclerView/////////////////////////////////////////////////////////////
            recyclerView = findViewById(R.id.show_all_maths);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
///////////////////////////////////////check admin////////////////////////////
            Bundle bundle=getIntent().getExtras();
            if(bundle!=null){
                check_user=bundle.getString("User");
            }else{

            }

            floatingActionButton = findViewById(R.id.add_new_matchs);
            if(Objects.equals(check_user, "user")){
                floatingActionButton.setVisibility(View.INVISIBLE);
            }
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Upload_matchs_admain.this, Admain_inputs_page.class);
                    startActivity(intent);
                }
            });
///////////////////////////////////////swipe//////////////////////////////////////////////////////////////////
            swipeRefreshLayout=findViewById(R.id.swip);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Upload_matchs_admain.this.show_all_match_datas();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 500);
                }
            });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////FloatingActionButton//////////////////////////////////////////////////////////////
            db = FirebaseFirestore.getInstance();
            list = new ArrayList<>();
            adapter_class = new Adapter_class(this, list);
            recyclerView.setAdapter(adapter_class);

            show_all_match_datas();




/////////////////////////////////////////show wining or notwining match/////////////////////////////////////////////////////////////////////
win_match_only.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
only_show_win_match();
only_win_match_type="win";
    }
});
notwin_match_only.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        only_lose_match_type="notwin";
        only_show_notwin_match();
    }
});
        }
    }
        public void show_all_match_datas () {
            //call_db
            db.collection("tournament_data").orderBy("store_order", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    list.clear();
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        Model_class module = new Model_class(snapshot.getString("id"),snapshot.getString("about_match_collection"),snapshot.getString("image_collection"),snapshot.getString("match_title"), snapshot.getString("match_place"), snapshot.getString("match_date"),snapshot.getString("tournament_status"),snapshot.getString("tournament_status_win"),snapshot.getString("tournament_status_notwin"), snapshot.getString("players_name_list"));
                        list.add(module);
                    }
                    adapter_class.notifyDataSetChanged();
                    Collections.reverse(list);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Upload_matchs_admain.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
                }
            });
        }

    public void only_show_win_match(){
        toolbar.setTitle("WINNING TOURNAMENT ONLY...");
        swipeRefreshLayout.setClickable(false);
        wining_match_bottom_text.setText("   BACK");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
       win_match_only.setBackground(this.getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        notwining_match_bottom_text.setText("LOSING MATCHES");
        notwin_match_only.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_event_notwin_24));
       win_match_only.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               only_win_match_type=null;
               Bundle bundle=new Bundle();
               bundle.putString("User",check_user);
               Intent intent=new Intent(Upload_matchs_admain.this, Upload_matchs_admain.class);
               intent.putExtras(bundle);
               startActivity(intent);
              // startActivity(new Intent(getApplicationContext(), Upload_matchs_admain.class));
           }
       });
        db.collection("tournament_data").whereEqualTo("tournament_status_win","WIN THE TOURNAMENT👍").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.INVISIBLE);
                list.clear();
                for (DocumentSnapshot snapshot : task.getResult()) {
                    Model_class module = new Model_class(snapshot.getString("id"),snapshot.getString("about_match_collection"),snapshot.getString("image_collection"), snapshot.getString("match_title"), snapshot.getString("match_place"), snapshot.getString("match_date"),snapshot.getString("tournament_status"),snapshot.getString("tournament_status_win"),snapshot.getString("tournament_status_notwin"), snapshot.getString("players_name_list"));
                    list.add(module);
                }
                adapter_class.notifyDataSetChanged();
                Collections.reverse(list);
                Toast.makeText(Upload_matchs_admain.this, "All win tha match", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Upload_matchs_admain.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
            }
        });
        notwin_match_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                only_show_notwin_match();
                swipeRefreshLayout.setClickable(false);
            }
        });
    }

    public void only_show_notwin_match(){
        toolbar.setTitle("LOSING TOURNAMENT ONLY...");
        swipeRefreshLayout.setClickable(false);
        wining_match_bottom_text.setText("WINNING MATCHSES");
        notwining_match_bottom_text.setText("   BACK");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        notwin_match_only.setBackground(this.getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        win_match_only.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_event_win_24));
        notwin_match_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                only_lose_match_type=null;
                Bundle bundle=new Bundle();
                bundle.putString("User",check_user);
                Intent intent=new Intent(Upload_matchs_admain.this, Upload_matchs_admain.class);
                intent.putExtras(bundle);
                startActivity(intent);
                //startActivity(new Intent(getApplicationContext(), Upload_matchs_admain.class));
            }
        });
        db.collection("tournament_data").whereEqualTo("tournament_status_notwin","LOSE THE TOURNAMENT👎").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.INVISIBLE);
                list.clear();
                for (DocumentSnapshot snapshot : task.getResult()) {
                    Model_class module = new Model_class(snapshot.getString("id"),snapshot.getString("about_match_collection"),snapshot.getString("image_collection"), snapshot.getString("match_title"), snapshot.getString("match_place"), snapshot.getString("match_date"),snapshot.getString("tournament_status"),snapshot.getString("tournament_status_win"),snapshot.getString("tournament_status_notwin"), snapshot.getString("players_name_list"));
                    list.add(module);
                }
                adapter_class.notifyDataSetChanged();
                Collections.reverse(list);
                Toast.makeText(Upload_matchs_admain.this, "All win tha match", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Upload_matchs_admain.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
            }
        });
win_match_only.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        only_show_win_match();
        swipeRefreshLayout.setClickable(false);
    }
});
    }
        private boolean isConnect (Upload_matchs_admain mainActivity){
            ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wificonnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobilconnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return (wificonnect == null || !wificonnect.isConnected()) && (mobilconnect == null || !mobilconnect.isConnected());


        }
////////////////////////////////////////////////////////search_menu////////////////////////////////////////////////////////////////
@SuppressLint("ResourceAsColor")
@RequiresApi(api = Build.VERSION_CODES.M)
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.serch_matchs,menu);
    MenuItem item=menu.findItem(R.id.serch_matchs);

    SearchView searchView= (SearchView) item.getActionView();
    //SearchView.SearchAutoComplete searchAutoComplete=(SearchView.SearchAutoComplete)searchView.findViewById(R.id.show_admain_maths);

    searchView.setQueryHint("Search Title");
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            search(s);

            //Toast.makeText(Admain_show_all_match_data.this,"No data found",Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            search(s);

            //Toast.makeText(Admain_show_all_match_data.this,"No data found",Toast.LENGTH_SHORT).show();
            return false;
        }
    });
    return super.onCreateOptionsMenu(menu);
}
    public void search(String m_title){
        db.collection("tournament_data").orderBy("match_title").startAt(m_title).endAt(m_title+"~").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                for(DocumentSnapshot snapshot :task.getResult()){
                    Model_class module = new Model_class(snapshot.getString("id"),snapshot.getString("about_match_collection"),snapshot.getString("image_collection"), snapshot.getString("match_title"), snapshot.getString("match_place"), snapshot.getString("match_date"),snapshot.getString("tournament_status"),snapshot.getString("tournament_status_win"),snapshot.getString("tournament_status_notwin"), snapshot.getString("players_name_list"));
                    list.add(module);


                }
                RecyclerView recyclerView = findViewById(R.id.show_all_maths);
                recyclerView.setHasFixedSize(true);
                adapter_class=new Adapter_class(Upload_matchs_admain.this,list);

                recyclerView.setAdapter(adapter_class);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Upload_matchs_admain.this,"No data found",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Bundle bundle=new Bundle();
        bundle.putString("User",check_user);
        if (only_win_match_type != null) {
            only_win_match_type=null;
            Intent intent=new Intent(this, Upload_matchs_admain.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }else if(only_lose_match_type!=null){
            only_lose_match_type=null;
            Intent intent=new Intent(this, Upload_matchs_admain.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else {
            if(Objects.equals(check_user, "user")) {
                Intent intent = new Intent(this, User_mainpage.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                Intent intent = new Intent(this, Admain_Mainpage.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }
    }


}