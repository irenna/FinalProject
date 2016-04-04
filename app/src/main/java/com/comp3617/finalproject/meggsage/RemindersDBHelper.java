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
    private static final int DB_VERSION = 1;

    private static final String TM_TABLE = "text_reminders";
    private static final String TM_COL_ID = BaseColumns._ID;
    private static final String TM_COL_CREATEDATE = "createDate";
    private static final String TM_COL_TITLE = "title";
    private static final String TM_COL_MESSAGE = "message";
    private static final String TM_COL_REC_NAME = "recipientName";
    private static final String TM_COL_REC_NUM = "recipientNumber";
    private static final String TM_COL_DUEDATE = "dueDate";
    private static final String TM_COL_SENT_STATUS = "sent";
    private static final String TM_COL_ACTIVE = "active";

    private static final String TM_CREATE_TABLE = "CREATE TABLE " + TM_TABLE + "(" + TM_COL_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + TM_COL_CREATEDATE + " INTEGER NOT NULL, "
            + TM_COL_TITLE + " TEXT NOT NULL, " + TM_COL_MESSAGE + " TEXT NOT NULL, " + TM_COL_REC_NAME + " TEXT, "
            + TM_COL_REC_NUM + " TEXT NOT NULL, " + TM_COL_DUEDATE + " INTEGER NOT NULL, " + TM_COL_SENT_STATUS + " INTEGER DEFAULT 0, "
            + TM_COL_ACTIVE + " INTEGER DEFAULT 1);";


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
        db.execSQL(TM_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }

    /** TEXT REMINDER METHODS  */

    public long createTextReminder(TextReminder tm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TM_COL_CREATEDATE, System.currentTimeMillis() / 1000);
        cv.put(TM_COL_TITLE, tm.getTitle());
        cv.put(TM_COL_MESSAGE, tm.getMessage());
        if(tm.getRecipientName().length() > 0) cv.put(TM_COL_REC_NAME, tm.getRecipientName());
        cv.put(TM_COL_REC_NUM, tm.getRecipientNumber());
        cv.put(TM_COL_DUEDATE, tm.getDueDate());

        // insert row
        long id = db.insert(TM_TABLE, null, cv);
        return id;
    }


    public TextReminder getTextReminder(long todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TM_TABLE + " WHERE " + TM_COL_ID + " = " + todo_id;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        TextReminder tm = new TextReminder();
        if(c != null) c.moveToFirst();

        tm.setId(c.getLong(c.getColumnIndex(TM_COL_ID)));
        tm.setCreateDate(c.getLong(c.getColumnIndex(TM_COL_CREATEDATE)));
        tm.setTitle(c.getString(c.getColumnIndex(TM_COL_TITLE)));
        tm.setMessage(c.getString(c.getColumnIndex(TM_COL_MESSAGE)));
        tm.setRecipientName(c.getString(c.getColumnIndex(TM_COL_REC_NAME)));
        tm.setRecipientNumber(c.getString(c.getColumnIndex(TM_COL_REC_NUM)));
        tm.setDueDate(c.getLong(c.getColumnIndex(TM_COL_DUEDATE)));
        tm.setSentStatus(c.getInt(c.getColumnIndex(TM_COL_SENT_STATUS)));
        tm.setActive(c.getInt(c.getColumnIndex(TM_COL_ACTIVE)));

        c.close();
        return tm;
    }

    public ArrayList<TextReminder> getAllTextReminders() {
        ArrayList<TextReminder> tms = new ArrayList<TextReminder>();
        String selectQuery = "SELECT  * FROM " + TM_TABLE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        c.moveToFirst();
        do {
            TextReminder tm = new TextReminder();
            tm.setId(c.getLong(c.getColumnIndex(TM_COL_ID)));
            tm.setCreateDate(c.getLong(c.getColumnIndex(TM_COL_CREATEDATE)));
            tm.setTitle(c.getString(c.getColumnIndex(TM_COL_TITLE)));
            tm.setMessage(c.getString(c.getColumnIndex(TM_COL_MESSAGE)));
            tm.setRecipientName(c.getString(c.getColumnIndex(TM_COL_REC_NAME)));
            tm.setRecipientNumber(c.getString(c.getColumnIndex(TM_COL_REC_NUM)));
            tm.setDueDate(c.getLong(c.getColumnIndex(TM_COL_DUEDATE)));
            tm.setSentStatus(c.getInt(c.getColumnIndex(TM_COL_SENT_STATUS)));
            tm.setActive(c.getInt(c.getColumnIndex(TM_COL_ACTIVE)));
            tms.add(tm);
        } while(c.moveToNext());
        c.close();
        return tms;
    }

    public int updateTextReminder(TextReminder tm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TM_COL_TITLE, tm.getTitle());
        cv.put(TM_COL_MESSAGE, tm.getMessage());
        String recName = (tm.getRecipientName().length() > 0) ? tm.getRecipientName() : null;
        cv.put(TM_COL_SENT_STATUS, tm.getSentStatus());
        cv.put(TM_COL_ACTIVE, tm.getActive());
        cv.put(TM_COL_REC_NAME, recName);
        cv.put(TM_COL_REC_NUM, tm.getRecipientNumber());
        cv.put(TM_COL_DUEDATE, tm.getDueDate());

        // updating row
        return db.update(TM_TABLE, cv, TM_COL_ID + " = " + tm.getId(), null);
    }

    public boolean deleteTextReminder(long tm_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TM_TABLE, TM_COL_ID + " = " + tm_id, null)  > 0;
    }

}
