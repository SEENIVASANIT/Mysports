package com.example.myproject.Admain;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class About_tournament extends AppCompatActivity {
    private FirebaseFirestore db;
    Toolbar toolbar;
    TextInputEditText match_position,competition,competition_date,match_goal,match_status,match_description;
    ProgressBar progressBar;
    TextView upload_firebase;
    String Mid,title,get_position, get_competition,get_C_date,get_goal,get_status,get_description,get_Eid,get_equlid;
int count,sub_collection=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_tournament);
        toolbar = findViewById(R.id.about_tournament_tool);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        String[] arry = new String[]{"Win tha match", "Lose the match", "Draw the match"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.drop_down_menu, arry);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.status_menu);
        autoCompleteTextView.setAdapter(adapter);
        match_position = findViewById(R.id.match_position);
        competition = findViewById(R.id.competition);
        competition_date = findViewById(R.id.competition_date);
        match_goal = findViewById(R.id.match_goal);
        match_description = findViewById(R.id.match_description);
        progressBar = findViewById(R.id.about_match_progressbra);
        upload_firebase = findViewById(R.id.upload_about_match_to_firebase);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            title = bundle.getString("Mtitle");
            Mid= bundle.getString("Mid");
            count= bundle.getInt("count");
            sub_collection= bundle.getInt("sub_collection");
            toolbar.setTitle(title);
        }
        competition_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(About_tournament.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        competition_date.setText(i2 + "/" + i1 + "/" + i);
                    }
                }, YEAR, MONTH, DAY);
                datePickerDialog.show();
            }
        });
        upload_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String position = match_position.getText().toString();
                String comp = competition.getText().toString();
                String date = competition_date.getText().toString();
                String goal = match_goal.getText().toString();
                String status = autoCompleteTextView.getText().toString();
                String description = match_description.getText().toString();
                if (count!=1){
                    update_to_firebase(position,comp,date,goal,status,description,get_Eid,get_equlid);
                }else{
                    String id = UUID.randomUUID().toString();
                    about_match_upload_firebase(position, comp, date, goal, status, description, id);
                }


            }
        });
//////////////////////////////////////////////////edit_match(bundle)///////////////////////////////////////////////////////////////////
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1!= null) {
            upload_firebase.setText("UPDATE");
            get_position = bundle.getString("Ematch_position");
           get_competition = bundle.getString("Ecompetition");
            get_C_date = bundle.getString("Ecompetition_date");
            get_goal = bundle.getString("Ematch_goal");
           get_status = bundle.getString("Ematch_status");
           get_description = bundle.getString("Ematch_description");
           get_equlid = bundle.getString("equaltitle");
            get_Eid = bundle.getString("Eid1");
           match_position.setText(get_position);
           competition.setText(get_competition);
           competition_date.setText(get_C_date);
          // match_goal.setText(get_goal);
            //System.out.println(get_goal);
          // autoCompleteTextView.setText(get_status);
           match_description.setText(get_description);
        }
    }
public void about_match_upload_firebase(String position,String comp,String date,String goal,String status,String description,String id){
        if (position.isEmpty()&& comp.isEmpty() && date.isEmpty() && goal.isEmpty() && status.isEmpty() && description.isEmpty()){
            Toast.makeText(this, "Empty field is not allowed", Toast.LENGTH_SHORT).show();
        }else if (position.isEmpty()){
            Toast.makeText(this, "please enter match position", Toast.LENGTH_SHORT).show();
        }else if (comp.isEmpty()){
            Toast.makeText(this, "please enter competition", Toast.LENGTH_SHORT).show();
        }else if (date.isEmpty()){
            Toast.makeText(this, "please enter competition_date", Toast.LENGTH_SHORT).show();
        }else if (goal.isEmpty()){
            Toast.makeText(this, "please enter match goal", Toast.LENGTH_SHORT).show();
        }else if (status.isEmpty()){
            Toast.makeText(this, "please enter match status", Toast.LENGTH_SHORT).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            upload_firebase.setLongClickable(false);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("match_position",position);
            map.put("competition",comp);
            map.put("competition_date",date);
            map.put("match_goal",goal);
            map.put("match_status",status);
            map.put("match_description",description);
            map.put("equaltitle",Mid);
            db.collection("About_tournament_data").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    sub_collection++;
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(About_tournament.this, "Data added successful!", Toast.LENGTH_SHORT).show();
                    Bundle bundle=new Bundle();
                    bundle.putString("Mtitle",title);
                    bundle.putString("Mid",Mid);
                    bundle.putInt("sub_collection",sub_collection);
                    db.collection("tournament_data").document(Mid).update("about_match_collection",String.valueOf(sub_collection));
                   // db.collection("About_tournament_data").document("sub_collection").update("sub_collection",String.valueOf(sub_collection));

                    Intent intent=new Intent(About_tournament.this, Show_About_Tournament.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(About_tournament.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
                }
            });
        }

}
public void update_to_firebase(String get_position,String get_competition,String get_C_date,String get_goal,String get_status,String get_description,String get_Eid,String get_equlid){
    progressBar.setVisibility(View.VISIBLE);
    if (get_position.isEmpty()&& get_competition.isEmpty() && get_C_date.isEmpty() && get_goal.isEmpty() && get_status.isEmpty() && get_description.isEmpty()){
        Toast.makeText(this, "Empty field is not allowed", Toast.LENGTH_SHORT).show();
    }else if (get_position.isEmpty()){
        Toast.makeText(this, "please enter match position", Toast.LENGTH_SHORT).show();
    }else if (get_competition.isEmpty()){
        Toast.makeText(this, "please enter competition", Toast.LENGTH_SHORT).show();
    }else if (get_C_date.isEmpty()){
        Toast.makeText(this, "please enter competition_date", Toast.LENGTH_SHORT).show();
    }else if (get_goal.isEmpty()){
        Toast.makeText(this, "please enter match goal", Toast.LENGTH_SHORT).show();
    }else if (get_status.isEmpty()){
        Toast.makeText(this, "please enter match status", Toast.LENGTH_SHORT).show();
    }else{
    db.collection("About_tournament_data").document(get_Eid).update("match_position",get_position,"competition",get_competition,"competition_date",get_C_date,"match_goal",get_goal,"match_status",get_status,"match_description",get_description,"equaltitle",get_equlid).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(About_tournament.this, "Data added successful!", Toast.LENGTH_SHORT).show();
            Bundle bundle=new Bundle();
            bundle.putString("Mtitle","About this match");
            bundle.putString("Mid",get_equlid);
            Intent intent=new Intent(About_tournament.this, Show_About_Tournament.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(About_tournament.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
        }
    });
}}

    @Override
    public void onBackPressed() {
        if(count==1){
            Bundle bundle=new Bundle();
            bundle.putString("Mtitle","About this match");
            bundle.putString("Mid",Mid);
            Intent intent=new Intent(About_tournament.this, Show_About_Tournament.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            Bundle bundle=new Bundle();
            bundle.putString("Mtitle","About this match");
            bundle.putString("Mid",get_equlid);
            Intent intent=new Intent(About_tournament.this, Show_About_Tournament.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
}
