package com.example.myproject.Admain;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

public class Tournament_related_images extends AppCompatActivity {
ImageView imageView;
EditText image_title,image_descripation;
Button upload_to_firebase;
TextView image_upload_page_title;
String match_title,match_id;
int subcount;
    private Uri imageUri;
    private FirebaseFirestore db;
    ProgressDialog dialag;
    StorageReference storageReference= FirebaseStorage.getInstance().getReference("TOURNAMENT_IMAGES");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_related_images);
        dialag=new ProgressDialog(this);

        db =FirebaseFirestore.getInstance();
        imageView=findViewById(R.id.chose_image_for_tournament);
        image_upload_page_title=findViewById(R.id.image_chose_page_title);
        image_title=findViewById(R.id.image_title_text);
        image_descripation=findViewById(R.id.image_description);
        upload_to_firebase=findViewById(R.id.image_upload_to_firebase);
        Bundle bundle=getIntent().getExtras();
        if (bundle != null) {
            match_id=bundle.getString("Mid");
            match_title=bundle.getString("Mtitle");
            subcount=bundle.getInt("sub_collection");
            image_upload_page_title.setText(match_title);
        }else{
            image_upload_page_title.setText("No title");
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
openFileChose1();
            }
        });
        upload_to_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_upload_to_firebase(imageUri);

            }
        });

    }
    public void openFileChose1(){
        Intent galleryIntent =new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(galleryIntent, 2);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && resultCode==RESULT_OK && data !=null){
            imageUri=data.getData();
            Picasso.with(this).load(imageUri).into(imageView);

        }
    }
    private String getFileExtension(Uri muri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(muri));
    }
    public void image_upload_to_firebase(Uri uri){
        dialag.setTitle("Upload Image...");
        if(imageUri==null && image_title.getText().toString().isEmpty()){
            Toast.makeText(this, "Empty field is not allow!", Toast.LENGTH_SHORT).show();
        }else if(image_title.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter some title!", Toast.LENGTH_SHORT).show();
        }else if(imageUri==null){
            Toast.makeText(this, "Please choose the image!", Toast.LENGTH_SHORT).show();
        }else{
            dialag.show();
            StorageReference fileRef = storageReference.child( image_title.getText().toString()+ "." + getFileExtension(imageUri));

            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String id= UUID.randomUUID().toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("Image_id",id);
                            map.put("Image_Title",image_title.getText().toString());
                            map.put("Image_description",image_descripation.getText().toString());
                            map.put("Image_url",uri.toString());
                            map.put("equaltitle",match_id);
                            db.collection("Tournament_related_images").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    dialag.dismiss();
                                    subcount++;
                                    Toast.makeText(Tournament_related_images.this, "Data add successful!", Toast.LENGTH_SHORT).show();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("Mtitle",match_title);
                                    bundle.putString("Mid",match_id);
                                    bundle.putInt("sub_collection",subcount);
                                    db.collection("tournament_data").document(match_id).update("about_match_collection",String.valueOf(subcount));
                                    Intent intent=new Intent(Tournament_related_images.this, Show_tournament_images.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Tournament_related_images.this, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
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
    @Override
    public void onBackPressed() {
        Bundle bundle=new Bundle();
        bundle.putString("Mtitle",match_title);
        bundle.putString("Mid",match_id);

        Intent intent=new Intent(Tournament_related_images.this, Show_tournament_images.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}