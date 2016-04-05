package com.comp3617.finalproject.meggsage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by meggz on 4/3/16.
 */
public class TextReminder {

    private long id;
    private long createDate;
    private String title;
    private String message;
    private String recipientName;
    private String recipientNumber;
    private long dueDate;
    private int sentStatus;
    private int active;

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getDueDate() {
        return dueDate;
    }

    public String getDueDateString()  {
        Date d = new Date(dueDate);
        SimpleDateFormat df = new SimpleDateFormat("EEE. MMM d, yyyy h:mm a");
        return df.format(d);
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public int getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(int sentStatus) {
        this.sentStatus = sentStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
