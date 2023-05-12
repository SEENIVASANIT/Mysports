package com.example.myproject.Admain;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.zolad.zoominimageview.ZoomInImageView;

import java.util.List;
import java.util.Objects;

public class Adapter_class extends RecyclerView.Adapter<Adapter_class.Myviewholder>{
    private List<Model_class> mList;
    private Upload_matchs_admain activity;
    private FirebaseFirestore db;

    Bundle bundle=new Bundle();

    public Adapter_class(Upload_matchs_admain activity,List<Model_class>mList){
        this.activity=activity;
        this.mList=mList;
    }



    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(activity).inflate(R.layout.show_all_matchs_cardview,parent,false);
        db=FirebaseFirestore.getInstance();
        return new Myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.mtitle.setText(mList.get(position).getTitle());
        holder.mplace.setText(mList.get(position).getPlace());
        holder.mdate.setText(mList.get(position).getDate());
        holder.players_names.setText(mList.get(position).getAdd_players_name());
        holder.nothing_happen.setText(mList.get(position).getAll_the_best());
        holder.win_tournament.setText(mList.get(position).getWin_tournament());
        holder.not_win_tournament.setText(mList.get(position).getNot_win_tounament());
//////////////////////////////////////////////////delete_match////////////////////////////////////////////////////////////////////
        holder.delete_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.mtitle.getContext());
                int getsubcount= Integer.parseInt(mList.get(position).getSub_collection());
                int getsubcount2=Integer.parseInt(mList.get(position).getSub_collection2());
                if(getsubcount==0){
                    builder.setTitle("Are you sure!");
                    builder.setMessage("Delete this item....");
                    builder.setIcon(R.drawable.ic_outline_report_problem);
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            db.collection("tournament_data").document(mList.get(position).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(activity, "Data was deletead!!!", Toast.LENGTH_SHORT).show();
                                    activity.show_all_match_datas();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(activity, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Toast.makeText(activity, "cancle", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }else if(getsubcount2==0){
                    builder.setTitle("WARNING!");
                    String a=mList.get(position).getSub_collection2();
                    String b=mList.get(position).getTitle();
                    builder.setMessage("Please delete tournament image"+" "+"("+a+")"+" "+"items then delete"+" "+"("+b+")"+" "+" this item....");
                    builder.setIcon(R.drawable.ic_outline_report_problem);
                    builder.setPositiveButton("Go'it", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            bundle.putString("Mtitle",mList.get(position).getTitle());
                            bundle.putString("Mid",mList.get(position).getId());
                            bundle.putInt("sub_collection", Integer.parseInt(mList.get(position).getSub_collection()));
                            Intent intent=new Intent(activity, Show_tournament_images.class);
                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        }
                    }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Toast.makeText(activity, "cancle", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }else {
                    builder.setTitle("WARNING!");
                    String a=mList.get(position).getSub_collection();
                    String b=mList.get(position).getTitle();
                    builder.setMessage("Please delete about match"+" "+"("+a+")"+" "+"items then delete"+" "+"("+b+")"+" "+" this item....");
                    builder.setIcon(R.drawable.ic_outline_report_problem);
                    builder.setPositiveButton("Go'it", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            bundle.putString("Mtitle",mList.get(position).getTitle());
                            bundle.putString("Mid",mList.get(position).getId());
                            bundle.putInt("sub_collection", Integer.parseInt(mList.get(position).getSub_collection()));
                            Intent intent=new Intent(activity, Show_About_Tournament.class);
                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        }
                    }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Toast.makeText(activity, "cancle", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }

            }
        });
 //////////////////////////////////////////////////edit_match/////////////////////////////////////////////////////////////////////
holder.edit_match.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        bundle.putString("ETitle",mList.get(position).getTitle());
        bundle.putString("Eplace",mList.get(position).getPlace());
        bundle.putString("Edate",mList.get(position).getDate());
        bundle.putString("Eid",mList.get(position).getId());
        bundle.putString("Eplayers",mList.get(position).getAdd_players_name());
        Intent intent=new Intent(activity, Admain_inputs_page.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
});
/////////////////////////////////////////////////////////add players///////////////////////////////////////////////////////////////////////
       holder.add_palyers.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               bundle.putString("getid",mList.get(position).getId());
               bundle.putString("getplayers",mList.get(position).getAdd_players_name());
               Intent intent=new Intent(activity, Add_players_page.class);
               intent.putExtras(bundle);
               activity.startActivity(intent);
           }
       });
