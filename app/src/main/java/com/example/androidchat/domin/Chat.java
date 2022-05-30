package com.example.androidchat.domin;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Chat {

    String from;
    String to;
    String message;
    Date time_stamp;

    public Chat() {
    }

    public Chat(String from, String to, String message, Date time_stamp) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.time_stamp = time_stamp;
    }


    public Date getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
