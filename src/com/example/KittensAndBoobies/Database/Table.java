package com.example.KittensAndBoobies.Database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by benoc on 28/04/2014.
 */
public class Table {
    public static final String TABLE_SCORES = "score";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_START = "start_time";
    public static final String COLUMN_DURATION = "duration";

    private static final String DATABASE_CREATE = "create table " + TABLE_SCORES
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SCORE + " integer not null, " + COLUMN_START
            + " integer not null, " + COLUMN_DURATION + " integer not null" + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(Table.class.getName(), "Eredeti verzió: " + oldVersion + " új verzió" + newVersion);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(database);
    }
}