//////////////////////////////////////////////////uploadimage///////////////////////////////////////////////////////////////////////
        holder.upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle.putString("Mtitle",mList.get(position).getTitle());
                bundle.putString("Mid",mList.get(position).getId());
                bundle.putString("user",activity.check_user);
                bundle.putInt("sub_collection", Integer.parseInt(mList.get(position).getSub_collection()));
                Intent intent=new Intent(activity, Show_tournament_images.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
        holder.player_match_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle.putString("Mtitle",mList.get(position).getTitle());
                bundle.putString("Mid",mList.get(position).getId());
                bundle.putString("User",activity.check_user);
                bundle.putInt("sub_collection", Integer.parseInt(mList.get(position).getSub_collection()));
                Intent intent=new Intent(activity, Show_tournament_images.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
///////////////////////////////////////////////////////anout_tournament////////////////////////////////////////////////////////////////
holder.about_tournament.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        bundle.putString("Mtitle",mList.get(position).getTitle());
        bundle.putString("Mid",mList.get(position).getId());
        bundle.putString("User",activity.check_user);
        bundle.putInt("sub_collection", Integer.parseInt(mList.get(position).getSub_collection()));
        Intent intent=new Intent(activity, Show_About_Tournament.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
});
/////////////////////////////////animation_button////////////////////////////////////////////////////////////////////////////////////////
        holder.win_or_not_open= AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.win_forword);
        holder.win_or_not_close=AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.win_float_button_close);
        holder.win_or_not_forword=AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.win_forword);
        holder.win_or_not_backword=AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.win_backword);
        holder.win_or_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.isopen){
                    holder.win_or_not.startAnimation(holder.win_or_not_forword);
                    holder.win.startAnimation(holder.win_or_not_close);
                    holder.notwin.startAnimation(holder.win_or_not_close);
                    holder.nothing.startAnimation(holder.win_or_not_close);
                    holder.win.setClickable(false);
                    holder.notwin.setClickable(false);
                    holder.nothing.setClickable(false);
                    holder.isopen=false;
                }else { holder.win_or_not.startAnimation(holder.win_or_not_backword);
                    holder.win.startAnimation(holder.win_or_not_open);
                    holder.notwin.startAnimation(holder.win_or_not_open);
                    holder.nothing.startAnimation(holder.win_or_not_open);
                    holder.win.setClickable(true);
                    holder.notwin.setClickable(true);
                    holder.nothing.setClickable(true);
                    holder.isopen=true;

                }

            }
        });
