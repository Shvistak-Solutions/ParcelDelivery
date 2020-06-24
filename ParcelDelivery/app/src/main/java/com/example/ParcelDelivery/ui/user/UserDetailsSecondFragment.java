package com.example.ParcelDelivery.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Objects;


public class UserDetailsSecondFragment extends Fragment {
    private  int userId, thisUserId;
    private View monday, tuesday, wednesday, thursday, friday;
    private EditText startMonday, startTuesday,startWednesday,startThursday,startFriday, endMonday, endTuesday,endWednesday,endThursday,endFriday;
    private EditText[] EditHolder = new EditText[10];
    private TextView week;
    private Button firstButton;
    private AppCompatImageButton btnNextWeek, btnPreviousWeek;
    private String[] weekDays;
    private ArrayList<HashMap<String, String>> details;
    private DatabaseHelper db;
    private Calendar cal;
    boolean edit = false;


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
        weekDays = new String[5];
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for(int i = 0; i < 5; i++){
            weekDays[i] = db.makeDateYMD(cal);
            cal.add(Calendar.DATE, 1);
        }
        details = db.getDataSQL("select godzina_rozpoczecia,godzina_zakonczenia,data from Grafik where data between '"+weekDays[0]+"' and '"+weekDays[4]+"' and id_prac="+thisUserId+" order by data");
        findLayoutItems(view);
        fillTextViews(details);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstButton.setOnClickListener(v->{
            if(!edit) {
                makeAllInputs();
                edit = true;
            }
            else {
                boolean allTrue = true;
                int index = 0;
                int firstFalseIndex = -1;
                for(EditText edit : EditHolder){
                    if(!edit.getText().toString().equals("Brak Danych")){
                        if(!checkRegex(edit.getText().toString())){
                            allTrue = false;
                            if(firstFalseIndex < 0 )
                                firstFalseIndex = index;
                        }else{
                            String dateTime = MakeDateTimeFromDateAndTime(weekDays[index/2],edit.getText().toString());
                            if( index % 2 == 0) {
                                if(db.updateSchedule(weekDays[index / 2], dateTime, null, 1, thisUserId) == 0)
                                    db.insertSchedule(weekDays[index / 2], dateTime, null, 1, thisUserId);
                            } else {
                                if(db.updateSchedule(weekDays[index / 2], null, dateTime, 1, thisUserId) == 0)
                                    db.insertSchedule(weekDays[index / 2], null, dateTime, 1, thisUserId);
                            }
                        }
                    }
                    index++;
                }
                if(allTrue) {
                    makeAllNoInput();
                    edit = false;
                }else{
                    EditHolder[firstFalseIndex].requestFocus();
                    View view1 = Objects.requireNonNull(getActivity()).getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.showSoftInput(view1, 0);
                    }
                }

            }
        });

        btnNextWeek.setOnClickListener(v->{
            showAnotherWeek(1);
        });

        btnPreviousWeek.setOnClickListener(v->{
            showAnotherWeek(0);
        });
    }


    private void showAnotherWeek(int state){
        if( state == 1)
            cal.add(Calendar.WEEK_OF_YEAR, +1);
        else
            cal.add(Calendar.WEEK_OF_YEAR, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for(int i = 0; i < 5; i++){
            weekDays[i] = db.makeDateYMD(cal);
            cal.add(Calendar.DATE, 1);
        }
        details = db.getDataSQL("select godzina_rozpoczecia,godzina_zakonczenia,data from Grafik where data between '"+weekDays[0]+"' and '"+weekDays[4]+"' and id_prac="+thisUserId+" order by data");
        fillTextViews(details);
    }


    private boolean checkRegex(String text){
        boolean isIt = text.matches("^([0-2]?[0-9]:[0-5][0-9])$");
        if(!isIt)
            return false;
        String[] help = text.split(":");
        if(help[0].matches("^(2[0-9])$")){
            return help[0].matches("^(2[0-3])$");
        }else
            return true;
    }

    private String MakeDateTimeFromDateAndTime(String date, String Time){
        return date.replace(" ","")+" "+Time.replace(" ","");
    }


    private void fillTextViews(ArrayList<HashMap<String,String>> details) {
        week.setText(weekDays[0]+" : "+weekDays[4]);

        int i = 0;
        String firstDay = weekDays[0];
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
                            startMonday.setText(db.cutTimeFromDateTime(details.get(j - i).get("godzina_rozpoczecia")));
                            endMonday.setText(db.cutTimeFromDateTime(details.get(j - i).get("godzina_zakonczenia")));
                            break;
                        case 1:
                            startTuesday.setText(db.cutTimeFromDateTime(details.get(j - i).get("godzina_rozpoczecia")));
                            endTuesday.setText(db.cutTimeFromDateTime(details.get(j - i).get("godzina_zakonczenia")));
                            break;
                        case 2:
                            startWednesday.setText(db.cutTimeFromDateTime(details.get(j - i).get("godzina_rozpoczecia")));
                            endWednesday.setText(db.cutTimeFromDateTime(details.get(j - i).get("godzina_zakonczenia")));
                            break;
                        case 3:
                            startThursday.setText(db.cutTimeFromDateTime(details.get(j - i).get("godzina_rozpoczecia")));
                            endThursday.setText(db.cutTimeFromDateTime(details.get(j - i).get("godzina_zakonczenia")));
                            break;
                        case 4:
                            startFriday.setText(db.cutTimeFromDateTime(details.get(j - i).get("godzina_rozpoczecia")));
                            endFriday.setText(db.cutTimeFromDateTime(details.get(j - i).get("godzina_zakonczenia")));
                            break;
                    }
                    j++;
                }
            }
            makeNoData(i,0);
            if(i + details.size() < 5) {
                i = details.size();
                makeNoData(5 - i, 1);
            }
        }else{
            makeNoData(5,0);
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
        assignEditTextToArray();

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
    
    private void setInput(EditText edit) {edit.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);}

    private void makeAllInputs(){
        setInput(startMonday);
        setInput(startThursday);
        setInput(startTuesday);
        setInput(startWednesday);
        setInput(startFriday);

        setInput(endFriday);
        setInput(endMonday);
        setInput(endThursday);
        setInput(endTuesday);
        setInput(endWednesday);
    }

    private void assignEditTextToArray(){
        EditHolder[0] = startMonday;
        EditHolder[1] = endMonday;
        EditHolder[2] = startTuesday;
        EditHolder[3] = endTuesday;
        EditHolder[4] = startWednesday;
        EditHolder[5] = endWednesday;
        EditHolder[6] = startThursday;
        EditHolder[7] = endThursday;
        EditHolder[8] = startFriday;
        EditHolder[9] = endFriday;
    }

    private void makeNoData(int i, int state) {
        if (state == 0) {
            if (i > 0) {
                startMonday.setText(R.string.no_data);
                endMonday.setText(R.string.no_data);
                if (i > 1) {
                    startTuesday.setText(R.string.no_data);
                    endTuesday.setText(R.string.no_data);
                    if (i > 2) {
                        startWednesday.setText(R.string.no_data);
                        endWednesday.setText(R.string.no_data);
                        if (i > 3) {
                            startThursday.setText(R.string.no_data);
                            endThursday.setText(R.string.no_data);
                            if (i > 4) {
                                startFriday.setText(R.string.no_data);
                                endFriday.setText(R.string.no_data);
                            }
                        }
                    }
                }
            }
        }else{
            if (i > 0) {
                startFriday.setText(R.string.no_data);
                endFriday.setText(R.string.no_data);
                if (i > 1) {
                    startThursday.setText(R.string.no_data);
                    endThursday.setText(R.string.no_data);
                    if (i > 2) {
                        startWednesday.setText(R.string.no_data);
                        endWednesday.setText(R.string.no_data);
                        if (i > 3) {
                            startTuesday.setText(R.string.no_data);
                            endTuesday.setText(R.string.no_data);
                            if (i > 4) {
                                startMonday.setText(R.string.no_data);
                                endMonday.setText(R.string.no_data);
                            }
                        }
                    }
                }
            }
        }
    }
}