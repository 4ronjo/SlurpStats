package com.example.slurpstats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DrinkDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] allColumns = {
            DatabaseHelper.COLUMN_DRINK_ID,
            DatabaseHelper.COLUMN_DRINK_NAME,
            DatabaseHelper.COLUMN_ALCOHOL_CONTENT
    };

    public DrinkDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Drink> getAllDrinks() {
        List<Drink> drinks = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_DRINKS, allColumns,
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Drink drink = cursorToDrink(cursor);
                drinks.add(drink);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return drinks;
    }

    private Drink cursorToDrink(Cursor cursor) {
        Drink drink = new Drink();
        drink.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DRINK_ID)));
        drink.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DRINK_NAME)));
        drink.setAlcoholContent(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ALCOHOL_CONTENT)));
        return drink;
    }


    public Drink addDrink(String name, double alcoholContent) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DRINK_NAME, name);
        values.put(DatabaseHelper.COLUMN_ALCOHOL_CONTENT, alcoholContent);

        long insertId = database.insert(DatabaseHelper.TABLE_DRINKS, null, values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_DRINKS, allColumns,
                DatabaseHelper.COLUMN_DRINK_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Drink newDrink = cursorToDrink(cursor);
        cursor.close();
        return newDrink;
    }

    public Drink getDrinkByName(String name) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_DRINKS, allColumns,
                DatabaseHelper.COLUMN_DRINK_NAME + " = ?", new String[]{name}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Drink drink = cursorToDrink(cursor);
            cursor.close();
            return drink;
        } else {
            return null;
        }
    }

    public Drink getDrinkById(long id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_DRINKS, allColumns,
                DatabaseHelper.COLUMN_DRINK_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Drink drink = cursorToDrink(cursor);
            cursor.close();
            return drink;
        } else {
            return null;
        }
    }

    public void deleteDrink(long id) {
        database.delete(DatabaseHelper.TABLE_DRINKS, DatabaseHelper.COLUMN_DRINK_ID + " = ?", new String[]{String.valueOf(id)});
    }
}