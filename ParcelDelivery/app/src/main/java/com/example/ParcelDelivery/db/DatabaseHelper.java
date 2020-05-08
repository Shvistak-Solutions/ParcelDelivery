package com.example.ParcelDelivery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "marmot.db"; // not case sensitive

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE Konta(id_pracownika INTEGER PRIMARY KEY, email VARCHAR, haslo VARCHAR);");
        database.execSQL("CREATE TABLE Pracownicy(id INTEGER PRIMARY KEY AUTOINCREMENT, imie VARCHAR, nazwisko VARCHAR, stanowisko INTEGER, pesel VARCHAR, nr_dowodu VARCHAR, adres VARCHAR, kod_pocztowy VARCHAR);");
        database.execSQL("CREATE TABLE Pensje(id_pracownika INTEGER, pensja FLOAT, ilosc_godzin INTEGER, data DATE, FOREIGN KEY(id_pracownika) REFERENCES Pracownicy(id));");
        database.execSQL("CREATE TABLE Dyspozycje(id_pracownika INTEGER, data DATE, godzina_rozpoczecia DATETIME, godzina_zakonczenia DATETIME, FOREIGN KEY(id_pracownika) REFERENCES Pracownicy(id));");
        database.execSQL("CREATE TABLE Grafik(id_pracownika INTEGER, data DATE, godzina_rozpoczecia DATETIME, godzina_zakonczenia DATETIME, wejscie DATETIME, wyjscie DATETIME, FOREIGN KEY(id_pracownika) REFERENCES Pracownicy(id))");
        database.execSQL("CREATE TABLE Klienci(id INTEGER PRIMARY KEY AUTOINCREMENT, imie VARCHAR, nazwisko VARCHAR, adres VARCHAR, kod_pocztowy VARCHAR);");
        database.execSQL("CREATE TABLE Paczki(id INTEGER PRIMARY KEY AUTOINCREMENT, id_kuriera INTEGER, status INTEGER, id_nadawcy INTEGER, id_odbiorcy INTEGER, FOREIGN KEY(id_kuriera) REFERENCES Pracownicy(id), FOREIGN KEY(id_odbiorcy) REFERENCES Klienci(id), FOREIGN KEY(id_nadawcy) REFERENCES Klienci(id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void insertUserDetails(String name, String surname, int position, String pesel, String idNum, String address, String postal){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("imie", name);
        cValues.put("nazwisko", surname);
        cValues.put("stanowisko", position);
        cValues.put("pesel", pesel);
        cValues.put("nr_dowodu", idNum);
        cValues.put("adres", address);
        cValues.put("kod_pocztowy", postal);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("Pracownicy",null, cValues);
        db.close();
    }

    public void DeleteUser(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Pracownicy", "id = ?",new String[]{String.valueOf(userid)});
        db.close();
    }
}
