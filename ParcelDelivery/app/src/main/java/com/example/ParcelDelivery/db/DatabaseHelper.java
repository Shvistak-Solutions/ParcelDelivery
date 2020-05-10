package com.example.ParcelDelivery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "marmot.db"; // not case sensitive

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE Konta(id INTEGER PRIMARY KEY, email VARCHAR unique, haslo VARCHAR);");
        database.execSQL("CREATE TABLE Pracownicy(id INTEGER PRIMARY KEY AUTOINCREMENT, imie VARCHAR, nazwisko VARCHAR, stanowisko INTEGER, email VARCHAR unique, pesel VARCHAR, nr_dowodu VARCHAR, adres VARCHAR, kod_pocztowy VARCHAR);");
        database.execSQL("CREATE TABLE Pensje(id INTEGER, pensja FLOAT, ilosc_godzin INTEGER, data DATE, FOREIGN KEY(id) REFERENCES Pracownicy(id));");
        database.execSQL("CREATE TABLE Dyspozycje(id INTEGER, data DATE, godzina_rozpoczecia DATETIME, godzina_zakonczenia DATETIME, FOREIGN KEY(id) REFERENCES Pracownicy(id));");
        database.execSQL("CREATE TABLE Grafik(id INTEGER, data DATE, godzina_rozpoczecia DATETIME, godzina_zakonczenia DATETIME, wejscie DATETIME, wyjscie DATETIME, FOREIGN KEY(id) REFERENCES Pracownicy(id))");
        database.execSQL("CREATE TABLE Klienci(id INTEGER PRIMARY KEY AUTOINCREMENT, imie VARCHAR, nazwisko VARCHAR, adres VARCHAR, kod_pocztowy VARCHAR);");
        database.execSQL("CREATE TABLE Paczki(id INTEGER PRIMARY KEY AUTOINCREMENT, id_kuriera INTEGER, status INTEGER, id_nadawcy INTEGER, id_odbiorcy INTEGER, FOREIGN KEY(id_kuriera) REFERENCES Pracownicy(id), FOREIGN KEY(id_odbiorcy) REFERENCES Klienci(id), FOREIGN KEY(id_nadawcy) REFERENCES Klienci(id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Konta");
        db.execSQL("DROP TABLE IF EXISTS Pracownicy");
        db.execSQL("DROP TABLE IF EXISTS Pensje");
        db.execSQL("DROP TABLE IF EXISTS Dyspozycje");
        db.execSQL("DROP TABLE IF EXISTS Grafik");
        db.execSQL("DROP TABLE IF EXISTS Klienci");
        db.execSQL("DROP TABLE IF EXISTS Paczki");

        onCreate(db);
    }

    public void dbSeed()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 1, 2);
        insertUserDetails("Katarzyna","Kamyczek", 3, "kkamins@email.com", "666666666666", "kozak", "łukowica", "11111");
        insertUserDetails("Rafał","Świstak", 0, "bober@email.com", "555555555555", "koza", "mielec", "11111");
        insertUserDetails("Szczepan","'Szlachta' Komoniewski", 2, "szlachta@email.com", "44444444444444", "szlachta", "KopalniaSiarki", "11111");
        insertUserDetails("Krzysztof","Dżachym", 1, "dżadża@email.com", "333333333333333", "jachym", "krakow", "11111");
        insertUserDetails("Patryk","Frasio", 2, "frasio@email.com", "222222222222", "frasio", "konkurencyjnaKopalniaSiarki", "11111");
        insertUserDetails("Łukasz","Scared", 1, "scared@email.com", "1111111111111", "difrent", "myślenice", "11111");
        insertUserDetails("Zdzisław","Siwy", 3, "siwy@email.com", "0000000000000", "siwy", "kanciapa", "11111");

    }

    public int getUserId(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select id from Pracownicy where email= '"+email+"'";
        Cursor cursor = db.rawQuery(query,null);
        int id;
        if(cursor.moveToFirst())
        {
            id = cursor.getInt(0);
        }
        else {
            cursor.close();
            return 0;
        }
        cursor.close();
        return id;
    }

    public long insertUserDetails(String name, String surname, int position,String email, String pesel, String idNum, String address, String postal){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        //ContentValues accountValues = new ContentValues();
        cValues.put("imie", name);
        cValues.put("nazwisko", surname);
        cValues.put("stanowisko", position);
        cValues.put("email",email);
        cValues.put("pesel", pesel);
        cValues.put("nr_dowodu", idNum);
        cValues.put("adres", address);
        cValues.put("kod_pocztowy", postal);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("Pracownicy",null, cValues);
        if(newRowId != -1) {
            cValues.clear();
            cValues.put("email", email);
            cValues.put("haslo", md5("1234"));
            int id = getUserId(email);
            if (id != 0) {
                cValues.put("id", id);
                newRowId = db.insert("Konta", null, cValues);
                if(newRowId != -1)
                    AutoFillOtherTables(id, db);
            }
            else return -1;
        }
        else {
            db.close();
            return newRowId;
        }
        db.close();
        return newRowId;
    }

    public ArrayList<HashMap<String, String>> GetUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String position;
        String query = "SELECT imie, nazwisko, stanowisko, email FROM Pracownicy";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("imie",cursor.getString(cursor.getColumnIndex("imie")));
            user.put("nazwisko",cursor.getString(cursor.getColumnIndex("nazwisko")));
            position = cursor.getString(cursor.getColumnIndex("stanowisko"));
            user.put("stanowisko",getPosition( position ));
            user.put("email",cursor.getString(cursor.getColumnIndex("email")));
            userList.add(user);
        }
        cursor.close();
        db.close();
        return  userList;
    }

    public HashMap<String, String> GetUserDetails(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        HashMap<String, String> user = new HashMap<>();
        String position;
        String query = "SELECT * FROM Pracownicy where id ="+id;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            user.put("imie",cursor.getString(cursor.getColumnIndex("imie")));
            user.put("nazwisko",cursor.getString(cursor.getColumnIndex("nazwisko")));
            position = cursor.getString(cursor.getColumnIndex("stanowisko"));
            user.put("stanowisko",getPosition( position ));
            user.put("email",cursor.getString(cursor.getColumnIndex("email")));
            user.put("pesel",cursor.getString(cursor.getColumnIndex("pesel")));
            user.put("nr_dowodu",cursor.getString(cursor.getColumnIndex("nr_dowodu")));
            user.put("adres",cursor.getString(cursor.getColumnIndex("adres")));
            user.put("kod_pocztowy",cursor.getString(cursor.getColumnIndex("kod_pocztowy")));
        }
        cursor.close();
        db.close();
        return  user;
    }

    public String GetUserDetail(String name,String table, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String result = "";
        String query = "SELECT "+name+" FROM "+table+" where id ="+id;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            if(name.equals("stanowisko"))
            {
                result = cursor.getString(cursor.getColumnIndex(name));
                result = getPosition(result);
            }
            else
               result =cursor.getString(cursor.getColumnIndex(name));
        }
        cursor.close();
        db.close();
        return  result;
    }

    public ArrayList<String> GetUsersWhatYouWant(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> userList = new ArrayList<>();
        String query = "SELECT "+name+" FROM Pracownicy";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            if(name.equals("stanowisko"))
            {
                String position = cursor.getString(cursor.getColumnIndex(name));
                userList.add(getPosition( position ));
            }
            else
            userList.add(cursor.getString(cursor.getColumnIndex(name)));
        }
        cursor.close();
        db.close();
        return  userList;
    }


    private void AutoFillOtherTables(int id_worker, SQLiteDatabase db)
    {
        db.execSQL("Insert INTO Pensje(id) values ("+id_worker+") ");
        db.execSQL("Insert INTO Dyspozycje(id) values ("+id_worker+") ");
        db.execSQL("Insert INTO Grafik(id) values ("+id_worker+") ");

    }

    public void DeleteUser(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Pracownicy", "id = ?",new String[]{String.valueOf(userid)});
        db.close();
    }

    private String getPosition(String position)
    {
        switch(position)
        {
            case "0":
                position = "Kurier";
                break;
            case "1":
                position = "Magazynier";
                break;
            case "2":
                position = "Koordynator";
                break;
            case "3":
                position = "Manager";
                break;
            default:
                position = "WTF";
                break;
        }
        return position;
    }

    // Ta funkcja to mocny snippet
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash

            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
