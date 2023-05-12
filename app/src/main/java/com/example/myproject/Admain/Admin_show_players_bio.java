package com.example.myproject.Admain;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myproject.Players.Players_Bio;
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

public class Admin_show_players_bio extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipe;
    private FirebaseFirestore db;
    Players_boi_adapter1 adapter;
    List<players_biodata> list;
    TextView select_year,show_year,junier,back_text1,senior,Back_text2,sub_junier,Back_text3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_show_tournament_images);
        setContentView(R.layout.activity_admin_show_players_bio);
        //////////////////////////////////toolbar///////////////////////////////////////////////
        toolbar = findViewById(R.id.image_view_tool);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        db =FirebaseFirestore.getInstance();
////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////prograssBar///////////////////////////////////////////////////////////
        progressBar = findViewById(R.id.progrssbar_show_image);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.INVISIBLE);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        TextView toptitle = findViewById(R.id.topmessage);
        TextView suptitle = findViewById(R.id.submassage);
        ImageView imageView = findViewById(R.id.nointernet);
        TextView tryagin = findViewById(R.id.tryagin);
        if (isConnect(com.example.myproject.Admain.Admin_show_players_bio.this)) {
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
                    startActivity(new Intent(Admin_show_players_bio.this,Admin_show_players_bio.class));
                }
            });
        } else {

////////////////////////////////////FULTER PLAYERS BIO//////////////////////////////////////////////////////////////////
            select_year=findViewById(R.id.select_year);
            show_year=findViewById(R.id.show_year);
            select_year.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        alertdialog_year();
                    }
                }
            });
            sub_junier=findViewById(R.id.sub_junior);
            back_text1=findViewById(R.id.change_text_to_back);
            junier=findViewById(R.id.junior);
            Back_text2=findViewById(R.id.change_text_to_back1);
            senior=findViewById(R.id.senior);
            Back_text3=findViewById(R.id.change_text_to_back2);
            sub_junier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sub_junier.setBackground(getDrawable(R.drawable.ic_baseline_arrow_back_24));
                    junier.setBackground(getDrawable(R.drawable.ic_baseline_person));
                    senior.setBackground(getDrawable(R.drawable.ic_baseline_scnior));
                    back_text1.setText("   Back");

                    players_fulter(2010,2020);
                    Toast.makeText(com.example.myproject.Admain.Admin_show_players_bio.this, "only show sub_junier players", Toast.LENGTH_SHORT).show();
                    sub_junier.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(), com.example.myproject.Admain.Admin_show_players_bio.class));
                        }
                    });
                    junier.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            junier.setBackground(getDrawable(R.drawable.ic_baseline_arrow_back_24));
                            sub_junier.setBackground(getDrawable(R.drawable.ic_baseline_junior));
                            senior.setBackground(getDrawable(R.drawable.ic_baseline_scnior));
                            Back_text2.setText("   Back");
                            players_fulter(2010,2002);
                            Toast.makeText(com.example.myproject.Admain.Admin_show_players_bio.this, "only show junier players", Toast.LENGTH_SHORT).show();
                            junier.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getApplicationContext(), com.example.myproject.Admain.Admin_show_players_bio.class));
                                }
                            });

                        }

                    });
                    senior.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            senior.setBackground(getDrawable(R.drawable.ic_baseline_arrow_back_24));
                            sub_junier.setBackground(getDrawable(R.drawable.ic_baseline_junior));
                            junier.setBackground(getDrawable(R.drawable.ic_baseline_person));
                            Back_text3.setText("   Back");
                            players_fulter(2001,1998);
                            Toast.makeText(com.example.myproject.Admain.Admin_show_players_bio.this, "only show senior players", Toast.LENGTH_SHORT).show();
                            senior.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getApplicationContext(), com.example.myproject.Admain.Admin_show_players_bio.class));
                                }
                            });
                        }
                    });
                }

            });
            junier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    junier.setBackground(getDrawable(R.drawable.ic_baseline_arrow_back_24));
                    sub_junier.setBackground(getDrawable(R.drawable.ic_baseline_junior));
                    senior.setBackground(getDrawable(R.drawable.ic_baseline_scnior));
                    Back_text2.setText("   Back");
                    players_fulter(2010,2002);
                    Toast.makeText(com.example.myproject.Admain.Admin_show_players_bio.this, "only show junier players", Toast.LENGTH_SHORT).show();
                    junier.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(), com.example.myproject.Admain.Admin_show_players_bio.class));
                        }
                    });
                    senior.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            senior.setBackground(getDrawable(R.drawable.ic_baseline_arrow_back_24));
                            sub_junier.setBackground(getDrawable(R.drawable.ic_baseline_junior));
                            junier.setBackground(getDrawable(R.drawable.ic_baseline_person));
                            Back_text3.setText("   Back");
                            players_fulter(2001,1998);
                            Toast.makeText(com.example.myproject.Admain.Admin_show_players_bio.this, "only show senior players", Toast.LENGTH_SHORT).show();
                            senior.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getApplicationContext(), com.example.myproject.Admain.Admin_show_players_bio.class));
                                }
                            });
                        }
                    });

                }

            });
            senior.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    senior.setBackground(getDrawable(R.drawable.ic_baseline_arrow_back_24));
                    sub_junier.setBackground(getDrawable(R.drawable.ic_baseline_junior));
                    junier.setBackground(getDrawable(R.drawable.ic_baseline_person));
                    Back_text3.setText("   Back");
                    players_fulter(2001,1998);
                    Toast.makeText(com.example.myproject.Admain.Admin_show_players_bio.this, "only show senior players", Toast.LENGTH_SHORT).show();
                    senior.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(), com.example.myproject.Admain.Admin_show_players_bio.class));
                        }
                    });

                }
            });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            toptitle.setVisibility(View.INVISIBLE);
            suptitle.setVisibility(View.INVISIBLE);
            tryagin.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
