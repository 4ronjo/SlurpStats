package com.example.slurpstats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "slurpstats.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_DRINKS = "Drinks";
    public static final String TABLE_RESULTS = "Results";
    public static final String TABLE_CONSUMPTION_DETAILS = "ConsumptionDetails";

    public static final String COLUMN_ID = "ID";

    public static final String COLUMN_DRINK_ID = "DrinkID";
    public static final String COLUMN_DRINK_NAME = "DrinkName";
    public static final String COLUMN_ALCOHOL_CONTENT = "AlcoholContent";

    public static final String COLUMN_GENDER = "Gender";
    public static final String COLUMN_WEIGHT = "Weight";
    public static final String COLUMN_BLOOD_ALCOHOL_CONTENT = "BloodAlcoholContent";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_TITLE = "Title";

    public static final String COLUMN_RESULT_ID = "ResultID";
    public static final String COLUMN_AMOUNT = "Amount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_DRINKS = "CREATE TABLE " + TABLE_DRINKS + "("
                + COLUMN_DRINK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DRINK_NAME + " TEXT, "
                + COLUMN_ALCOHOL_CONTENT + " REAL"
                + ")";
        db.execSQL(CREATE_TABLE_DRINKS);

        db.execSQL("PRAGMA foreign_keys=ON;");

        String CREATE_TABLE_RESULTS = "CREATE TABLE " + TABLE_RESULTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_WEIGHT + " REAL, "
                + COLUMN_BLOOD_ALCOHOL_CONTENT + " REAL, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_TITLE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_RESULTS);

        String CREATE_TABLE_CONSUMPTION_DETAILS = "CREATE TABLE " + TABLE_CONSUMPTION_DETAILS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RESULT_ID + " INTEGER, "
                + COLUMN_DRINK_ID + " INTEGER, "
                + COLUMN_AMOUNT + " REAL, "
                + "FOREIGN KEY(" + COLUMN_RESULT_ID + ") REFERENCES " + TABLE_RESULTS + "(" + COLUMN_ID + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + COLUMN_DRINK_ID + ") REFERENCES " + TABLE_DRINKS + "(" + COLUMN_DRINK_ID + ")"
                + ")";
        db.execSQL(CREATE_TABLE_CONSUMPTION_DETAILS);

        insertPredefinedDrinks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSUMPTION_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRINKS);
        onCreate(db);
    }

    private void insertPredefinedDrinks(SQLiteDatabase db) {
        String insertVodka = "INSERT INTO " + TABLE_DRINKS + "("
                + COLUMN_DRINK_NAME + ", " + COLUMN_ALCOHOL_CONTENT + ") VALUES ('Vodka', 40)";
        db.execSQL(insertVodka);

        String insertBeer = "INSERT INTO " + TABLE_DRINKS + "("
                + COLUMN_DRINK_NAME + ", " + COLUMN_ALCOHOL_CONTENT + ") VALUES ('Beer', 5)";
        db.execSQL(insertBeer);

        String insertJackDaniels = "INSERT INTO " + TABLE_DRINKS + "("
                + COLUMN_DRINK_NAME + ", " + COLUMN_ALCOHOL_CONTENT + ") VALUES ('Jack Daniels', 40)";
        db.execSQL(insertJackDaniels);

    }
}


