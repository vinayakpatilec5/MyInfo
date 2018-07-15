package com.mygamelogic.myinfo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/07/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "myinfo_db";

    private static final String TABLE_NAME = "infoTable";
    private static final String SEARCH_TEXT = "searchText";
    private static final String GSON_DATA = "gsonData";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create notes table
        String query="CREATE TABLE infoTable(id  INTEGER PRIMARY KEY AUTOINCREMENT, searchText TEXT, gsonData TEXT)";
        db.execSQL(query);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    //---------------------------------
    public void insertNewData(String gsonData,String searchText) {
        // get writable database as we want to write data
        if((searchText!=null)&&(!searchText.isEmpty())&&(gsonData!=null)&&(!gsonData.isEmpty())) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SEARCH_TEXT, searchText);
            values.put(GSON_DATA, gsonData);
            // insert row
            long id = db.insert(TABLE_NAME, null, values);

            db.close();
        }
    }
    public void checkForDataAndAdd(String searchText,String gsonData ) {
            // get writable database as we want to write data
            if((searchText!=null)&&(!searchText.isEmpty())&&(gsonData!=null)&&(!gsonData.isEmpty())) {
                SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.query(TABLE_NAME,
                        new String[]{"id", SEARCH_TEXT, GSON_DATA},
                        SEARCH_TEXT + "=?",
                        new String[]{searchText}, null, null, null, null);
                int count = cursor.getCount();
                cursor.close();
                if (count == 0) {
                    insertNewData(gsonData, searchText);
                }
            }
    }
    public String getGsonData(String searchText) {
        // get writable database as we want to write data
        if((searchText!=null)&&(!searchText.isEmpty())) {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.query(TABLE_NAME,
                    new String[]{"id", SEARCH_TEXT, GSON_DATA},
                    SEARCH_TEXT + "=?",
                    new String[]{searchText}, null, null, null, null);
            int count = cursor.getCount();
            String gsonData = "";
            if (cursor.moveToFirst()) {
                gsonData = cursor.getString(cursor.getColumnIndex(GSON_DATA));
            }
            cursor.close();
            return gsonData;
        }
        return null;
    }
}
