package com.example.myproject.Admain;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.Players.Players_Bio;
import com.example.myproject.Players.Show_biodata;
import com.example.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.zolad.zoominimageview.ZoomInImageView;

import java.util.List;

/////////////////////////////////////////////////////////players_bio_adapter///////////////////////////////////////////////////////
public class Players_boi_adapter extends RecyclerView.Adapter<Players_boi_adapter.Players_bio_viewholder> {
    private Show_biodata activity;
    private List<players_biodata> ilist;
    private FirebaseFirestore db;
    FirebaseStorage storageReference;
    public Players_boi_adapter(Show_biodata activity, List<players_biodata> bio_models) {
        this.activity = activity;
        this.ilist = bio_models;
    }


    @NonNull
    @Override
    public Players_bio_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.show_players_bio, parent, false);
        db = FirebaseFirestore.getInstance();
        return new Players_bio_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Players_bio_viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.player_name.setText(ilist.get(position).getPlayers_name());
        holder.player_dob.setText(ilist.get(position).getPlayers_dob());
        holder.player_age.setText(ilist.get(position).getPlayers_age());
        holder.player_genter.setText(ilist.get(position).getPlayers_genter());
        holder.player_mobile.setText(ilist.get(position).getPlayers_mobile());
        holder.player_position.setText(ilist.get(position).getPlayers_position());
        Picasso.with(activity).load(ilist.get(position).getProfile_image()).placeholder(R.drawable.sthc_pic).fit().into(holder.zoomInImageView);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String call_player=ilist.get(position).players_mobile;
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(call_player)));
                activity.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.player_name.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Delete this image...");
                builder.setIcon(R.drawable.ic_outline_report_problem);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("biodata").document(ilist.get(position).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    notifyItemRemoved(position);
                                    Toast.makeText(activity, "Delete successful.", Toast.LENGTH_SHORT).show();

                                }
                                storageReference= FirebaseStorage.getInstance();
                                StorageReference delectstorage=storageReference.getReferenceFromUrl(ilist.get(position).profile_image);
                                delectstorage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();

                                    }
                                });

                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("B_name",ilist.get(position).players_name);
                bundle.putString("B_dob",ilist.get(position).players_dob);
                bundle.putString("B_age",ilist.get(position).players_age);
                bundle.putString("B_genter",ilist.get(position).players_genter);
                bundle.putString("B_mobile",ilist.get(position).players_mobile);
                bundle.putString("B_position",ilist.get(position).players_position);
                bundle.putString("B_id",ilist.get(position).id);
                bundle.putString("email1",ilist.get(position).email);
                Intent intent=new Intent(activity, Players_Bio.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ilist.size();
    }

    public class Players_bio_viewholder extends RecyclerView.ViewHolder {
        ZoomInImageView zoomInImageView;
        TextView player_name, player_dob, player_age, player_genter, player_mobile, player_position, call;
        Button edit, delete;

        public Players_bio_viewholder(@NonNull View itemView) {
            super(itemView);
            zoomInImageView = itemView.findViewById(R.id.profile);
            player_name = itemView.findViewById(R.id.name_player);
            player_dob = itemView.findViewById(R.id.dob_players);
            player_age = itemView.findViewById(R.id.age_player);
            player_genter = itemView.findViewById(R.id.genter_players);
            player_mobile = itemView.findViewById(R.id.mobile_players);
            player_position = itemView.findViewById(R.id.position_players);
            call = itemView.findViewById(R.id.call_player);
            edit = itemView.findViewById(R.id.edit_bio);
            delete = itemView.findViewById(R.id.delete_bio);
        }
    }
}
class Players_boi_adapter1 extends RecyclerView.Adapter<Players_boi_adapter1.Players_bio_viewholder> {
    private Admin_show_players_bio activity;
    private List<players_biodata> ilist;
    private FirebaseFirestore db;
    FirebaseStorage storageReference;
    public Players_boi_adapter1(Admin_show_players_bio activity, List<players_biodata> bio_models) {
        this.activity = activity;
        this.ilist = bio_models;
    }


    @NonNull
    @Override
    public Players_bio_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.show_players_bio, parent, false);
        db = FirebaseFirestore.getInstance();
        return new Players_bio_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Players_bio_viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.player_name.setText(ilist.get(position).getPlayers_name());
        holder.player_dob.setText(ilist.get(position).getPlayers_dob());
        holder.player_age.setText(ilist.get(position).getPlayers_age());
        holder.player_genter.setText(ilist.get(position).getPlayers_genter());
        holder.player_mobile.setText(ilist.get(position).getPlayers_mobile());
        holder.player_position.setText(ilist.get(position).getPlayers_position());
        Picasso.with(activity).load(ilist.get(position).getProfile_image()).placeholder(R.drawable.sthc_pic).fit().into(holder.zoomInImageView);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String call_player=ilist.get(position).players_mobile;
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(call_player)));
                activity.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.player_name.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Delete this image...");
                builder.setIcon(R.drawable.ic_outline_report_problem);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("biodata").document(ilist.get(position).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    notifyItemRemoved(position);
                                    Toast.makeText(activity, "Delete successful.", Toast.LENGTH_SHORT).show();

                                }
                                storageReference= FirebaseStorage.getInstance();
                                StorageReference delectstorage=storageReference.getReferenceFromUrl(ilist.get(position).profile_image);
                                delectstorage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();

                                    }
                                });

                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("B_name",ilist.get(position).players_name);
                bundle.putString("B_dob",ilist.get(position).players_dob);
                bundle.putString("B_age",ilist.get(position).players_age);
                bundle.putString("B_genter",ilist.get(position).players_genter);
                bundle.putString("B_mobile",ilist.get(position).players_mobile);
                bundle.putString("B_position",ilist.get(position).players_position);
                bundle.putString("B_id",ilist.get(position).id);
                bundle.putString("email1",ilist.get(position).email);
                Intent intent=new Intent(activity, Players_Bio.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ilist.size();
    }

    public class Players_bio_viewholder extends RecyclerView.ViewHolder {
        ZoomInImageView zoomInImageView;
        TextView player_name, player_dob, player_age, player_genter, player_mobile, player_position, call;
        Button edit, delete;

        public Players_bio_viewholder(@NonNull View itemView) {
            super(itemView);
            zoomInImageView = itemView.findViewById(R.id.profile);
            player_name = itemView.findViewById(R.id.name_player);
            player_dob = itemView.findViewById(R.id.dob_players);
            player_age = itemView.findViewById(R.id.age_player);
            player_genter = itemView.findViewById(R.id.genter_players);
            player_mobile = itemView.findViewById(R.id.mobile_players);
            player_position = itemView.findViewById(R.id.position_players);
            call = itemView.findViewById(R.id.call_player);
            edit = itemView.findViewById(R.id.edit_bio);
            delete = itemView.findViewById(R.id.delete_bio);
        }
    }
}
