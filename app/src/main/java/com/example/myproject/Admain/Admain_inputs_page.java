package com.example.myproject.Admain;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myproject.FcmNotificationsSender;
import com.example.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
public class Admain_inputs_page extends AppCompatActivity {

    EditText match_title,match_place,edit_players;
    TextView upload_to_fierbase,match_date,edit_upload_to_firebase,upload_botton_text,edit_bottom_text,text_players;
    double date_order_store_firebase;
    ProgressBar upload_matchs_progress,edit_match_progress;
    private FirebaseFirestore db;
    private String get_match_title,get_match_place,get_match_date,get_match_id,get_macth_players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admain_inputs_page);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        match_title=findViewById(R.id.match_TITLE);
        match_place=findViewById(R.id.match_PLASE);
        match_date=findViewById(R.id.match_DATE);
        upload_to_fierbase=findViewById(R.id.upload_to_fierbase);
        upload_botton_text=findViewById(R.id.upload_bottom_text);
        upload_matchs_progress=findViewById(R.id.upload_match_process);
        text_players=findViewById(R.id.text_players);
        edit_players=findViewById(R.id.edit_players);
        edit_upload_to_firebase=findViewById(R.id.edit_all_data_to_firebase);
        edit_bottom_text=findViewById(R.id.edit_bottom_text);
        edit_match_progress=findViewById(R.id.edit_match_process);
        upload_matchs_progress.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        edit_match_progress.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        db =FirebaseFirestore.getInstance();
///////////////////////////////////////////////////////function////////////////////////////////////////////////////////
        match_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int MONTH=calendar.get(Calendar.MONTH);
                int YEAR=calendar.get(Calendar.YEAR);
                int DAY=calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog=new DatePickerDialog(Admain_inputs_page.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1=i1+1;
                        match_date.setText(i2+"/"+i1+"/"+i);
                        date_order_store_firebase=(i*10000)+(i1*100)+i2;

                    }
                },YEAR,MONTH,DAY);
                datePickerDialog.show();
            }
        });
        upload_to_fierbase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title1=match_title.getText().toString();
                String place1=match_place.getText().toString();
                String date1=match_date.getText().toString();
                String players1=edit_players.getText().toString();
                Bundle bundle1=getIntent().getExtras();
                if(bundle1!=null){
                    update_to_firebasestorage(title1,place1,date1,get_match_id,players1);
                }else {
                    String id= UUID.randomUUID().toString();
                    save_to_Fierstore(id,title1,place1,date1);
                }

            }


        });
        edit_upload_to_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title1=match_title.getText().toString();
                String place1=match_place.getText().toString();
                String date1=match_date.getText().toString();
                String players1=edit_players.getText().toString();
                Bundle bundle1=getIntent().getExtras();
                if(bundle1!=null){
                    update_to_firebasestorage(title1,place1,date1,get_match_id,players1);
                }
            }
        });
//////////////////////////////////////////////////edit_match(bundle)///////////////////////////////////////////////////////////////////
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            upload_to_fierbase.setText("UPDATE");
            get_match_title=bundle.getString("ETitle");
            get_match_place=bundle.getString("Eplace");
            get_match_date=bundle.getString("Edate");
            get_match_id=bundle.getString("Eid");
            get_macth_players=bundle.getString("Eplayers");
            match_title.setText(get_match_title);
            match_place.setText(get_match_place);
            match_date.setText(get_match_date);
            if(get_macth_players.equals("          Please upload players name list soon...!")){

            }else{
                edit_players.setText(get_macth_players);
                edit_players.setVisibility(View.VISIBLE);
            }
            upload_to_fierbase.setVisibility(View.INVISIBLE);
            upload_botton_text.setVisibility(View.INVISIBLE);
            edit_upload_to_firebase.setVisibility(View.VISIBLE);
            edit_bottom_text.setVisibility(View.VISIBLE);
            text_players.setVisibility(View.VISIBLE);
        }else{

        }

    }///////////////end onCreate method
    public void save_to_Fierstore(String id,String title1,String place1,String date1){

        if(title1.isEmpty()){
            Toast.makeText(this, "Please enter a match title!", Toast.LENGTH_SHORT).show();
        }else if(place1.isEmpty()){
            Toast.makeText(this, "Plase enter a match place!", Toast.LENGTH_SHORT).show();
        }else if(date1.isEmpty()){
            Toast.makeText(this, "Plase select a match date!", Toast.LENGTH_SHORT).show();
        }else if(title1.isEmpty() && place1.isEmpty() && date1.isEmpty()){
            Toast.makeText(this, "Empty field is not allowed!", Toast.LENGTH_SHORT).show();
        }else {
            upload_matchs_progress.setVisibility(View.VISIBLE);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("match_title", title1);
            map.put("match_place", place1);
            map.put("match_date",date1);
            map.put("store_order",date_order_store_firebase);
            map.put("tournament_status","ALL THE BEST 👍");
            map.put("tournament_status_win","");
            map.put("tournament_status_notwin","");
            map.put("players_name_list","                upload players name list soon...!");
            map.put("about_match_collection","0");
            map.put("image_collection","0");
            db.collection("tournament_data").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    upload_matchs_progress.setVisibility(View.INVISIBLE);
                    FcmNotificationsSender notificationsSender=new FcmNotificationsSender("/topics/all",title1,place1,getApplicationContext(), Admain_inputs_page.this);
                    notificationsSender.SendNotifications();
                    Toast.makeText(Admain_inputs_page.this, "Match published successfull...!", Toast.LENGTH_LONG).show();
                    //Toast.makeText(Admain_inputs_page.this, "Data added successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Admain_inputs_page.this, Upload_matchs_admain.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    upload_matchs_progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(Admain_inputs_page.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
/////////////////////////////////////////update_to_firebase//////////////////////////////////////////////////////////////////////////////////////////
public void update_to_firebasestorage(String edit_title,String edit_place,String edit_date,String id,String edit_players){
    edit_match_progress.setVisibility(View.VISIBLE);
    if(edit_title.isEmpty()){
        Toast.makeText(this, "Please enter a match title!", Toast.LENGTH_SHORT).show();
    }else if(edit_place.isEmpty()){
        Toast.makeText(this, "Plase enter a match place!", Toast.LENGTH_SHORT).show();
    }else if(edit_date.isEmpty()){
        Toast.makeText(this, "Plase select a match date!", Toast.LENGTH_SHORT).show();
    }else if(edit_title.isEmpty() && edit_place.isEmpty() && edit_date.isEmpty()){
        Toast.makeText(this, "Empty field is not allowed!", Toast.LENGTH_SHORT).show();
    }else{
        db.collection("tournament_data").document(id).update("match_title",edit_title,"match_place",edit_place,"match_date",edit_date,"players_name_list",edit_players).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                edit_match_progress.setVisibility(View.INVISIBLE);
                Toast.makeText(Admain_inputs_page.this, "Data update successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Admain_inputs_page.this, Upload_matchs_admain.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                edit_match_progress.setVisibility(View.INVISIBLE);
                Toast.makeText(Admain_inputs_page.this, "Something woring,plase check your network and Tryagin!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(Admain_inputs_page.this, Upload_matchs_admain.class));
            }
        });

}}

}