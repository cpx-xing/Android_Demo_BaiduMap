package com.example.demo_baidumap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManaget {
    public static SQLiteDatabase sqLiteDatabase;
//    初始化数据库
    public static void initDB(Context context){
        DbHelper mDBHelper = new DbHelper(context);
        sqLiteDatabase = mDBHelper.getWritableDatabase();
    }
}
