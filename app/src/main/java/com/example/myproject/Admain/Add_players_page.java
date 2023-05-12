package com.example.myproject.Admain;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myproject.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class Add_players_page extends AppCompatActivity {
EditText players_name;
TextView upload_players_name_to_firebase;
ProgressBar progressBar;
    private FirebaseFirestore db;
    String id,prives_players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players_page);
        db=FirebaseFirestore.getInstance();
        players_name=findViewById(R.id.add_players);
        progressBar=findViewById(R.id.upload_players_processbra);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        upload_players_name_to_firebase=findViewById(R.id.upload_players_to_firebase);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            id=bundle.getString("getid");
            prives_players=bundle.getString("getplayers");

        }else{
            Toast.makeText(this, "NO data item", Toast.LENGTH_SHORT).show();
        }
        if(prives_players.equals("          Please upload players name list soon...!")){

        }else{
            players_name.setText(prives_players);
        }
        upload_players_name_to_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getPlayers_name=players_name.getText().toString();

                if(getPlayers_name.isEmpty()){
                    Toast.makeText(Add_players_page.this, "Empty field is not allow!", Toast.LENGTH_SHORT).show();
                }else{

                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Add_players_page.this, "data add successful...", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(Add_players_page.this, Upload_matchs_admain.class);
                            startActivity(intent);
                        }
                    }, 600);
                    db.collection("tournament_data").document(id).update("players_name_list",players_name.getText().toString());

                }

            }
        });

    }
}