holder.win.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View view) {
        db.collection("tournament_data").document(mList.get(position).getId()).update("tournament_status","");
        db.collection("tournament_data").document(mList.get(position).getId()).update("tournament_status_win","WIN THE TOURNAMENT👍");
        db.collection("tournament_data").document(mList.get(position).getId()).update("tournament_status_notwin","");
        if(holder.win.isClickable()) {
            holder.win_or_not.startAnimation(holder.win_or_not_forword);
            holder.win.startAnimation(holder.win_or_not_close);
            holder.notwin.startAnimation(holder.win_or_not_close);
            holder.nothing.startAnimation(holder.win_or_not_close);
            holder.win.setClickable(false);
            holder.notwin.setClickable(false);
            holder.nothing.setClickable(false);
            holder.isopen = false;
            activity.show_all_match_datas();
        }
    }
});
        holder.notwin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { ;
                db.collection("tournament_data").document(mList.get(position).getId()).update("tournament_status","");
                db.collection("tournament_data").document(mList.get(position).getId()).update("tournament_status_win","");
                db.collection("tournament_data").document(mList.get(position).getId()).update("tournament_status_notwin","LOSE THE TOURNAMENT👎");
                if(holder.notwin.isClickable()) {
                    holder.win_or_not.startAnimation(holder.win_or_not_forword);
                    holder.win.startAnimation(holder.win_or_not_close);
                    holder.notwin.startAnimation(holder.win_or_not_close);
                    holder.nothing.startAnimation(holder.win_or_not_close);
                    holder.win.setClickable(false);
                    holder.notwin.setClickable(false);
                    holder.nothing.setClickable(false);
                    holder.isopen = false;
                    activity.show_all_match_datas();
                }
            }
        });
        holder.nothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("tournament_data").document(mList.get(position).getId()).update("tournament_status","ALL THE BEST 👍");
                db.collection("tournament_data").document(mList.get(position).getId()).update("tournament_status_win","");
                db.collection("tournament_data").document(mList.get(position).getId()).update("tournament_status_notwin","");
                if(holder.notwin.isClickable()) {
                    holder.win_or_not.startAnimation(holder.win_or_not_forword);
                    holder.win.startAnimation(holder.win_or_not_close);
                    holder.notwin.startAnimation(holder.win_or_not_close);
                    holder.nothing.startAnimation(holder.win_or_not_close);
                    holder.win.setClickable(false);
                    holder.notwin.setClickable(false);
                    holder.nothing.setClickable(false);
                    holder.isopen = false;
                    activity.show_all_match_datas();
                }
                }

        });

    }////endonbindholder

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder{
        TextView mtitle,mplace,mdate,win_tournament,not_win_tournament,nothing_happen,players_names,about_tournament;
       Button edit_match,delete_match,upload_image,add_palyers,player_match_info;
        Animation win_or_not_open,win_or_not_close,win_or_not_forword,win_or_not_backword;
        FloatingActionButton win_or_not,win,notwin,nothing;
        boolean isopen=false;//dufult false
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            mtitle=itemView.findViewById(R.id.match_title_cardview);
            mplace=itemView.findViewById(R.id.match_place_cardview);
            mdate=itemView.findViewById(R.id.matchs_date_cardview);
            about_tournament=itemView.findViewById(R.id.about_tournament);
            players_names=itemView.findViewById(R.id.players_names);
            edit_match=itemView.findViewById(R.id.admain_edit_match);
            delete_match=itemView.findViewById(R.id.admain_delete_match);
            upload_image=itemView.findViewById(R.id.admain_upload_image);
            add_palyers=itemView.findViewById(R.id.admain_add_players);
            win_or_not=itemView.findViewById(R.id.win_or_not);
            win=itemView.findViewById(R.id.win);
            notwin=itemView.findViewById(R.id.notwin);
            nothing=itemView.findViewById(R.id.nothing1);
            win_tournament=itemView.findViewById(R.id.win_tournament);
            not_win_tournament=itemView.findViewById(R.id.not_win_tournament);
            nothing_happen=itemView.findViewById(R.id.nothing_happen);
            nothing_happen=itemView.findViewById(R.id.nothing_happen);
            player_match_info=itemView.findViewById(R.id.user_show_image);
           String match_info_image=activity.check_user;
           if(Objects.equals(match_info_image, "user")){
               edit_match.setVisibility(View.GONE);
               upload_image.setVisibility(View.GONE);
               delete_match.setVisibility(View.GONE);
               win_or_not.setVisibility(View.GONE);
               add_palyers.setVisibility(View.GONE);
               player_match_info.setVisibility(View.VISIBLE);
           }

        }
    }
}
///////////////////////////////////////////////////////////////////image_adapter///////////////////////////////////////////////////////////////////////////

class Image_Adapter extends RecyclerView.Adapter<Image_Adapter.ImageviewHolder>{
    private Show_tournament_images activity;
    private List<Image_model> ilist;
    private FirebaseFirestore db;
    FirebaseStorage storageReference;
    Bundle bundle=new Bundle();
    public Image_Adapter(Show_tournament_images activity,List<Image_model> image_models){
        this.activity=activity;
        this.ilist=image_models;
    }
    @NonNull
    @Override
    public ImageviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(activity).inflate(R.layout.tournament_image_cardview,parent,false);
        db=FirebaseFirestore.getInstance();
        return new ImageviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageviewHolder holder, @SuppressLint("RecyclerView") int position) {
holder.image_title.setText(ilist.get(position).getImage_title());
holder.image_description.setText(ilist.get(position).getImage_description());
        Picasso.with(activity).load(ilist.get(position).getImageuri()).fit().into(holder.imageView);
    ////////////////////////////////////////////////////////////download_image////////////////////////////////////////////////////////////
    holder.download.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String geturl = ilist.get(position).getImageuri();
            String getTitle = ilist.get(position).getImage_title();
            activity.getpermission(geturl,getTitle);
        }

    });
