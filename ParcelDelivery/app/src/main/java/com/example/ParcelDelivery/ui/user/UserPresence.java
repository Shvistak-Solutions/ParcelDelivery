package com.example.ParcelDelivery.ui.user;

import android.content.Context;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UserPresence {

    Calendar cal;
    DatabaseHelper db;
    boolean presentSet;
    boolean absentSet;
    boolean workDay;
    ArrayList<HashMap<String,String>> details;
    int userId;
    Context context;

    public UserPresence(int id, Context con){
        userId = id;
        context = con;
        cal = Calendar.getInstance();
        db = new DatabaseHelper(context);
    }


    public int prepareFields(){
        details = db.getDataSQL("select data,wejscie, wyjscie from Grafik where id_prac="+userId+" and data like '"+db.makeDateYMD(cal)+"'");

        if(details.size() > 0){
            if(!db.cutTimeFromDateTime(details.get(0).get("wejscie")).equals("0")) {
                presentSet = true;
            }
            if(!db.cutTimeFromDateTime(details.get(0).get("wyjscie")).equals("0")){
                absentSet = true;
            }
            if(presentSet){
                if(absentSet){
                    return -2;
                }
                else
                    return -1;
            }
        }
        else{
            workDay = false;
            return -3;
        }
        return 1;
    }


    public void addEnter(){
        cal = Calendar.getInstance();
        db.updateDataSQL("update Grafik set wejscie=" + db.makeDateTime(cal) + " where id_prac="+userId+" and data =" + db.makeDateYMD(cal));
        presentSet = true;
        details = db.getDataSQL("select data,wejscie, wyjscie from Grafik where id_prac="+userId+" and data like '"+db.makeDateYMD(cal)+"'");
    }

    public void addExit(){
        cal = Calendar.getInstance();
        db.updateDataSQL("update Grafik set wyjscie="+db.makeDateTime(cal)+" where id_prac="+userId+" and data ="+db.makeDateYMD(cal));
        absentSet = true;
        details = db.getDataSQL("select data,wejscie, wyjscie from Grafik where id_prac="+userId+" and data like '"+db.makeDateYMD(cal)+"'");
    }
}
