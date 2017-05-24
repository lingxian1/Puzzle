package com.example.administrator.puzzle.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Android shnu
 * Created by 140153815cyk on 2017/5/24.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "Puzzle";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "top";
    //创建数据库
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists " + TABLE_NAME
                + "(create_date VARCHAR,"
                + " use_time VARCHAR,"
                + " type VARCHAR)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    //添加数据
    public long insert(String date, String use_time, String type ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("create_date",date);
        values.put("use_time",use_time);
        values.put("type",type);
        long row = db.insert(TABLE_NAME, null, values);
        return row;
    }

    //根据条件查询
    public Cursor query(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE type like ? order by use_time", new String[]{type});
        return cursor;
    }
}
