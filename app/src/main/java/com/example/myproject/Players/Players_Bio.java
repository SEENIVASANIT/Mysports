package com.example.myproject.Players;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Players_Bio extends AppCompatActivity {
CircleImageView circleImageView;
TextInputEditText player_name,player_dob,player_age,player_mobile,player_position;
RadioButton male,female;
    String edit_name,edit_dob,edit_age,edit_genter,edit_mobile,edit_position,B_id,email1,emaild=null,male_or_female;
TextView submit,back;
    ProgressDialog dialag;
    private Uri imageUri;
    private FirebaseFirestore db;

    int order_year,update;
    StorageReference storageReference= FirebaseStorage.getInstance().getReference("players_profile");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_bio);

            //players_email_enter_dialop();
        db = FirebaseFirestore.getInstance();
        dialag = new ProgressDialog(this);
            circleImageView = findViewById(R.id.circle_image);
            player_name = findViewById(R.id.players_name);
            player_dob = findViewById(R.id.players_dob);
            player_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar=Calendar.getInstance();
                    int MONTH=calendar.get(Calendar.MONTH);
                    int YEAR=calendar.get(Calendar.YEAR);
                    int DAY=calendar.get(Calendar.DATE);
                    DatePickerDialog datePickerDialog=new DatePickerDialog(Players_Bio.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            i1=i1+1;
                            player_dob.setText(i2+"/"+i1+"/"+i);
                            order_year=i;



                        }
                    },YEAR,MONTH,DAY);
                    datePickerDialog.show();
                }

            });
