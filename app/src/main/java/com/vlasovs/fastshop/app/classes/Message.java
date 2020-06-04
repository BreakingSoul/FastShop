package com.vlasovs.fastshop.app.classes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message {
    private int userID;
    private short toSupport;
    private String message;
    private String sendDate;

    public Message(int userID, short toSupport, String message, String sendDate) {
        this.userID = userID;
        this.toSupport = toSupport;
        this.message = message;
        this.sendDate = sendDate;
    }

    public Message(int userID, short toSupport, String message) {
        this.userID = userID;
        this.toSupport = toSupport;
        this.message = message;
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.sendDate = fmt.format(currentTime);
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public short getToSupport() {
        return toSupport;
    }

    public void setToSupport(short toSupport) {
        this.toSupport = toSupport;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
