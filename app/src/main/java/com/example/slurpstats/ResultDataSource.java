package com.example.slurpstats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.slurpstats.Result;

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


    // Beispiel für addResult-Methode
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
        cursor.moveToFirst();
        Result newResult = cursorToResult(cursor);
        cursor.close();
        return newResult;
    }

    // Beispiel für getAnzahlErgebnisse-Methode
    public int getNumberOfResults() {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_RESULTS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
