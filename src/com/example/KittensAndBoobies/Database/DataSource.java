package com.example.KittensAndBoobies.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by benoc on 28/04/2014.
 */
public class DataSource {
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = { Table.COLUMN_ID, Table.COLUMN_SCORE, Table.COLUMN_START, Table.COLUMN_DURATION };
    public DataSource(Context context) {
        dbHelper = new DBHelper(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }

    public Record createRecord(long aScore, long aStart, long aDuration) {
        ContentValues values = new ContentValues();
        values.put(Table.COLUMN_SCORE, aScore);
        values.put(Table.COLUMN_START, aStart);
        values.put(Table.COLUMN_DURATION, aDuration);
        long insertId = database.insert(Table.TABLE_SCORES, null, values);
        Cursor cursor = database.query(Table.TABLE_SCORES,
                allColumns, Table.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Record newRecord = cursorToRecord(cursor);
        cursor.close();
        return newRecord;
    }
    private Record cursorToRecord(Cursor cursor) {
        Record record = new Record();
        record.setId(cursor.getLong(0));
        record.setScore(cursor.getLong(1));
        record.setStart_time(cursor.getLong(2));
        record.setDuration(cursor.getLong(3));
        return record;
    }

    public void deleteRecord(Record record) {
        long id = record.getId();
        database.delete(Table.TABLE_SCORES, Table.COLUMN_ID + " = " + id, null);
    }

    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<Record>();
        Cursor cursor = database.query(Table.TABLE_SCORES, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Record record = cursorToRecord(cursor);
            records.add(record);
            cursor.moveToNext();
        }
        cursor.close();
        Collections.sort(records);
        Collections.reverse(records);
        return records;
    }
}
