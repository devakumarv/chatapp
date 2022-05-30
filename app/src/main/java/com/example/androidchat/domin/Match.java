package com.example.androidchat.domin;

public class Match {
    String name;
    String user_id;
    String pro_img_url;

    public Match(String name, String user_id, String pro_img_url) {
        this.name = name;
        this.user_id = user_id;
        this.pro_img_url = pro_img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPro_img_url() {
        return pro_img_url;
    }

    public void setPro_img_url(String pro_img_url) {
        this.pro_img_url = pro_img_url;
    }

}
