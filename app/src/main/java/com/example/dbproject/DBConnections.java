package com.example.dbproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnections extends SQLiteOpenHelper {

    public static final String DbName = "MyLibrary.db";
    public static final int version = 1;

    public DBConnections(Context contsxt) {
        super(contsxt, DbName, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS Books (ID INTEGER primary key NOT NULL, title TEXT NOT NULL, author TEXT NOT NULL, publication_date TEXT NOT NULL, publication_house TEXT NOT NULL, category TEXT, branch_id INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if EXISTS Books");
        onCreate(db);
    }
}
