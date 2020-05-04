package com.example.parceldelivery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "marmot.db"; // not case sensitive

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE Pracownicy(id INT PRIMARY KEY AUTOINCREMENT, imie VARCHAR, nazwisko VARCHAR, stanowisko INT, pesel VARCHAR, nr_dowodu VARCHAR, adres VARCHAR, kod_pocztowy VARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {

    }
}
