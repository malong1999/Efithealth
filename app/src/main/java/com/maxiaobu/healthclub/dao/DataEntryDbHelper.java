package com.maxiaobu.healthclub.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 马小布 on 2016/9/22.
 * 数据录入数据库
 */
public class DataEntryDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "DataEntry.db";
    public static final String ITEMID = "itemid";
    public static final String GROUPS = "groups";
    public static final String STRENGTH = "strength";
    public static final String TIMES = "times";
    public String dataBaseName;

    public DataEntryDbHelper(Context context, String corderlessonid) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dataBaseName = corderlessonid;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        Log.d("DataEntryDbHelper", dataBaseName);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + dataBaseName +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT,itemid TEXT,groups INTEGER" +
                ",strength TEXT,times TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
