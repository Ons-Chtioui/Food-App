package com.example.foodapp.Helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodapp.Domain.Foods;

import java.util.ArrayList;

public class FoodDatabase /*extends SQLiteOpenHelper */{
/*
    private static final String DB_NAME = "foodapp.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_FOOD = "foods";

    public FoodDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_FOOD + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Title TEXT," +
                "Description TEXT," +
                "Price REAL," +
                "ImagePath TEXT," +
                "Star REAL," +
                "TimeValue INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        onCreate(db);
    }

    // Ajouter un Food
    public long addFood(Foods f) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Title", f.getTitle());
        cv.put("Description", f.getDescription());
        cv.put("Price", f.getPrice());
        cv.put("ImagePath", f.getImagePath());
        cv.put("Star", f.getStar());
        cv.put("TimeValue", f.getTimeValue());

        return db.insert(TABLE_FOOD, null, cv);
    }

    // Récupérer tous les foods
    public ArrayList<Foods> getAllFoods() {
        ArrayList<Foods> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_FOOD, null);

        if (c.moveToFirst()) {
            do {
                Foods f = new Foods();
                f.setId(c.getInt(0));
                f.setTitle(c.getString(1));
                f.setDescription(c.getString(2));
                f.setPrice(c.getDouble(3));
                f.setImagePath(c.getString(4));
                f.setStar(c.getDouble(5));
                f.setTimeValue(c.getInt(6));

                list.add(f);

            } while (c.moveToNext());
        }
        c.close();
        return list;
    }*/
}
