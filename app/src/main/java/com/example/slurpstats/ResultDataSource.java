package com.example.slurpstats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ResultDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_GENDER,
            DatabaseHelper.COLUMN_WEIGHT,
            DatabaseHelper.COLUMN_BLOOD_ALCOHOL_CONTENT,
            DatabaseHelper.COLUMN_DATE,
            DatabaseHelper.COLUMN_TITLE
    };

    public ResultDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Öffnet die Datenbankverbindung
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // Schließt die Datenbankverbindung
    public void close() {
        dbHelper.close();
    }

    // Methode, um ein Ergebnis anhand seiner ID abzurufen
    public Result getResultById(long id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_RESULTS, allColumns,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Result result = cursorToResult(cursor);
            cursor.close();
            return result;
        } else {
            return null;
        }
    }

    // Methode, um alle Ergebnisse abzurufen
    public List<Result> getAllResults() {
        List<Result> results = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_RESULTS, allColumns,
                null, null, null, null, DatabaseHelper.COLUMN_DATE + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Result result = cursorToResult(cursor);
                results.add(result);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return results;
    }

    public void deleteResult(long id) {
        database.delete(DatabaseHelper.TABLE_RESULTS, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        database.delete(DatabaseHelper.TABLE_CONSUMPTION_DETAILS, DatabaseHelper.COLUMN_RESULT_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteAllResults() {
        database.delete(DatabaseHelper.TABLE_RESULTS, null, null);
        database.delete(DatabaseHelper.TABLE_CONSUMPTION_DETAILS, null, null);
    }

    // Hilfsmethode, um einen Cursor in ein Result-Objekt zu konvertieren
    private Result cursorToResult(Cursor cursor) {
        Result result = new Result();
        result.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
        result.setGender(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENDER)));
        result.setWeight(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WEIGHT)));
        result.setBloodAlcoholContent(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BLOOD_ALCOHOL_CONTENT)));
        result.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE)));
        result.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)));
        return result;
    }

    // Methode, um ein Ergebnis hinzuzufügen
    public Result addResult(String gender, double weight, double bloodAlcoholContent, String date, String title) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_GENDER, gender);
        values.put(DatabaseHelper.COLUMN_WEIGHT, weight);
        values.put(DatabaseHelper.COLUMN_BLOOD_ALCOHOL_CONTENT, bloodAlcoholContent);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_TITLE, title);

        long insertId = database.insert(DatabaseHelper.TABLE_RESULTS, null, values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_RESULTS, allColumns,
                DatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Result newResult = cursorToResult(cursor);
            cursor.close();
            return newResult;
        } else {
            return null;
        }
    }

    // Methode, um die Anzahl der Ergebnisse zu erhalten
    public int getNumberOfResults() {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_RESULTS, null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }
}