package com.example.androidchat.domin;

public class ImageItem {

    public String UserImgUrl, name, UserHobby;


    public ImageItem(String userImgUrl, String name, String userHobby) {
        this.UserImgUrl = userImgUrl;
        this.name = name;
        this.UserHobby = userHobby;
    }

    public String getUserImgUrl() {
        return UserImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        UserImgUrl = userImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserHobby() {
        return UserHobby;
    }

    public void setUserHobby(String userHobby) {
        UserHobby = userHobby;
    }

}
