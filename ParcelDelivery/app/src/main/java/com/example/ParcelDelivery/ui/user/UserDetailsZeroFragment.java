package com.example.ParcelDelivery.ui.user;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.HashMap;

public class UserDetailsZeroFragment extends Fragment {
    private  int userId, thisUserId ;

    private DatabaseHelper db;


    public static UserDetailsZeroFragment newInstance(int thisUserId, int userId) {
        UserDetailsZeroFragment fragment = new UserDetailsZeroFragment();

        Bundle args = new Bundle();
        args.putInt("userId", userId);          // id usera przeglądającego
        args.putInt("thisUserId", thisUserId);  // id usera ktorego danych dotyczy  ps. jest to uzywane do przeglądania listy użytkownikow jeżeli takie oba id są sobie rowne
        fragment.setArguments(args);            // jeżeli oba id są sobie rowne - powinny byc podejmowane akcje - np uzytkownik wtedy nie moze uzywac niektorych przyciskow
                                                // nie moze ustawiac sobie pensji - można "zniknąć elementy" za pomocą Visibility GONE lub INVISIBLE

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
                R.layout.fragment_userdetails0, container, false);
        db = new DatabaseHelper(getContext());
        findLayoutItems(view);

        if(userId == thisUserId) // jeżeli użytkownik przeglądający, jest użytkownikiem posiadającym to konto - uniwersalność
        {
            // TODO:
        }
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();


    }


    private void fillTextViews(HashMap<String,String> details) {       // tutaj filluje danymi przykłąd na dole - taka sugestia, zeby pasowalo do reszty fragmentow

//        if(details.get("stawka") != null)
//            rate = Float.parseFloat((Objects.requireNonNull(details.get("stawka"))));
//        else
//            rate = 0;
//        if(details.get("ilosc_godzin") != null)
//            hours = Float.parseFloat((Objects.requireNonNull(details.get("ilosc_godzin"))));
//        else
//            hours = 0;
//        textRate.setText(details.get("stawka"));
//        textHours.setText(Float.toString(hours));
//
//        textSalary.setText(details.get("pensja"));


    }

    private void findLayoutItems(View view){ // tutaj znajduje itemy layoutu - tak w kwestii uporządkowania

//        textRate = (TextView)view.findViewById(R.id.textSalaryRate);
//        textRate.setInputType(InputType.TYPE_NULL);
//        textSalary = (TextView)view.findViewById(R.id.textSalary);
//        textHours = (TextView)view.findViewById(R.id.textSalaryHours);
//        buttonEdit = (Button)view.findViewById(R.id.buttonSalary);

    }



}