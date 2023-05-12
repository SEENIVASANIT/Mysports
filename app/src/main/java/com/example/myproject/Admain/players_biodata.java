package com.example.myproject.Admain;

//////////////////////////////////////players_boidata////////////////////////////////////////////////////////////
public class players_biodata {
    String players_name, players_dob, players_age, players_mobile, players_genter, players_position, profile_image, id,email;

    public players_biodata() {

    }

    public players_biodata(String profile_image, String players_name, String players_dob, String players_age, String players_genter, String players_mobile, String players_position,String id,String email) {
        this.players_name = players_name;
        this.players_dob = players_dob;
        this.players_age = players_age;
        this.players_genter = players_genter;
        this.players_mobile = players_mobile;
        this.players_position = players_position;
        this.profile_image = profile_image;
        this.email=email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayers_name() {
        return players_name;
    }

    public void setPlayers_name(String players_name) {
        this.players_name = players_name;
    }

    public String getPlayers_dob() {
        return players_dob;
    }

    public void setPlayers_dob(String players_dob) {
        this.players_dob = players_dob;
    }

    public String getPlayers_age() {
        return players_age;
    }

    public void setPlayers_age(String players_age) {
        this.players_age = players_age;
    }

    public String getPlayers_mobile() {
        return players_mobile;
    }

    public void setPlayers_mobile(String players_mobile) {
        this.players_mobile = players_mobile;
    }

    public String getPlayers_genter() {
        return players_genter;
    }

    public void setPlayers_genter(String players_genter) {
        this.players_genter = players_genter;
    }

    public String getPlayers_position() {
        return players_position;
    }

    public void setPlayers_position(String players_position) {
        this.players_position = players_position;
    }


}
