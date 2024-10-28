package com.example.slurpstats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    private static final String DATABASE_NAME = "slurpstats.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_DRINKS = "Drinks";
    public static final String TABLE_RESULTS = "Results";
    public static final String TABLE_CONSUMPTION_DETAILS = "ConsumptionDetails";

    // Common Column Names
    public static final String COLUMN_ID = "ID";

    // Drinks Table Columns
    public static final String COLUMN_DRINK_ID = "DrinkID";
    public static final String COLUMN_DRINK_NAME = "DrinkName";
    public static final String COLUMN_ALCOHOL_CONTENT = "AlcoholContent";

    // Results Table Columns
    // Reusing COLUMN_ID for Result ID
    public static final String COLUMN_GENDER = "Gender";
    public static final String COLUMN_WEIGHT = "Weight";
    public static final String COLUMN_BLOOD_ALCOHOL_CONTENT = "BloodAlcoholContent";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_TITLE = "Title";

    // ConsumptionDetails Table Columns
    // Reusing COLUMN_ID for ConsumptionDetail ID
    public static final String COLUMN_RESULT_ID = "ResultID";
    // Reusing COLUMN_DRINK_ID
    public static final String COLUMN_AMOUNT = "Amount";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Drinks Table
        String CREATE_TABLE_DRINKS = "CREATE TABLE " + TABLE_DRINKS + "("
                + COLUMN_DRINK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DRINK_NAME + " TEXT, "
                + COLUMN_ALCOHOL_CONTENT + " REAL"
                + ")";
        db.execSQL(CREATE_TABLE_DRINKS);

        // Create Results Table
        String CREATE_TABLE_RESULTS = "CREATE TABLE " + TABLE_RESULTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_WEIGHT + " REAL, "
                + COLUMN_BLOOD_ALCOHOL_CONTENT + " REAL, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_TITLE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_RESULTS);

        // Create ConsumptionDetails Table
        String CREATE_TABLE_CONSUMPTION_DETAILS = "CREATE TABLE " + TABLE_CONSUMPTION_DETAILS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RESULT_ID + " INTEGER, "
                + COLUMN_DRINK_ID + " INTEGER, "
                + COLUMN_AMOUNT + " REAL, "
                + "FOREIGN KEY(" + COLUMN_RESULT_ID + ") REFERENCES " + TABLE_RESULTS + "(" + COLUMN_ID + "), "
                + "FOREIGN KEY(" + COLUMN_DRINK_ID + ") REFERENCES " + TABLE_DRINKS + "(" + COLUMN_DRINK_ID + ")"
                + ")";
        db.execSQL(CREATE_TABLE_CONSUMPTION_DETAILS);

        // Insert Predefined Drinks
        insertPredefinedDrinks(db);
    }

    // Upgrade Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop Old Tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSUMPTION_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRINKS);
        // Create New Tables
        onCreate(db);
    }

    // Insert Predefined Drinks
    private void insertPredefinedDrinks(SQLiteDatabase db) {
        // Insert Vodka
        String insertVodka = "INSERT INTO " + TABLE_DRINKS + "("
                + COLUMN_DRINK_NAME + ", " + COLUMN_ALCOHOL_CONTENT + ") VALUES ('Vodka', 40)";
        db.execSQL(insertVodka);

        // Insert Beer
        String insertBeer = "INSERT INTO " + TABLE_DRINKS + "("
                + COLUMN_DRINK_NAME + ", " + COLUMN_ALCOHOL_CONTENT + ") VALUES ('Beer', 5)";
        db.execSQL(insertBeer);

        // Insert Jack Daniels
        String insertJackDaniels = "INSERT INTO " + TABLE_DRINKS + "("
                + COLUMN_DRINK_NAME + ", " + COLUMN_ALCOHOL_CONTENT + ") VALUES ('Jack Daniels', 40)";
        db.execSQL(insertJackDaniels);

        // Add more predefined drinks as needed
    }
}


