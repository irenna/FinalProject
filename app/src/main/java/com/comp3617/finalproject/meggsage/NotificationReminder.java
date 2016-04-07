package com.comp3617.finalproject.meggsage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by meggz on 4/6/16.
 */
public class NotificationReminder {

    private long id;
    private long createDate;
    private String title;
    private String message;
    private Integer colour;
    private int recurrence;
    private long dueDate;
    private int active;
    private int importance;

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

    public Integer getColour() {
        return colour;
    }

    public void setColour(Integer colour) {
        this.colour = colour;
    }

    public int getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(int recurrence) {
        this.recurrence = recurrence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

}
