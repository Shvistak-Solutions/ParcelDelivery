package com.example.ParcelDelivery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.FontsContract;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "marmot.db"; // not case sensitive

    private String TAB_ACCOUNT = "Konta";
    private String TAB_WORKERS = "Pracownicy";
    private String TAB_SALARY = "Pensje";
    private String TAB_AVAILABILITY = "Dyspozycje";
    private String TAB_SCHEDULE = "Grafik";
    private String TAB_CLIENTS = "Klienci";
    private String TAB_PACKAGES = "Paczki";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE "+TAB_ACCOUNT+"(id INTEGER PRIMARY KEY, email VARCHAR unique, haslo VARCHAR)");
        database.execSQL("CREATE TABLE "+TAB_WORKERS+"(id INTEGER PRIMARY KEY AUTOINCREMENT, imie VARCHAR, nazwisko VARCHAR, stanowisko INTEGER, email VARCHAR unique, pesel VARCHAR unique, nr_dowodu VARCHAR unique, adres VARCHAR, kod_pocztowy VARCHAR);");
        database.execSQL("CREATE TABLE "+TAB_SALARY+"(id INTEGER, pensja FLOAT, ilosc_godzin INTEGER, data DATE, FOREIGN KEY(id) REFERENCES Pracownicy(id));");
        database.execSQL("CREATE TABLE "+TAB_AVAILABILITY+"(id INTEGER, data DATE, godzina_rozpoczecia DATETIME, godzina_zakonczenia DATETIME, FOREIGN KEY(id) REFERENCES Pracownicy(id));");
        database.execSQL("CREATE TABLE "+TAB_SCHEDULE+"(id INTEGER, data DATE, godzina_rozpoczecia DATETIME, godzina_zakonczenia DATETIME, wejscie DATETIME, wyjscie DATETIME, FOREIGN KEY(id) REFERENCES Pracownicy(id))");
        database.execSQL("CREATE TABLE "+TAB_CLIENTS+"(id INTEGER PRIMARY KEY AUTOINCREMENT, imie VARCHAR, nazwisko VARCHAR, adres VARCHAR, kod_pocztowy VARCHAR);");
        database.execSQL("CREATE TABLE "+TAB_PACKAGES+"(id INTEGER PRIMARY KEY AUTOINCREMENT, id_kuriera INTEGER, status INTEGER, id_nadawcy INTEGER, id_odbiorcy INTEGER, FOREIGN KEY(id_kuriera) REFERENCES Pracownicy(id), FOREIGN KEY(id_odbiorcy) REFERENCES Klienci(id), FOREIGN KEY(id_nadawcy) REFERENCES Klienci(id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TAB_PACKAGES);
        db.execSQL("DROP TABLE IF EXISTS "+TAB_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS "+TAB_AVAILABILITY);
        db.execSQL("DROP TABLE IF EXISTS "+TAB_SALARY);
        db.execSQL("DROP TABLE IF EXISTS "+TAB_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS "+TAB_WORKERS);
        db.execSQL("DROP TABLE IF EXISTS "+TAB_CLIENTS);

        onCreate(db);
    }

    public void dbSeed()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 1, 2);
        insertNewUser("Katarzyna","Kamyczek", 3, "kkamins@email.com", "666666666666", "kozak", "łukowica", "11111");
        insertNewUser("Rafał","Świstak", 0, "bober@email.com", "555555555555", "koza", "mielec", "11111");
        insertNewUser("Szczepan","'Szlachta' Komoniewski", 2, "szlachta@email.com", "44444444444444", "szlachta", "KopalniaSiarki", "11111");
        insertNewUser("Krzysztof","Dżachym", 1, "dżadża@email.com", "333333333333333", "jachym", "krakow", "11111");
        insertNewUser("Patryk","Frasio", 2, "frasio@email.com", "222222222222", "frasio", "konkurencyjnaKopalniaSiarki", "11111");
        insertNewUser("Łukasz","Scared", 1, "scared@email.com", "1111111111111", "difrent", "myślenice", "11111");
        insertNewUser("Zdzisław","Siwy", 3, "siwy@email.com", "0000000000000", "siwy", "kanciapa", "11111");

    }

    public void updatePassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String password_md5 = md5(password);
        values.put("haslo",password_md5);
        db.update("Konta",values,"email = ?",new String[] {email});
        db.close();

    }

    public boolean checkIfExists(String userEmail){
        SQLiteDatabase  db = this.getReadableDatabase();

        String query = "select "+ "email" + " from " + "Konta";
        Cursor cursor = db.rawQuery(query, null);
        String existEmail;

        if (cursor.moveToFirst()) {
            do {
                existEmail = cursor.getString(0);

                if (existEmail.equals(userEmail)) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }

    public int getUserId(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select id from "+TAB_WORKERS+" where email= '"+email+"'";
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

    //Dodaje nowych pracownikow
    public long insertNewUser(String name, String surname, int position,String email, String pesel, String idNum, String address, String postal){
        //Get the Data Repository in write mode
        if(surname.isEmpty() || name.isEmpty() || email.isEmpty())
            return 0;
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
        long newRowId = db.insert(TAB_WORKERS,null, cValues);
        if(newRowId != -1) {
            cValues.clear();
            cValues.put("email", email);
            cValues.put("haslo", md5("1234"));
            int id = getUserId(email);
            if (id != 0) {
                cValues.put("id", id);
                newRowId = db.insert(TAB_ACCOUNT, null, cValues);
                if(newRowId != -1)
                    autoFillOtherTables(id, db);
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


    public HashMap<String, String> getData(String[] columns, String table, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder columnList = new StringBuilder();
        HashMap<String,String> user = new HashMap<>();
        int howManyColumns = columns.length;
        for (int i = 0; i < howManyColumns; i++ )
        {
            columnList.append(columns[i]);
            if(i != howManyColumns - 1)
                columnList.append(", ");
        }
        String query = "SELECT "+columnList+" FROM "+table+" where id="+id;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            for( String column : columns) {
                if(column.equals("stanowisko"))
                {
                    user.put(column,getPosition(cursor.getString(cursor.getColumnIndex(column))));
                }
                else
                user.put(column, cursor.getString(cursor.getColumnIndex(column)));
            }
        }
        cursor.close();
        db.close();
        return user;
    }

    public String getData(String column, String table, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String user ="";
        String query = "SELECT "+column+" FROM "+table+" where id="+id;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            if(column.equals("stanowisko"))
            {
                user = getPosition(cursor.getString(cursor.getColumnIndex(column)));
            }
            else
                user = cursor.getString(cursor.getColumnIndex(column));
        }
        cursor.close();
        db.close();
        return user;
    }

    public HashMap<String, String> getData(String table, int id){

        SQLiteDatabase db = this.getWritableDatabase();
        HashMap<String, String> user = new HashMap<>();
        String query = "SELECT * FROM "+table+" where id ="+id;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            user = databaseContentToHashMap(table,cursor);
        }
        cursor.close();
        db.close();
        return  user;
    }

    public ArrayList<HashMap<String, String>> getData(String[] columns, String table){
        SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder columnList = new StringBuilder();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        int howManyColumns = columns.length;
        for (int i = 0; i < howManyColumns; i++ )
        {
            columnList.append(columns[i]);
            if(i != howManyColumns - 1)
                columnList.append(", ");
        }
        String query = "SELECT "+columnList+" FROM "+table;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            for( String column : columns) {
                if(column.equals("stanowisko"))
                {
                    user.put(column,getPosition(cursor.getString(cursor.getColumnIndex(column))));
                }
                else
                    user.put(column, cursor.getString(cursor.getColumnIndex(column)));
            }
            userList.add(user);
        }
        cursor.close();
        db.close();
        return userList;
    }

    public ArrayList<String> getData(String column, String table){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> userList = new ArrayList<>();

        String query = "SELECT "+column+" FROM "+table;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            if(column.equals("stanowisko"))
            {
                userList.add(getPosition(cursor.getString(cursor.getColumnIndex(column))));
            }
            else
                userList.add(cursor.getString(cursor.getColumnIndex(column)));
        }
        cursor.close();
        db.close();
        return userList;
    }

    public ArrayList<HashMap<String, String>> getData(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        String query = "SELECT * FROM "+table;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user = databaseContentToHashMap(table,cursor);
            userList.add(user);
        }
        cursor.close();
        db.close();
        return userList;
    }

    public int updateData(String[] columns, String[] values, String table, String whereColumn, String whereValue)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        int res;
        for(int i = 0; i < columns.length; i++)
        {
            if(columns[i].equals("haslo"))
                values[i]=md5(values[i]);
            val.put(columns[i],values[i]);
        }
        res = db.update(table,val,whereColumn+"= ?",new String[] {whereValue});
        db.close();
        return res;
    }

    public int updateData(String column,String value, String table,String whereColumn, String whereValue)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        int res;
        if(column.equals("haslo"))
            value=md5(value);
        val.put(column,value);
        res = db.update(table,val,whereColumn+"= ?",new String[] {whereValue});
        db.close();
        return res;
    }

    public int updateData(String[] columns, String[] values, String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        int res;
        for(int i = 0; i < columns.length; i++)
        {
            if(columns[i].equals("haslo"))
                values[i]=md5(values[i]);
            val.put(columns[i],values[i]);
        }
        res = db.update(table,val,null,null);
        db.close();
        return res;
    }

    public int updateData(String column,String value, String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        int res;
        if(column.equals("haslo"))
            value=md5(value);
        val.put(column,value);
        res = db.update(table,val,null,null);
        db.close();
        return res;
    }

    private void autoFillOtherTables(int id_worker, SQLiteDatabase db)
    {
        db.execSQL("Insert INTO "+TAB_SALARY+"(id) values ("+id_worker+") ");
        db.execSQL("Insert INTO "+TAB_AVAILABILITY+"(id) values ("+id_worker+") ");
        db.execSQL("Insert INTO "+TAB_SCHEDULE+"(id) values ("+id_worker+") ");

    }

    public void deleteUser(int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete( TAB_ACCOUNT, "id = ?",new String[]{String.valueOf(userId)});
        db.delete(TAB_AVAILABILITY, "id = ?",new String[]{String.valueOf(userId)});
        db.delete(TAB_SALARY, "id = ?",new String[]{String.valueOf(userId)});
        db.delete(TAB_SCHEDULE, "id = ?",new String[]{String.valueOf(userId)});
        db.delete(TAB_WORKERS, "id = ?",new String[]{String.valueOf(userId)});
        db.close();
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

    private HashMap<String,String> databaseContentToHashMap(String table, Cursor cursor)
    {
        HashMap<String, String> user = new HashMap<>();
        switch(table) {
            case "Pracownicy":
                String position;
                user.put("id", cursor.getString(cursor.getColumnIndex("id")));
                user.put("imie", cursor.getString(cursor.getColumnIndex("imie")));
                user.put("nazwisko", cursor.getString(cursor.getColumnIndex("nazwisko")));
                position = cursor.getString(cursor.getColumnIndex("stanowisko"));
                user.put("stanowisko", getPosition(position));
                user.put("email", cursor.getString(cursor.getColumnIndex("email")));
                user.put("pesel", cursor.getString(cursor.getColumnIndex("pesel")));
                user.put("nr_dowodu", cursor.getString(cursor.getColumnIndex("nr_dowodu")));
                user.put("adres", cursor.getString(cursor.getColumnIndex("adres")));
                user.put("kod_pocztowy", cursor.getString(cursor.getColumnIndex("kod_pocztowy")));
                break;
            case "Pensje":
                user.put("id", cursor.getString(cursor.getColumnIndex("id")));
                user.put("pensja", cursor.getString(cursor.getColumnIndex("pensja")));
                user.put("ilosc_godzin", cursor.getString(cursor.getColumnIndex("ilosc_godzin")));
                user.put("data", cursor.getString(cursor.getColumnIndex("data")));
                break;
            case "Grafik":
                user.put("id", cursor.getString(cursor.getColumnIndex("id")));
                user.put("godzina_rozpoczecia", cursor.getString(cursor.getColumnIndex("godzina_rozpoczecia")));
                user.put("godzina_zakonczenia", cursor.getString(cursor.getColumnIndex("godzina_zakonczenia")));
                user.put("data", cursor.getString(cursor.getColumnIndex("data")));
                user.put("wejscie", cursor.getString(cursor.getColumnIndex("wejscie")));
                user.put("wyjscie", cursor.getString(cursor.getColumnIndex("wyjscie")));
                break;
            case "Dyspozycje":
                user.put("id", cursor.getString(cursor.getColumnIndex("id")));
                user.put("godzina_rozpoczecia", cursor.getString(cursor.getColumnIndex("godzina_rozpoczecia")));
                user.put("godzina_zakonczenia", cursor.getString(cursor.getColumnIndex("godzina_zakonczenia")));
                user.put("data", cursor.getString(cursor.getColumnIndex("data")));
                break;
            default:
                break;
        }
        return user;
    }

}


