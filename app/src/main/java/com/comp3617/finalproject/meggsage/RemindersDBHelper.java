package com.comp3617.finalproject.meggsage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by meggz on 4/3/16.
 */
public class RemindersDBHelper extends SQLiteOpenHelper {

    private static final String LOG = "DBHelper";
    private static final String DB_NAME = "reminders.db";
    private static final int DB_VERSION = 2;

    private static final String TR_TABLE = "text_reminders";
    private static final String TR_COL_ID = BaseColumns._ID;
    private static final String TR_COL_CREATEDATE = "createDate";
    private static final String TR_COL_TITLE = "title";
    private static final String TR_COL_MESSAGE = "message";
    private static final String TR_COL_REC_NAME = "recipientName";
    private static final String TR_COL_REC_NUM = "recipientNumber";
    private static final String TR_COL_DUEDATE = "dueDate";
    private static final String TR_COL_SENT_STATUS = "sent";
    private static final String TR_COL_ACTIVE = "active";

    private static final String TR_CREATE_TABLE = "CREATE TABLE " + TR_TABLE + "(" + TR_COL_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + TR_COL_CREATEDATE + " INTEGER NOT NULL, "
            + TR_COL_TITLE + " TEXT NOT NULL, " + TR_COL_MESSAGE + " TEXT NOT NULL, " + TR_COL_REC_NAME + " TEXT, "
            + TR_COL_REC_NUM + " TEXT NOT NULL, " + TR_COL_DUEDATE + " INTEGER NOT NULL, " + TR_COL_SENT_STATUS + " INTEGER DEFAULT 0, "
            + TR_COL_ACTIVE + " INTEGER DEFAULT 1);";


    private static final String NR_TABLE = "notification_reminders";
    private static final String NR_COL_ID = BaseColumns._ID;
    private static final String NR_COL_CREATEDATE = "createDate";
    private static final String NR_COL_TITLE = "title";
    private static final String NR_COL_MESSAGE = "message";
    private static final String NR_COL_IMPORTANCE = "importance";
    private static final String NR_COL_RECURRENCE = "recurrence";
    private static final String NR_COL_DUEDATE = "dueDate";
    private static final String NR_COL_ACTIVE = "active";
    private static final String NR_COL_COLOUR = "colour";


    private static final String NR_CREATE_TABLE = "CREATE TABLE " + NR_TABLE + "(" + NR_COL_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + NR_COL_CREATEDATE + " INTEGER NOT NULL, "
            + NR_COL_TITLE + " TEXT NOT NULL, " + NR_COL_MESSAGE + " TEXT NOT NULL, " + NR_COL_IMPORTANCE + " INTEGER DEFAULT 0, "
            + NR_COL_RECURRENCE + " INTEGER DEFAULT 0, " + NR_COL_DUEDATE + " INTEGER NOT NULL, "
            + NR_COL_ACTIVE + " INTEGER DEFAULT 1, " + NR_COL_COLOUR + " INT);";


    private static RemindersDBHelper INSTANCE;

