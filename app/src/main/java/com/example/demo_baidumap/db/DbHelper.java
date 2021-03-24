package com.example.demo_baidumap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DbHelper(Context context){
        super(context,"forecast.db",null,1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
//      创建表的操作
        String sql = "create table info(_id integer primary key autoincrement,district_geocode varchar(7) unique not null,city varchar(20) unique not null)";
        String sql2 = "create table city(_id integer primary key autoincrement,city_name varchar(20) unique not null,city_id varchar(7) unique not null)";
        db.execSQL(sql2);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
