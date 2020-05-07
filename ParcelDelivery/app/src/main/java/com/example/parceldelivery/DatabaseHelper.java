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
        database.execSQL("CREATE TABLE Konta(id_pracownika INTEGER PRIMARY KEY, email VARCHAR, haslo VARCHAR);");
        database.execSQL("CREATE TABLE Pracownicy(id INTEGER PRIMARY KEY AUTOINCREMENT, imie VARCHAR, nazwisko VARCHAR, stanowisko INTEGER, pesel VARCHAR, nr_dowodu VARCHAR, adres VARCHAR, kod_pocztowy VARCHAR);");
        database.execSQL("CREATE TABLE Pensje(id_pracownika INTEGER, pensja FLOAT, ilosc_godzin INTEGER, data DATE, FOREIGN KEY(id_pracownika) REFERENCES Pracownicy(id));");
        database.execSQL("CREATE TABLE Dyspozycje(id_pracownika INTEGER, data DATE, godzina_rozpoczecia DATETIME, godzina_zakonczenia DATETIME, FOREIGN KEY(id_pracownika) REFERENCES Pracownicy(id));");
        database.execSQL("CREATE TABLE Grafik(id_pracownika INTEGER, data DATE, godzina_rozpoczecia DATETIME, godzina_zakonczenia DATETIME, wejscie DATETIME, wyjscie DATETIME, FOREIGN KEY(id_pracownika) REFERENCES Pracownicy(id))");
        database.execSQL("CREATE TABLE Klienci(id INTEGER PRIMARY KEY AUTOINCREMENT, imie VARCHAR, nazwisko VARCHAR, adres VARCHAR, kod_pocztowy VARCHAR);");
        database.execSQL("CREATE TABLE Paczki(id INTEGER PRIMARY KEY AUTOINCREMENT, id_kuriera INTEGER, status INTEGER, id_nadawcy INTEGER, id_odbiorcy INTEGER, FOREIGN KEY(id_kuriera) REFERENCES Pracownicy(id), FOREIGN KEY(id_odbiorcy) REFERENCES Klienci(id), FOREIGN KEY(id_nadawcy) REFERENCES Klienci(id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {

    }
}
