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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

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
    private TextView textWeek, textScheduleOrAvailability;
    private Button buttonAvailability, buttonSaveChanges;
    private AppCompatImageButton btnNextWeek, btnPreviousWeek;
    private String[] weekDays;
    private ArrayList<HashMap<String, String>> details;
    private DatabaseHelper db;
    private Calendar cal;
    boolean edit = false;
    boolean sameUser = false;
    boolean scheduleTrue = true;


    public static UserDetailsSecondFragment newInstance(int thisUserId,int userId) {
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
        if(userId == thisUserId)
            sameUser = true;
        weekDays = new String[5];
        findLayoutItems(view);
        cal = Calendar.getInstance();
        showWeek(0,1);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        buttonSaveChanges.setOnClickListener(v -> {
            if(sameUser){
                if(!scheduleTrue){
                    if (!edit) {
                        makeAllInputs();
                        edit = true;
                    } else {
                            saveHourData(0);
                    }
                }
            }else{
                if(scheduleTrue){
                    if (!edit) {
                        makeAllInputs();
                        edit = true;
                    } else {
                            saveHourData(1);
                    }
                }
            }
        });


        buttonAvailability.setOnClickListener(v->{
            if(scheduleTrue){
                switchToAvailability();
            }else{
                switchToSchedule();
            }
        });


        btnNextWeek.setOnClickListener(v->{
            if(scheduleTrue)
                showWeek(1,1);
            else
                showWeek(1,0);
        });

        btnPreviousWeek.setOnClickListener(v->{
            if(scheduleTrue)
                showWeek(-1,1);
            else
                showWeek(-1,0);
        });
    }

    private void switchToAvailability(){
        textScheduleOrAvailability.setText(R.string.availability);
        if(sameUser)
            buttonSaveChanges.setVisibility(View.VISIBLE);
        else
            buttonSaveChanges.setVisibility(View.INVISIBLE);
        showWeek(0,0);
        buttonAvailability.setText(R.string.check_schedule);
        scheduleTrue = false;
    }

    private void switchToSchedule(){
        textScheduleOrAvailability.setText(R.string.schedule);
        if(sameUser)
            buttonSaveChanges.setVisibility(View.INVISIBLE);
        else
            buttonSaveChanges.setVisibility(View.VISIBLE);
        showWeek(0,1);
        buttonAvailability.setText(R.string.check_availability);
        scheduleTrue = true;
    }


    private void showWeek(int state, int schedule_1_avability_0){
        if( state == 1)
            cal.add(Calendar.WEEK_OF_YEAR, +1);
        else if(state == -1)
            cal.add(Calendar.WEEK_OF_YEAR, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for(int i = 0; i < 5; i++){
            weekDays[i] = db.makeDateYMD(cal);
            cal.add(Calendar.DATE, 1);
        }
        if(schedule_1_avability_0 == 1)
            details = db.getDataSQL("select godzina_rozpoczecia,godzina_zakonczenia,data from Grafik where data between '"+weekDays[0]+"' and '"+weekDays[4]+"' and id_prac="+thisUserId+" order by data");
        else if (schedule_1_avability_0 == 0)
            details = db.getDataSQL("select godzina_rozpoczecia,godzina_zakonczenia,data from Dyspozycje where data between '"+weekDays[0]+"' and '"+weekDays[4]+"' and id_prac="+thisUserId+" order by data");

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


    private void saveHourData(int state){
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
                    String dateTime = db.makeDateTimeFromDateAndTime(weekDays[index/2],edit.getText().toString());
                    if( index % 2 == 0) {
                        if(state == 1){
                            if(db.updateSchedule(weekDays[index / 2], dateTime, null, 1, thisUserId) == 0)
                                db.insertSchedule(weekDays[index / 2], dateTime, null, 1, thisUserId);
                        }else{
                            if(db.updateSchedule(weekDays[index / 2], dateTime, null, 0, thisUserId) == 0)
                                db.insertSchedule(weekDays[index / 2], dateTime, null, 0, thisUserId);
                        }

                    } else {
                        if(state == 1){
                            if(db.updateSchedule(weekDays[index / 2], null, dateTime, 1, thisUserId) == 0)
                                db.insertSchedule(weekDays[index / 2], null, dateTime, 1, thisUserId);
                        }else{
                            if(db.updateSchedule(weekDays[index / 2], null, dateTime, 0, thisUserId) == 0)
                                db.insertSchedule(weekDays[index / 2], null, dateTime, 0, thisUserId);
                        }

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


    private void fillTextViews(ArrayList<HashMap<String,String>> details) {
        textWeek.setText(weekDays[0]+" : "+weekDays[4]);

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
        buttonAvailability = view.findViewById(R.id.buttonAvailability);
        buttonSaveChanges = view.findViewById(R.id.buttonSaveSchedule);
        textWeek = view.findViewById(R.id.textScheduleWeek);
        btnNextWeek = view.findViewById(R.id.buttonNextWeek);
        btnPreviousWeek = view.findViewById(R.id.buttonPreviousWeek);
        textScheduleOrAvailability = view.findViewById(R.id.textScheduleAvability);
        
        textScheduleOrAvailability.setText(R.string.schedule);

        if(sameUser){
            buttonSaveChanges.setVisibility(View.INVISIBLE);
            buttonAvailability.setText(R.string.check_availability);
        }

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