/////////////////////////////////////////////////////////////////////////////////belete_image////////////////////////////////////////////
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(holder.image_title.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Delete this image...");
                builder.setIcon(R.drawable.ic_outline_report_problem);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("Tournament_related_images").document(ilist.get(position).getImage_id()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    notifyItemRemoved(position);
                                    Toast.makeText(activity, "Delete successful.", Toast.LENGTH_SHORT).show();
                                    activity.getcount();
                                }
                                storageReference= FirebaseStorage.getInstance();
                                StorageReference delectstorage=storageReference.getReferenceFromUrl(ilist.get(position).imageuri);
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
    }


    @Override
    public int getItemCount() {
        return ilist.size();
    }

    public class ImageviewHolder extends RecyclerView.ViewHolder{
ZoomInImageView imageView;
TextView image_title,image_description;
Button download,delete;

        public ImageviewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imagess);
            image_title=itemView.findViewById(R.id.match_image_title);
            image_description=itemView.findViewById(R.id.match_image_description);
            download=itemView.findViewById(R.id.download_match_image);
            delete=itemView.findViewById(R.id.delete_match_image);
            if(activity.check_user.equals("user")){
                delete.setVisibility(View.GONE);
            }

        }
    }
}
/////////////////////////////////////////////////About_match///////////////////////////////////////////////////
class About_Adapter extends RecyclerView.Adapter<About_Adapter.AboutViewHolder>{
    private List<About_model> mList;
    private Show_About_Tournament activity;
    private FirebaseFirestore db;
    Bundle bundle=new Bundle();
    public About_Adapter(Show_About_Tournament activity,List<About_model>mList){
        this.activity=activity;
        this.mList=mList;

    }

    @NonNull
    @Override
    public AboutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(activity).inflate(R.layout.about_tournament_caedview,parent,false);
        db=FirebaseFirestore.getInstance();
        return new AboutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AboutViewHolder holder, @SuppressLint("RecyclerView") int position) {
holder.match_position.setText(mList.get(position).getPosition());
holder.competition.setText(mList.get(position).getCompetition());
holder.competition_date.setText(mList.get(position).getDate());
holder.match_status.setText(mList.get(position).getGoal());
holder.match_goal.setText(mList.get(position).getStatus());
holder.match_description.setText(mList.get(position).getDescription());

holder.about_delete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

       // db.collection("tournament_data").document(mList.get(position).getEqal_id()).update("sub_collection",String.valueOf(a));
        AlertDialog.Builder builder=new AlertDialog.Builder(holder.match_description.getContext());
        builder.setTitle("Are you sure!");
        builder.setMessage("Delete this item....");
        builder.setIcon(R.drawable.ic_outline_report_problem);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                db.collection("About_tournament_data").document(mList.get(position).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(activity, "Data was deletead!!!", Toast.LENGTH_SHORT).show();
                        activity.show_image_fierstore();

                        activity.getcount();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "Something woring,plase check your network and Tryagin!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(activity, "cancle", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
});
holder.about_edit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        bundle.putString("Eid1",mList.get(position).getId());
        bundle.putString("Ematch_position",mList.get(position).getPosition());
        bundle.putString("Ecompetition",mList.get(position).getCompetition());
        bundle.putString("Ecompetition_date",mList.get(position).getDate());
        bundle.putString("Ematch_goal",mList.get(position).getGoal());
        System.out.println(mList.get(position).getStatus());
        bundle.putString("Ematch_status",mList.get(position).getStatus());
        bundle.putString("Ematch_description",mList.get(position).getDescription());
        bundle.putString("equaltitle",mList.get(position).getEqal_id());
        Intent intent=new Intent(activity, About_tournament.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AboutViewHolder extends RecyclerView.ViewHolder{
TextView match_position,competition,competition_date,match_goal,match_status,match_description;
Button about_edit,about_delete;
        public AboutViewHolder(@NonNull View itemView) {
            super(itemView);
            match_position=itemView.findViewById(R.id.cardview_position);
            competition=itemView.findViewById(R.id.cardview_competition);
            competition_date=itemView.findViewById(R.id.cardview_date);
            match_goal=itemView.findViewById(R.id.cardview_golal);
            match_status=itemView.findViewById(R.id.cardview_status);
            match_description=itemView.findViewById(R.id.cardview_description);
            about_edit=itemView.findViewById(R.id.about_cardview_edit);
            about_delete=itemView.findViewById(R.id.about_cardview_delete);

            if(Objects.equals(activity.check_user, "user")){
                about_delete.setVisibility(View.GONE);
                about_edit.setVisibility(View.GONE);
            }
        }
    }

}
