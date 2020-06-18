package com.example.ParcelDelivery.ui.user;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;



public class UserDetailsSecondFragment extends Fragment {
    private  int userId, thisUserId;
    private View monday, tuesday, wednesday, thursday, friday;
    private EditText startMonday, startTuesday,startWednesday,startThursday,startFriday, endMonday, endTuesday,endWednesday,endThursday,endFriday;
    private TextView week;
    Button firstButton;
    AppCompatImageButton btnNextWeek, btnPreviousWeek;
    String firstDayOfWeek, lastDayOfWeek;
    ArrayList<HashMap<String, String>> details;
    DatabaseHelper db;
    Calendar cal;

    ViewPager viewPager;


    public static UserDetailsSecondFragment newInstance(int userId,int thisUserId) {
        UserDetailsSecondFragment fragment = new UserDetailsSecondFragment();

        //this.userId = userId;
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        args.putInt("thisUserId", thisUserId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getInt("userId", 0);
        thisUserId = getArguments().getInt("thisUserId", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_userdetails2, container, false);
        db = new DatabaseHelper(getContext());
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        firstDayOfWeek = db.makeDateYMD(cal);
        cal.add(Calendar.DATE, 4);
        lastDayOfWeek = db.makeDateYMD(cal);
        details = db.getDataSQL("select godzina_rozpoczecia,godzina_zakonczenia,data from Grafik where data between '"+firstDayOfWeek+"' and '"+lastDayOfWeek+"' and id_prac="+thisUserId+" order by data");
        findLayoutItems(view);
        fillTextViews(details);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstButton.setOnClickListener(v->{
            Toast.makeText(getContext(),"start",Toast.LENGTH_SHORT).show();
        });

        btnNextWeek.setOnClickListener(v->{
            cal.add(Calendar.WEEK_OF_YEAR, +1);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            firstDayOfWeek = db.makeDateYMD(cal);
            cal.add(Calendar.DATE, 4);
            lastDayOfWeek = db.makeDateYMD(cal);
            details = db.getDataSQL("select godzina_rozpoczecia,godzina_zakonczenia,data from Grafik where data between '"+firstDayOfWeek+"' and '"+lastDayOfWeek+"' and id_prac="+thisUserId+" order by data");
            fillTextViews(details);
        });

        btnPreviousWeek.setOnClickListener(v->{
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            firstDayOfWeek = db.makeDateYMD(cal);
            cal.add(Calendar.DATE, 4);
            lastDayOfWeek = db.makeDateYMD(cal);
            details = db.getDataSQL("select godzina_rozpoczecia,godzina_zakonczenia,data from Grafik where data between '"+firstDayOfWeek+"' and '"+lastDayOfWeek+"' and id_prac="+thisUserId+" order by data");
            fillTextViews(details);
        });
    }


    private void fillTextViews(ArrayList<HashMap<String,String>> details) {
        week.setText(firstDayOfWeek+" : "+lastDayOfWeek);

        int i = 0;
        String firstDay = firstDayOfWeek;
        if(details.size() > 0) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            while (!firstDay.equals(details.get(0).get("data")) && i < 5) {
                cal.add(Calendar.DATE, 1);
                firstDay = db.makeDateYMD(cal);
                i++;
            }
            if( i < 5) {
                int j = i;
                while (j < details.size() + i) {
                    switch (j) {
                        case 0:
                            startMonday.setText(details.get(j - i).get("godzina_rozpoczecia"));
                            endMonday.setText(details.get(j - i).get("godzina_zakonczenia"));
                            break;
                        case 1:
                            startTuesday.setText(details.get(j - i).get("godzina_rozpoczecia"));
                            endTuesday.setText(details.get(j - i).get("godzina_zakonczenia"));
                            break;
                        case 2:
                            startWednesday.setText(details.get(j - i).get("godzina_rozpoczecia"));
                            endWednesday.setText(details.get(j - i).get("godzina_zakonczenia"));
                            break;
                        case 3:
                            startThursday.setText(details.get(j - i).get("godzina_rozpoczecia"));
                            endThursday.setText(details.get(j - i).get("godzina_zakonczenia"));
                            break;
                        case 4:
                            startFriday.setText(details.get(j - i).get("godzina_rozpoczecia"));
                            endFriday.setText(details.get(j - i).get("godzina_zakonczenia"));
                            break;
                    }
                    j++;
                }
            }
            makeNoData(i);
        }else{
            makeNoData(5);
        }


    }

    private void findLayoutItems(View view){
        monday = view.findViewById(R.id.includedLayoutMonday);
        tuesday = view.findViewById(R.id.includedLayoutTuesday);
        wednesday = view.findViewById(R.id.includedLayoutWednesday);
        thursday = view.findViewById(R.id.includedLayoutThursday);
        friday = view.findViewById(R.id.includedLayoutFriday);
        TextView day = monday.findViewById(R.id.textScheduleDay);
        day.setText("Poniedziałek");
        day = tuesday.findViewById(R.id.textScheduleDay);
        day.setText("Wtorek");
        day = wednesday.findViewById(R.id.textScheduleDay);
        day.setText("Środa");
        day = thursday.findViewById(R.id.textScheduleDay);
        day.setText("Czwartek");
        day = friday.findViewById(R.id.textScheduleDay);
        day.setText("Piątek");
        startMonday = monday.findViewById(R.id.editTextScheduleStart);
        endMonday = monday.findViewById(R.id.editTextScheduleEnd);
        startTuesday = tuesday.findViewById(R.id.editTextScheduleStart);
        endTuesday = tuesday.findViewById(R.id.editTextScheduleEnd);
        startWednesday = wednesday.findViewById(R.id.editTextScheduleStart);
        endWednesday = wednesday.findViewById(R.id.editTextScheduleEnd);
        startThursday = thursday.findViewById(R.id.editTextScheduleStart);
        endThursday = thursday.findViewById(R.id.editTextScheduleEnd);
        startFriday = friday.findViewById(R.id.editTextScheduleStart);
        endFriday = friday.findViewById(R.id.editTextScheduleEnd);
        firstButton = view.findViewById(R.id.button2);
        week = view.findViewById(R.id.textScheduleWeek);
        btnNextWeek = view.findViewById(R.id.buttonNextWeek);
        btnPreviousWeek = view.findViewById(R.id.buttonPreviousWeek);

        makeAllNoInput();

    }

    private void setNoInput(EditText edit){
        edit.setInputType(InputType.TYPE_NULL);
    }

    private void makeAllNoInput(){
        setNoInput(startMonday);
        setNoInput(startThursday);
        setNoInput(startTuesday);
        setNoInput(startWednesday);
        setNoInput(startFriday);

        setNoInput(endFriday);
        setNoInput(endMonday);
        setNoInput(endThursday);
        setNoInput(endTuesday);
        setNoInput(endWednesday);
    }

    private void makeNoData(int i){
        if( i > 0 ) {
            startMonday.setText(R.string.no_data);
            endMonday.setText(R.string.no_data);
            if( i > 1) {
                startTuesday.setText(R.string.no_data);
                endTuesday.setText(R.string.no_data);
                if(i > 2) {
                    startWednesday.setText(R.string.no_data);
                    endWednesday.setText(R.string.no_data);
                    if(i > 3) {
                        startThursday.setText(R.string.no_data);
                        endThursday.setText(R.string.no_data);
                        if( i > 4) {
                            startFriday.setText(R.string.no_data);
                            endFriday.setText(R.string.no_data);
                        }
                    }
                }
            }
        }
    }

}