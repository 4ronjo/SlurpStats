package com.example.slurpstats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "slurpstats.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_GETRAENKE = "getraenke";
    private static final String COLUMN_GETRAENKE_ID = "getraenke_id";
    private static final String COLUMN_GETRAENKE_NAME = "name";
    private static final String COLUMN_GETRAENKE_ALKOHOLGEHALT = "alkoholgehalt";


    private static final String CREATE_TABLE_GETRAENKE = "CREATE TABLE " + TABLE_GETRAENKE + " ("
            + COLUMN_GETRAENKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_GETRAENKE_NAME + " TEXT NOT NULL, "
            + COLUMN_GETRAENKE_ALKOHOLGEHALT + " REAL NOT NULL);";


    private static final String TABLE_ERGEBNISSE = "ergebnisse";
    private static final String COLUMN_ERGEBNISSE_ID = "id";
    private static final String COLUMN_ERGEBNISSE_GESCHLECHT = "geschlecht";
    private static final String COLUMN_ERGEBNISSE_GEWICHT = "gewicht";
    private static final String COLUMN_ERGEBNISSE_BLUTALKOHOLWERT = "blutalkoholwert";


    private static final String CREATE_TABLE_ERGEBNISSE = "CREATE TABLE " + TABLE_ERGEBNISSE + " ("
            + COLUMN_ERGEBNISSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ERGEBNISSE_GESCHLECHT + " TEXT NOT NULL, "
            + COLUMN_ERGEBNISSE_GEWICHT + " REAL NOT NULL, "
            + COLUMN_ERGEBNISSE_BLUTALKOHOLWERT + " REAL NOT NULL);";


    private static final String TABLE_VERBRAUCHSDETAILS = "verbrauchsdetails";
    private static final String COLUMN_VERBRAUCHSDETAILS_ID = "id";
    private static final String COLUMN_VERBRAUCHSDETAILS_RESULT_ID = "result_id";
    private static final String COLUMN_VERBRAUCHSDETAILS_GETRAENKE_ID = "getraenke_id";
    private static final String COLUMN_VERBRAUCHSDETAILS_MENGE = "menge";


    private static final String CREATE_TABLE_VERBRAUCHSDETAILS = "CREATE TABLE " + TABLE_VERBRAUCHSDETAILS + " ("
            + COLUMN_VERBRAUCHSDETAILS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_VERBRAUCHSDETAILS_RESULT_ID + " INTEGER NOT NULL, "
            + COLUMN_VERBRAUCHSDETAILS_GETRAENKE_ID + " INTEGER NOT NULL, "
            + COLUMN_VERBRAUCHSDETAILS_MENGE + " REAL NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_VERBRAUCHSDETAILS_RESULT_ID + ") REFERENCES " + TABLE_ERGEBNISSE + "(" + COLUMN_ERGEBNISSE_ID + "), "
            + "FOREIGN KEY(" + COLUMN_VERBRAUCHSDETAILS_GETRAENKE_ID + ") REFERENCES " + TABLE_GETRAENKE + "(" + COLUMN_GETRAENKE_ID + "));";

    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GETRAENKE);
        db.execSQL(CREATE_TABLE_ERGEBNISSE);
        db.execSQL(CREATE_TABLE_VERBRAUCHSDETAILS);

        // Vordefinierte Getränke einfügen
        insertPredefinedDrinks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void insertPredefinedDrinks(SQLiteDatabase db) {
        insertDrink(db, "Bier", 5.0);
        insertDrink(db, "Vodka", 40.0);
        insertDrink(db, "Jack Daniels", 40.0);
        // Weitere Getränke hinzufügen
    }

    private void insertDrink(SQLiteDatabase db, String name, double alkoholgehalt) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_GETRAENKE_NAME, name);
        values.put(COLUMN_GETRAENKE_ALKOHOLGEHALT, alkoholgehalt);
        db.insert(TABLE_GETRAENKE, null, values);
    }

    public long addDrink(String name, double alkoholgehalt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GETRAENKE_NAME, name);
        values.put(COLUMN_GETRAENKE_ALKOHOLGEHALT, alkoholgehalt);
        long id = db.insert(TABLE_GETRAENKE, null, values);
        db.close();
        return id;
    }
    public List<Drink> getAllDrinks() {
        List<Drink> drinks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GETRAENKE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GETRAENKE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GETRAENKE_NAME));
                double alkoholgehalt = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GETRAENKE_ALKOHOLGEHALT));
                drinks.add(new Drink(id, name, alkoholgehalt));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return drinks;
    }

    public long addResult(String geschlecht, double gewicht, double blutalkoholwert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ERGEBNISSE_GESCHLECHT, geschlecht);
        values.put(COLUMN_ERGEBNISSE_GEWICHT, gewicht);
        values.put(COLUMN_ERGEBNISSE_BLUTALKOHOLWERT, blutalkoholwert);
        long id = db.insert(TABLE_ERGEBNISSE, null, values);
        db.close();
        return id;
    }

    public List<Result> getAllResults() {
        List<Result> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ERGEBNISSE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ERGEBNISSE_ID));
                String geschlecht = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ERGEBNISSE_GESCHLECHT));
                double gewicht = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ERGEBNISSE_GEWICHT));
                double blutalkoholwert = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ERGEBNISSE_BLUTALKOHOLWERT));
                results.add(new Result(id, geschlecht, gewicht, blutalkoholwert));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return results;
    }

    public void deleteResult(int resultId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ERGEBNISSE, COLUMN_ERGEBNISSE_ID + " = ?", new String[]{String.valueOf(resultId)});
        db.delete(TABLE_VERBRAUCHSDETAILS, COLUMN_VERBRAUCHSDETAILS_RESULT_ID + " = ?", new String[]{String.valueOf(resultId)});
        db.close();
    }

    public long addConsumptionDetail(int resultId, int getraenkeId, double menge) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VERBRAUCHSDETAILS_RESULT_ID, resultId);
        values.put(COLUMN_VERBRAUCHSDETAILS_GETRAENKE_ID, getraenkeId);
        values.put(COLUMN_VERBRAUCHSDETAILS_MENGE, menge);
        long id = db.insert(TABLE_VERBRAUCHSDETAILS, null, values);
        db.close();
        return id;
    }

    public List<ConsumptionDetail> getConsumptionDetailsByResultId(int resultId) {
        List<ConsumptionDetail> details = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_VERBRAUCHSDETAILS, null, COLUMN_VERBRAUCHSDETAILS_RESULT_ID + " = ?",
                new String[]{String.valueOf(resultId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VERBRAUCHSDETAILS_ID));
                int getraenkeId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VERBRAUCHSDETAILS_GETRAENKE_ID));
                double menge = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VERBRAUCHSDETAILS_MENGE));
                details.add(new ConsumptionDetail(id, resultId, getraenkeId, menge));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return details;
    }


}