    public static RemindersDBHelper getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new RemindersDBHelper(context.getApplicationContext());
        }
        return INSTANCE;
    }


    private RemindersDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TR_CREATE_TABLE);
        db.execSQL(NR_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NR_TABLE);
        onCreate(db);
    }

    /** TEXT REMINDER METHODS  */

    public long createTextReminder(TextReminder tm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TR_COL_CREATEDATE, System.currentTimeMillis() / 1000);
        cv.put(TR_COL_TITLE, tm.getTitle());
        cv.put(TR_COL_MESSAGE, tm.getMessage());
        if(tm.getRecipientName() != null) cv.put(TR_COL_REC_NAME, tm.getRecipientName());
        cv.put(TR_COL_REC_NUM, tm.getRecipientNumber());
        cv.put(TR_COL_DUEDATE, tm.getDueDate());

        // insert row
        long id = db.insert(TR_TABLE, null, cv);
        return id;
    }


    public TextReminder getTextReminder(long tr_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TR_TABLE + " WHERE " + TR_COL_ID + " = " + tr_id;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        TextReminder tm = new TextReminder();
        if(c != null) c.moveToFirst();

        tm.setId(c.getLong(c.getColumnIndex(TR_COL_ID)));
        tm.setCreateDate(c.getLong(c.getColumnIndex(TR_COL_CREATEDATE)));
        tm.setTitle(c.getString(c.getColumnIndex(TR_COL_TITLE)));
        tm.setMessage(c.getString(c.getColumnIndex(TR_COL_MESSAGE)));
        tm.setRecipientName(c.getString(c.getColumnIndex(TR_COL_REC_NAME)));
        tm.setRecipientNumber(c.getString(c.getColumnIndex(TR_COL_REC_NUM)));
        tm.setDueDate(c.getLong(c.getColumnIndex(TR_COL_DUEDATE)));
        tm.setSentStatus(c.getInt(c.getColumnIndex(TR_COL_SENT_STATUS)));
        tm.setActive(c.getInt(c.getColumnIndex(TR_COL_ACTIVE)));

        c.close();
        return tm;
    }

    public ArrayList<TextReminder> getAllTextReminders() {
        ArrayList<TextReminder> tms = new ArrayList<TextReminder>();
        String selectQuery = "SELECT  * FROM " + TR_TABLE;


        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            c.moveToFirst();
            do {
                TextReminder tm = new TextReminder();
                tm.setId(c.getLong(c.getColumnIndex(TR_COL_ID)));
                tm.setCreateDate(c.getLong(c.getColumnIndex(TR_COL_CREATEDATE)));
                tm.setTitle(c.getString(c.getColumnIndex(TR_COL_TITLE)));
                tm.setMessage(c.getString(c.getColumnIndex(TR_COL_MESSAGE)));
                tm.setRecipientName(c.getString(c.getColumnIndex(TR_COL_REC_NAME)));
                tm.setRecipientNumber(c.getString(c.getColumnIndex(TR_COL_REC_NUM)));
                tm.setDueDate(c.getLong(c.getColumnIndex(TR_COL_DUEDATE)));
                tm.setSentStatus(c.getInt(c.getColumnIndex(TR_COL_SENT_STATUS)));
                tm.setActive(c.getInt(c.getColumnIndex(TR_COL_ACTIVE)));
                tms.add(tm);
            } while(c.moveToNext());
        }

        c.close();
        return tms;
    }

    public ArrayList<TextReminder> getActiveTextReminders() {
        ArrayList<TextReminder> tms = new ArrayList<TextReminder>();
        String selectQuery = "SELECT  * FROM " + TR_TABLE + " WHERE " + TR_COL_ACTIVE + " = " + 1;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            c.moveToFirst();
            do {
                TextReminder tm = new TextReminder();
                tm.setId(c.getLong(c.getColumnIndex(TR_COL_ID)));
                tm.setCreateDate(c.getLong(c.getColumnIndex(TR_COL_CREATEDATE)));
                tm.setTitle(c.getString(c.getColumnIndex(TR_COL_TITLE)));
                tm.setMessage(c.getString(c.getColumnIndex(TR_COL_MESSAGE)));
                tm.setRecipientName(c.getString(c.getColumnIndex(TR_COL_REC_NAME)));
                tm.setRecipientNumber(c.getString(c.getColumnIndex(TR_COL_REC_NUM)));
                tm.setDueDate(c.getLong(c.getColumnIndex(TR_COL_DUEDATE)));
                tm.setSentStatus(c.getInt(c.getColumnIndex(TR_COL_SENT_STATUS)));
                tm.setActive(c.getInt(c.getColumnIndex(TR_COL_ACTIVE)));
                tms.add(tm);
            } while(c.moveToNext());
        }

        c.close();
        return tms;
    }

    public boolean sendTextReminder(long tm_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TR_COL_SENT_STATUS, 1);
        cv.put(TR_COL_ACTIVE, 0);

        // updating row
        return db.update(TR_TABLE, cv, TR_COL_ID + " = " + tm_id, null) > 0;
    }

    public boolean failTextReminder(long tm_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TR_COL_SENT_STATUS, 2);
        cv.put(TR_COL_ACTIVE, 0);

        // updating row
        return db.update(TR_TABLE, cv, TR_COL_ID + " = " + tm_id, null) > 0;
    }

    public int updateTextReminder(TextReminder tm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TR_COL_TITLE, tm.getTitle());
        cv.put(TR_COL_MESSAGE, tm.getMessage());
        String recName = tm.getRecipientName();
        cv.put(TR_COL_SENT_STATUS, tm.getSentStatus());
        cv.put(TR_COL_ACTIVE, tm.getActive());
        cv.put(TR_COL_REC_NAME, recName);
        cv.put(TR_COL_REC_NUM, tm.getRecipientNumber());
        cv.put(TR_COL_DUEDATE, tm.getDueDate());

        // updating row
        return db.update(TR_TABLE, cv, TR_COL_ID + " = " + tm.getId(), null);
    }

    public boolean deleteTextReminder(long tm_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TR_TABLE, TR_COL_ID + " = " + tm_id, null)  > 0;
    }

    /** NOTIFICATION REMINDER METHODS  */

    public long createNotificationReminder(NotificationReminder nr) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NR_COL_CREATEDATE, System.currentTimeMillis() / 1000);
        cv.put(NR_COL_TITLE, nr.getTitle());
        cv.put(NR_COL_MESSAGE, nr.getMessage());
        cv.put(NR_COL_IMPORTANCE, nr.getImportance());
        cv.put(NR_COL_RECURRENCE, nr.getRecurrence());
        cv.put(NR_COL_DUEDATE, nr.getDueDate());
        cv.put(NR_COL_ACTIVE, nr.getActive());
        if(nr.getColour() != null) cv.put(NR_COL_COLOUR, nr.getColour());

        // insert row
        long id = db.insert(NR_TABLE, null, cv);
        return id;
    }


    public NotificationReminder getNotificationReminder(long nr_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + NR_TABLE + " WHERE " + NR_COL_ID + " = " + nr_id;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        NotificationReminder nr = new NotificationReminder();
        if(c != null) c.moveToFirst();

        nr.setId(c.getLong(c.getColumnIndex(NR_COL_ID)));
        nr.setCreateDate(c.getLong(c.getColumnIndex(NR_COL_CREATEDATE)));
        nr.setTitle(c.getString(c.getColumnIndex(NR_COL_TITLE)));
        nr.setMessage(c.getString(c.getColumnIndex(NR_COL_MESSAGE)));
        nr.setRecurrence(c.getInt(c.getColumnIndex(NR_COL_RECURRENCE)));
        nr.setImportance(c.getInt(c.getColumnIndex(NR_COL_IMPORTANCE)));
        nr.setDueDate(c.getLong(c.getColumnIndex(NR_COL_DUEDATE)));
        if(!c.isNull(c.getColumnIndex(NR_COL_COLOUR)))nr.setColour(c.getInt(c.getColumnIndex(NR_COL_COLOUR)));
        nr.setActive(c.getInt(c.getColumnIndex(NR_COL_ACTIVE)));

        c.close();
        return nr;
    }

    public ArrayList<NotificationReminder> getAllNotificationReminders() {
        ArrayList<NotificationReminder> nrs = new ArrayList<NotificationReminder>();
        String selectQuery = "SELECT  * FROM " + NR_TABLE;


        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            c.moveToFirst();
            do {
                NotificationReminder nr = new NotificationReminder();
                nr.setId(c.getLong(c.getColumnIndex(NR_COL_ID)));
                nr.setCreateDate(c.getLong(c.getColumnIndex(NR_COL_CREATEDATE)));
                nr.setTitle(c.getString(c.getColumnIndex(NR_COL_TITLE)));
                nr.setMessage(c.getString(c.getColumnIndex(NR_COL_MESSAGE)));
                nr.setRecurrence(c.getInt(c.getColumnIndex(NR_COL_RECURRENCE)));
                nr.setImportance(c.getInt(c.getColumnIndex(NR_COL_IMPORTANCE)));
                nr.setDueDate(c.getLong(c.getColumnIndex(NR_COL_DUEDATE)));
                if(!c.isNull(c.getColumnIndex(NR_COL_COLOUR)))nr.setColour(c.getInt(c.getColumnIndex(NR_COL_COLOUR)));
                nr.setActive(c.getInt(c.getColumnIndex(NR_COL_ACTIVE)));
                nrs.add(nr);
            } while(c.moveToNext());
        }

        c.close();
        return nrs;
    }

    public ArrayList<NotificationReminder> getActiveNotificationReminders() {
        ArrayList<NotificationReminder> nrs = new ArrayList<NotificationReminder>();
        String selectQuery = "SELECT  * FROM " + NR_TABLE + " WHERE " + NR_COL_ACTIVE + " = " + 1;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            c.moveToFirst();
            do {
                NotificationReminder nr = new NotificationReminder();
                nr.setId(c.getLong(c.getColumnIndex(NR_COL_ID)));
                nr.setCreateDate(c.getLong(c.getColumnIndex(NR_COL_CREATEDATE)));
                nr.setTitle(c.getString(c.getColumnIndex(NR_COL_TITLE)));
                nr.setMessage(c.getString(c.getColumnIndex(NR_COL_MESSAGE)));
                nr.setRecurrence(c.getInt(c.getColumnIndex(NR_COL_RECURRENCE)));
                nr.setImportance(c.getInt(c.getColumnIndex(NR_COL_IMPORTANCE)));
                nr.setDueDate(c.getLong(c.getColumnIndex(NR_COL_DUEDATE)));
                if(!c.isNull(c.getColumnIndex(NR_COL_COLOUR))) nr.setColour(c.getInt(c.getColumnIndex(NR_COL_COLOUR)));
                nr.setActive(c.getInt(c.getColumnIndex(NR_COL_ACTIVE)));
                nrs.add(nr);
            } while(c.moveToNext());
        }

        c.close();
        return nrs;
    }

    public boolean sendNotificationReminder(long nr_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NR_COL_ACTIVE, 0);

        // updating row
        return db.update(NR_TABLE, cv, NR_COL_ID + " = " + nr_id, null) > 0;
    }

    public int updateNotificationReminder(NotificationReminder nr) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NR_COL_TITLE, nr.getTitle());
        cv.put(NR_COL_MESSAGE, nr.getMessage());
        cv.put(NR_COL_COLOUR, nr.getColour());
        cv.put(NR_COL_ACTIVE, nr.getActive());
        cv.put(NR_COL_RECURRENCE, nr.getRecurrence());
        cv.put(NR_COL_IMPORTANCE, nr.getImportance());
        cv.put(NR_COL_DUEDATE, nr.getDueDate());

        // updating row
        return db.update(NR_TABLE, cv, NR_COL_ID + " = " + nr.getId(), null);
    }

    public boolean deleteNotificationReminder(long nr_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NR_TABLE, NR_COL_ID + " = " + nr_id, null)  > 0;
    }
    

}
