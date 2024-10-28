package com.example.slurpstats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.slurpstats.ConsumptionDetail;

import java.util.ArrayList;
import java.util.List;

public class ConsumptionDetailDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_RESULT_ID,
            DatabaseHelper.COLUMN_DRINK_ID,
            DatabaseHelper.COLUMN_AMOUNT
    };

    public ConsumptionDetailDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Open the database
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // Close the database
    public void close() {
        dbHelper.close();
    }

    // Add a new consumption detail
    public ConsumptionDetail addConsumptionDetail(long resultId, long drinkId, double amount) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_RESULT_ID, resultId);
        values.put(DatabaseHelper.COLUMN_DRINK_ID, drinkId);
        values.put(DatabaseHelper.COLUMN_AMOUNT, amount);

        long insertId = database.insert(DatabaseHelper.TABLE_CONSUMPTION_DETAILS, null, values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_CONSUMPTION_DETAILS, allColumns,
                DatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        ConsumptionDetail newDetail = cursorToConsumptionDetail(cursor);
        cursor.close();
        return newDetail;
    }

    // Get all consumption details for a specific result
    public List<ConsumptionDetail> getConsumptionDetailsByResultId(long resultId) {
        List<ConsumptionDetail> details = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_CONSUMPTION_DETAILS, allColumns,
                DatabaseHelper.COLUMN_RESULT_ID + " = ?", new String[]{String.valueOf(resultId)}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ConsumptionDetail detail = cursorToConsumptionDetail(cursor);
            details.add(detail);
            cursor.moveToNext();
        }
        cursor.close();
        return details;
    }

    // Helper method to convert cursor to ConsumptionDetail
    private ConsumptionDetail cursorToConsumptionDetail(Cursor cursor) {
        ConsumptionDetail detail = new ConsumptionDetail();
        detail.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
        detail.setResultId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESULT_ID)));
        detail.setDrinkId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DRINK_ID)));
        detail.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT)));
        return detail;
    }


}