/////////////////////////////////////recyclerView/////////////////////////////////////////////////////////////////////
            recyclerView = findViewById(R.id.show_match_images);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            list = new ArrayList<>();
            adapter = new Players_boi_adapter1(this, list);
            recyclerView.setAdapter(adapter);
            show_biodata();
//////////////////////floatingAction//////////////////////////////////////////////////////////////////////////////////
            floatingActionButton=findViewById(R.id.add_new_image);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("email","Admin@gmail.com");
                    bundle.putInt("update",1);
                    Intent intent=new Intent(com.example.myproject.Admain.Admin_show_players_bio.this, Players_Bio.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
//////////////////////////////////////////call fun///////////////////////////////////

/////////////////////////////////////SWIPE////////////////////////////////////////////////////////////////
        swipe = findViewById(R.id.swiping);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                    }
                }, 500);
                // Toast.makeText(ShowActivity_image_viws.this, "Refreshing...", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void show_biodata(){
        progressBar.setVisibility(View.VISIBLE);
        db.collection("biodata").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                progressBar.setVisibility(View.INVISIBLE);

                for (DocumentSnapshot snapshot : task.getResult()) {
                    players_biodata module = new players_biodata(snapshot.getString("Image_url"), snapshot.getString("player_name"),snapshot.getString("player_dob"),snapshot.getString("player_age"),snapshot.getString("player_genter"),snapshot.getString("player_mobile"),snapshot.getString("player_position"),snapshot.getString("id"),snapshot.getString("email"));
                    list.add(module);
                }
                adapter.notifyDataSetChanged();
                Collections.reverse(list);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(com.example.myproject.Admain.Admin_show_players_bio.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
            }
        });

    }
    ///////////////////////////////////////////players_fulter////////////////////////////////////////////////////////////////////
    public void players_fulter(int order){
        progressBar.setVisibility(View.VISIBLE);
        db.collection("biodata").whereEqualTo("player_age_order",order).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                progressBar.setVisibility(View.INVISIBLE);

                for (DocumentSnapshot snapshot : task.getResult()) {
                    players_biodata module = new players_biodata(snapshot.getString("Image_url"), snapshot.getString("player_name"),snapshot.getString("player_dob"),snapshot.getString("player_age"),snapshot.getString("player_genter"),snapshot.getString("player_mobile"),snapshot.getString("player_position"),snapshot.getString("id"),snapshot.getString("email"));
                    list.add(module);
                }
                adapter.notifyDataSetChanged();
                Collections.reverse(list);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(com.example.myproject.Admain.Admin_show_players_bio.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void players_fulter(int less,int greate){
        progressBar.setVisibility(View.VISIBLE);
        db.collection("biodata").whereGreaterThanOrEqualTo("player_age_order",greate).whereLessThanOrEqualTo("player_age_order",less).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                progressBar.setVisibility(View.INVISIBLE);

                for (DocumentSnapshot snapshot : task.getResult()) {
                    players_biodata module = new players_biodata(snapshot.getString("Image_url"), snapshot.getString("player_name"),snapshot.getString("player_dob"),snapshot.getString("player_age"),snapshot.getString("player_genter"),snapshot.getString("player_mobile"),snapshot.getString("player_position"),snapshot.getString("id"),snapshot.getString("email"));
                    list.add(module);
                }
                adapter.notifyDataSetChanged();
                Collections.reverse(list);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(com.example.myproject.Admain.Admin_show_players_bio.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
            }
        });

    }
    /////////////////////////////////////////////////AlertBialog_year///////////////////////////////////////////////////////////
    public void alertdialog_year(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view1= LayoutInflater.from(this).inflate(R.layout.year_picker,findViewById(R.id.select_year_dialag));
        TextView selected_year;
        Button select_and_ok;
        selected_year=view1.findViewById(R.id.show_select_year);
        select_and_ok=view1.findViewById(R.id.year_ok_button);
        NumberPicker numberPicker=view1.findViewById(R.id.year_bicker);
        numberPicker.setMinValue(1970);
        numberPicker.setMaxValue(2030);
        final int[] year ={0};
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                selected_year.setText("ONLY SHOW [YEAR:"+i1+"] CATEGORY PLAYERS");
                show_year.setText("Select year:"+i1);
                year[0] =i1;

            }
        });
        builder.setView(view1);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
        select_and_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(year[0]!=0)
                    players_fulter(year[0]);

            }
        });
    }
    private boolean isConnect (com.example.myproject.Admain.Admin_show_players_bio mainActivity){
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilconnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wificonnect == null || !wificonnect.isConnected()) && (mobilconnect == null || !mobilconnect.isConnected());
    }
}