package com.example.myproject.Admain;

public class Model_class {
    String id,title,place,date,win_tournament,not_win_tounament,all_the_best,add_players_name,sub_collection,sub_collection2;
    public Model_class(){

    }
    public Model_class(String id,String sub_collection,String sub_collection2,String title,String place,String date,String all_the_best,String win_tournament,String not_win_tournament,String add_players_name){
        this.id=id;
        this.title=title;
        this.place=place;
        this.date=date;
        this.win_tournament=win_tournament;
        this.not_win_tounament=not_win_tournament;
        this.all_the_best=all_the_best;
        this.add_players_name=add_players_name;
        this.sub_collection=sub_collection;
        this.sub_collection2=sub_collection2;


    }

    public String getSub_collection2() {
        return sub_collection2;
    }

    public void setSub_collection2(String sub_collection2) {
        this.sub_collection2 = sub_collection2;
    }

    public String getSub_collection() {
        return sub_collection;
    }

    public void setSub_collection(String sub_collection) {
        this.sub_collection = sub_collection;
    }

    public String getAdd_players_name() {
        return add_players_name;
    }

    public void setAdd_players_name(String add_players_name) {
        this.add_players_name = add_players_name;
    }

    public String getAll_the_best() {
        return all_the_best;
    }

    public void setAll_the_best(String all_the_best) {
        this.all_the_best = all_the_best;
    }

    public String getNot_win_tounament() {
        return not_win_tounament;
    }

    public void setNot_win_tounament(String not_win_tounament) {
        this.not_win_tounament = not_win_tounament;
    }

    public String getWin_tournament() {
        return win_tournament;
    }

    public void setWin_tournament(String win_tournament) {
        this.win_tournament = win_tournament;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
class Image_model {
    String imageuri,image_title,image_id,image_description;
    public Image_model(){
    }
    public Image_model(String imageuri,String image_title,String image_description,String image_id){
        this.imageuri=imageuri;
        this.image_title=image_title;
        this.image_description=image_description;
        this.image_id=image_id;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String image_title) {
        this.image_title = image_title;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_description() {
        return image_description;
    }

    public void setImage_description(String image_description) {
        this.image_description = image_description;
    }
}
////////////////////////////////////////////////////AboutModel///////////////////////////////////////
class About_model{
    String position,competition,date,goal,status,description,id,eqal_id;
public About_model(){

}
public About_model(String eqal_id,String id,String position,String competition,String date,String status,String goal,String description){
    this.id=id;
    this.position=position;
    this.competition=competition;
    this.date=date;
    this.goal=goal;
    this.status=status;
    this.description=description;
    this.eqal_id=eqal_id;

}



    public String getEqal_id() {
        return eqal_id;
    }

    public void setEqal_id(String eqal_id) {
        this.eqal_id = eqal_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