/////////////////////////////////////back//////////////////////////////
back=findViewById(R.id.back_to_bio);
back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Bundle bundle=new Bundle();
        bundle.putString("email",emaild);
        bundle.putString("show_bio","show");
        Intent intent=new Intent(Players_Bio.this, Show_biodata.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
});
            player_age = findViewById(R.id.players_age);
            player_mobile = findViewById(R.id.player_mobile);
            player_position = findViewById(R.id.player_position);
            male=findViewById(R.id.male);
            female=findViewById(R.id.female);
            submit = findViewById(R.id.submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(male.isChecked()){
        male_or_female="Male";
                    }else if(female.isChecked()){
                        male_or_female="Female";
                    }else{
                        male_or_female=null;
                    }
String name=player_name.getText().toString();
String dob=player_dob.getText().toString();
String age=player_age.getText().toString();
String genter=male_or_female;
String mobile=player_mobile.getText().toString();
String position=player_position.getText().toString();
if(update==1)
    uplad_bio_data(imageUri,name,dob,age,genter,mobile,position,order_year);
else
    update_bio_data(name,dob,age,genter,mobile,position,B_id,order_year,emaild);
                }
            });
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFileChose1();

                }
            });
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null) {

            edit_name= bundle1.getString("B_name");
            edit_dob= bundle1.getString("B_dob");
            edit_age= bundle1.getString("B_age");
            edit_genter= bundle1.getString("B_genter");
            edit_mobile= bundle1.getString("B_mobile");
            edit_position= bundle1.getString("B_position");
            B_id= bundle1.getString("B_id");
            email1= bundle1.getString("email1");
            player_name.setText(edit_name);
            player_dob.setText(edit_dob);
            player_age.setText(edit_age);
            player_mobile.setText(edit_mobile);
            player_position.setText(edit_position);
            if(edit_name!=null){
                if(edit_genter.equals("Male"))
                  male.setChecked(true);
                else
                female.setChecked(true);
            }

        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            emaild= bundle.getString("email");
            update=bundle.getInt("update");
        }
        }


    public void openFileChose1(){
        Intent galleryIntent =new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, 2);


    }
    public void uplad_bio_data(Uri uri,String name,String dob,String age,String genter,String mobile,String position,int order){
        dialag.setTitle("Uploading information...!");
        if(imageUri==null && player_name.getText().toString().isEmpty()&&genter==null && player_dob.getText().toString().isEmpty() && player_age.getText().toString().isEmpty() && player_mobile.getText().toString().isEmpty() && player_position.getText().toString().isEmpty() && male_or_female.toString().isEmpty()){
            Toast.makeText(this, "Empty field is not allow!", Toast.LENGTH_SHORT).show();
        }else if (imageUri==null){
            Toast.makeText(this, "Please chose your picture!", Toast.LENGTH_SHORT).show();
        }else if (player_name.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
        }else if (player_dob.getText().toString().isEmpty()){
            Toast.makeText(this, "Please select your DOB!", Toast.LENGTH_SHORT).show();
        }else if (player_age.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter your age!", Toast.LENGTH_SHORT).show();
        }else if (player_mobile.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter your mobile number!", Toast.LENGTH_SHORT).show();
        }else if (player_position.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter your game position!", Toast.LENGTH_SHORT).show();
        }else if (male_or_female==null){
            Toast.makeText(this, "Please enter chose your genter!", Toast.LENGTH_SHORT).show();
        }
         else {
            dialag.show();
            StorageReference fileRef = storageReference.child(  System.currentTimeMillis()+ "." + getFileExtension(imageUri));
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String id= UUID.randomUUID().toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id",id);
                            map.put("email",emaild);
                            map.put("player_name",name);
                            map.put("player_age",age);
                            map.put("player_age_order",order);
                            map.put("player_dob",dob);
                            map.put("player_mobile",mobile);
                            map.put("player_position",position);
                            map.put("player_genter",genter);
                            map.put("Image_url",uri.toString());
                            db.collection("biodata").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    dialag.dismiss();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("email",emaild);
                                    bundle.putString("show_bio","show");
                                    Intent intent=new Intent(Players_Bio.this, Show_biodata.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    Toast.makeText(Players_Bio.this, "Data add successful!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialag.dismiss();
                                    Toast.makeText(Players_Bio.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    float a=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();


                    dialag.setMessage("Place wait!"+(int)a+"%");
                }
            });

        }
    }
    /////////////////////////////////////////////////update/////////////////////////////////////////////////////
    public void update_bio_data(String name111,String dob111,String age111,String genter111,String mobile111,String position111,String id,int order,String emaild) {
        dialag.setTitle("UPDATE!!!");
        if (player_name.getText().toString().isEmpty() && genter111 == null && player_dob.getText().toString().isEmpty() && player_age.getText().toString().isEmpty() && player_mobile.getText().toString().isEmpty() && player_position.getText().toString().isEmpty() && male_or_female.toString().isEmpty()) {
            Toast.makeText(this, "Empty field is not allow!", Toast.LENGTH_SHORT).show();
        } else if (player_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
        } else if (player_dob.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select your DOB!", Toast.LENGTH_SHORT).show();
        } else if (player_age.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your age!", Toast.LENGTH_SHORT).show();
        } else if (player_mobile.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your mobile number!", Toast.LENGTH_SHORT).show();
        } else if (player_position.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your game position!", Toast.LENGTH_SHORT).show();
        } else if (male_or_female == null) {
            Toast.makeText(this, "Please enter chose your genter!", Toast.LENGTH_SHORT).show();
        } else {
            dialag.show();
            db.collection("biodata").document(id).update("player_name", name111, "player_age", age111, "player_dob", dob111, "player_mobile", mobile111, "player_position", position111, "player_genter", genter111,"player_age_order",order).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Players_Bio.this, "Update Successfully!!!", Toast.LENGTH_SHORT).show();
                    dialag.dismiss();
                    Bundle bundle=new Bundle();
                    bundle.putString("email",email1);
                    bundle.putString("show_bio","show");
                    Intent intent=new Intent(Players_Bio.this, Show_biodata.class);
                    intent.putExtras(bundle);
                    startActivity(intent);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialag.dismiss();
                    Toast.makeText(Players_Bio.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data !=null){
            imageUri=data.getData();
            //imageView.setImageURI(imageUri);
            Picasso.with(this).load(imageUri).fit().into(circleImageView);

        }
    }
    private String getFileExtension(Uri muri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(muri));
    }

}