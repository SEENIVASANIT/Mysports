package com.example.myproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.myproject.Admain.Admain_Mainpage;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Common_login extends AppCompatActivity {
    TextInputEditText username,user_password,admain_password;
    TextInputLayout textInputLayout,textInputLayout2;
    TextView Change_to_admain,for_admain_oly,enter_to_app;
    SwitchCompat aSwitch;
    ProgressBar login_progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_login);
        username=findViewById(R.id.username);
        user_password=findViewById(R.id.user_login_password);
        admain_password=findViewById(R.id.admain_login_password);
        textInputLayout=(TextInputLayout) findViewById(R.id.admain_login_password_layout);
        textInputLayout2=(TextInputLayout) findViewById(R.id.user_login_password_layout);
        Change_to_admain=findViewById(R.id.change_to_admain);
        for_admain_oly=findViewById(R.id.visable_admain);
        enter_to_app=findViewById(R.id.enter_app);
        login_progressBar=findViewById(R.id.login_process);
        login_progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        aSwitch=(SwitchCompat) findViewById(R.id.login_user_or_admain);
        enter_to_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aSwitch.isChecked()){


                }else {

                            String store_pass="sthc123";
                            if(username.getText().toString().isEmpty()){
                                Toast.makeText(Common_login.this, "Please enter name!", Toast.LENGTH_SHORT).show();

                            }else if (user_password.getText().toString().isEmpty()){
                                Toast.makeText(Common_login.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                            }
                            else if (!username.getText().toString().isEmpty() && user_password.getText().toString().equals(store_pass)){
                                //enter_to_app.setBackgroundColor(Color.parseColor("#ffe6e6"));

                                login_progressBar.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        login_progressBar.setVisibility(View.INVISIBLE);
                                        Bundle bundle=new Bundle();
                                        bundle.putString("User","user");
                                        Intent intent=new Intent(Common_login.this, User_mainpage.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                },1000);


                                Toast.makeText(Common_login.this, "Welcome"+" "+username.getText().toString(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Common_login.this, "plase enter correct password...", Toast.LENGTH_SHORT).show();



                            }

                }


            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Change_to_admain.setText("LOGIN/ADMIN");
                    Change_to_admain.setTextColor(Color.parseColor("#ff0000"));
                    for_admain_oly.setVisibility(View.VISIBLE);
                    textInputLayout.setVisibility(View.VISIBLE);
                    textInputLayout2.setVisibility(View.INVISIBLE);

                    enter_to_app.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String store_pass=new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
                            String store_pass1=new SimpleDateFormat("h:mm", Locale.getDefault()).format(new Date());
                            System.out.println(store_pass1);
                            if(username.getText().toString().isEmpty()){
                                Toast.makeText(Common_login.this, "Please enter name!", Toast.LENGTH_SHORT).show();

                            }else if (admain_password.getText().toString().isEmpty()){
                                Toast.makeText(Common_login.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                            }
                            else if (!username.getText().toString().isEmpty() && (admain_password.getText().toString().equals(store_pass)) || (admain_password.getText().toString().equals(store_pass1))){
                               // enter_to_app.setBackgroundColor(Color.parseColor("#936c6c"));
                                login_progressBar.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        login_progressBar.setVisibility(View.INVISIBLE);
                                        Intent intent=new Intent(Common_login.this, Admain_Mainpage.class);
                                        startActivity(intent);
                                    }
                                },700);


                                Toast.makeText(Common_login.this, "Welcome"+" "+username.getText().toString(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Common_login.this, "plase enter correct password...", Toast.LENGTH_SHORT).show();



                            }
                        }
                    });
                }else {
                    Change_to_admain.setText("LOGIN/USER");
                    for_admain_oly.setVisibility(View.INVISIBLE);
                    Change_to_admain.setTextColor(Color.parseColor("#09C611"));
                    textInputLayout.setVisibility(View.INVISIBLE);
                    textInputLayout2.setVisibility(View.VISIBLE);
                    enter_to_app.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                                String store_pass="sthc123";

                                if(username.getText().toString().isEmpty()){
                                    Toast.makeText(Common_login.this, "Please enter name!", Toast.LENGTH_SHORT).show();

                                }else if (user_password.getText().toString().isEmpty()){
                                    Toast.makeText(Common_login.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                                }
                                else if (!username.getText().toString().isEmpty() && user_password.getText().toString().equals(store_pass)){
                                //    enter_to_app.setBackgroundColor(Color.parseColor("#ffe6e6"));
                                    Bundle bundle=new Bundle();
                                    bundle.putString("User","user");
                                    Intent intent=new Intent(Common_login.this, User_mainpage.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
//                                    Intent intent=new Intent(Common_login.this,Admain_Mainpage.class);
//                                    startActivity(intent);

                                    Toast.makeText(Common_login.this, "User login...", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Common_login.this, "plase enter correct password...", Toast.LENGTH_SHORT).show();



                                }

                            }



                    });


                }
            }
        });

    }


}