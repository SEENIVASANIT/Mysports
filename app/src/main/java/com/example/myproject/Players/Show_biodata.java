
package com.example.myproject.Players;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myproject.Admain.Players_boi_adapter;
import com.example.myproject.Admain.players_biodata;
import com.example.myproject.R;
import com.example.myproject.User_mainpage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Show_biodata extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipe;
    private FirebaseFirestore db;
    Players_boi_adapter adapter;
    List<players_biodata> list;
   public String email_id=null;
    SharedPreferences sharedPreferences;
    String save_email_id,show_dio=null;
   boolean check_box;
   int count=0;
    SharedPreferences.Editor editor;
    CheckBox checkBox;
    TextInputEditText textInputEditText;
    TextView select_year,show_year,junier,back_text1,senior,Back_text2,sub_junier,Back_text3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_show_tournament_images);
        setContentView(R.layout.activity_show_biodata);
 //////////////////////////////////toolbar///////////////////////////////////////////////
        toolbar = findViewById(R.id.image_view_tool);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("MYBIO DATA!");
        db =FirebaseFirestore.getInstance();
////////////////////////////////////////////////////////////////////////////////////////////////
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        editor=sharedPreferences.edit();
        loadData();

///////////////////////////////////////////////prograssBar///////////////////////////////////////////////////////////
        progressBar = findViewById(R.id.progrssbar_show_image);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.INVISIBLE);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        TextView toptitle = findViewById(R.id.topmessage);
        TextView suptitle = findViewById(R.id.submassage);
        ImageView imageView = findViewById(R.id.nointernet);
        TextView tryagin = findViewById(R.id.tryagin);
        if (isConnect(Show_biodata.this)) {
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
                    startActivity(new Intent(this, Show_biodata.class));
                }
            });
        } else {
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                email_id= bundle.getString("email");
                show_dio=bundle.getString("show_bio");
            }
            if(show_dio!=null){
                show_biodata(email_id);
            }else{
                alertdialog();
            }

            sharedPreferences=getSharedPreferences("email",MODE_PRIVATE);
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
            adapter = new Players_boi_adapter(this, list);
            recyclerView.setAdapter(adapter);
//////////////////////floatingAction//////////////////////////////////////////////////////////////////////////////////
            floatingActionButton=findViewById(R.id.add_new_image);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("email",email_id);
                    bundle.putInt("update",1);
                    Intent intent=new Intent(Show_biodata.this, Players_Bio.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
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
    public void show_biodata(String email){
        email_id=email;
        progressBar.setVisibility(View.VISIBLE);
        db.collection("biodata").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                Toast.makeText(Show_biodata.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
            }
        });

    }
//////////////////////////////////////////////////AlertBialog/////////////////////////////////////////////////////////////////

    public void alertdialog(){
     AlertDialog.Builder builder=new AlertDialog.Builder(this);
    builder.setCancelable(false);
    View view1= LayoutInflater.from(this).inflate(R.layout.players_bio_dialog,findViewById(R.id.playres_bio_dialag));
    TextView back_to_mainpage,create_new_account,enterbiodatabage;
    back_to_mainpage=view1.findViewById(R.id.back_to_mainpage);
    textInputEditText=view1.findViewById(R.id.player_email);
    create_new_account=view1.findViewById(R.id.new_account);
    checkBox=view1.findViewById(R.id.save_email);
    TextView new_account_text_visible=view1.findViewById(R.id.new_account_letter_viaible);
    enterbiodatabage=view1.findViewById(R.id.enter_biodata_page);
    builder.setView(view1);
    AlertDialog alertDialog=builder.create();
    alertDialog.show();
    enterbiodatabage.setOnClickListener(new View.OnClickListener() {
        @SuppressLint("NewApi")
        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onClick(View view) {
            loadData();
            if(count!=1){
                 if(save_email_id.equals(textInputEditText.getText().toString())){
                    show_biodata(textInputEditText.getText().toString());
                    if(checkBox.isChecked()){
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("text",textInputEditText.getText().toString());
                        editor.putBoolean("bool",true);
                        editor.apply();
                    }else{
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("text",textInputEditText.getText().toString());
                        editor.putBoolean("bool",false);
                        editor.apply();
                    }

                     show_biodata(textInputEditText.getText().toString());
                     alertDialog.dismiss();

                }
              else if (textInputEditText.getText().toString().isEmpty()) {
                    Toast.makeText(Show_biodata.this, "Please enter email id!", Toast.LENGTH_SHORT).show();
                }
              else if(save_email_id!=textInputEditText.getText().toString()){
                  Toast.makeText(Show_biodata.this, "This "+"'"+textInputEditText.getText().toString()+"'"+"email id is wrong!!!\nOtherwise create a new account!", Toast.LENGTH_LONG).show();
                }
            }else {
                if (!textInputEditText.getText().toString().isEmpty()){
                    if(checkBox.isChecked()){
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("text",textInputEditText.getText().toString());
                        editor.putBoolean("bool",true);
                        editor.apply();
                    }else{
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("text",textInputEditText.getText().toString());
                        editor.putBoolean("bool",false);
                        editor.apply();
                    }
                    show_biodata(textInputEditText.getText().toString());
                    alertDialog.dismiss();

                }else{
                    Toast.makeText(Show_biodata.this, "Please enter email id!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });
    back_to_mainpage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle=new Bundle();
            bundle.putString("User","user");
            Intent intent=new Intent(Show_biodata.this, User_mainpage.class);
            intent.putExtras(bundle);
            startActivity(intent);
            //startActivity(new Intent(Show_biodata.this, Admain_Mainpage.class));
        }
    });
    create_new_account.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            create_new_account.setVisibility(View.INVISIBLE);
            textInputEditText.setText("");
            count=1;
            new_account_text_visible.setVisibility(View.VISIBLE);
            //textInputEditText.setHint("Enter your email id");
        }
    });


        if(check_box) {
            checkBox.setChecked(true);
            textInputEditText.setText(save_email_id);
        }
        else
            checkBox.setChecked(false);
}
public void loadData(){
        sharedPreferences=getSharedPreferences("email",MODE_PRIVATE);
        save_email_id=sharedPreferences.getString("text","0@#%*^0919");
        check_box=sharedPreferences.getBoolean("bool",false);


}
    private boolean isConnect (Show_biodata mainActivity){
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilconnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wificonnect == null || !wificonnect.isConnected()) && (mobilconnect == null || !mobilconnect.isConnected());
    }
}