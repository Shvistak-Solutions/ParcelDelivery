package com.example.ParcelDelivery.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.HashMap;

public class UserDetailsFirstFragment extends Fragment {
    private static int userId;
    // Store instance variables
    private String title;
    private int page;

    TextView name, surname, pesel, email, idNum, address, postal, position;
    Button buttonRmv, buttonResetPassword, buttonTest;
    Intent intent;
    DatabaseHelper db;
    HashMap<String,String> details;

    // newInstance constructor for creating fragment with arguments
    public static UserDetailsFirstFragment newInstance(int page, String title, int id) {
        UserDetailsFirstFragment fragmentFirst = new UserDetailsFirstFragment();
        userId = page+1;
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        db = new DatabaseHelper(getContext());
        details = db.getData("Pracownicy",userId);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_userdetails, container, false);
        findLayoutItems(view);
        fillTextViews(details);
        TextView tvLabel = (TextView) view.findViewById(R.id.textDetailEmail);
        tvLabel.setText(page + " -- " + title);
        return view;
    }

    private void fillTextViews(HashMap<String,String> details) {

        name.setText(details.get("imie"));
        surname.setText(details.get("nazwisko"));
        email.setText(details.get("email"));
        position.setText(details.get("stanowisko"));
        pesel.setText(details.get("pesel"));
        address.setText(details.get("adres"));
        idNum.setText(details.get("nr_dowodu"));
        postal.setText(details.get("kod_pocztowy"));

    }

    private void findLayoutItems(View view){
        name = (TextView)view.findViewById(R.id.textDetailName);
        surname = (TextView)view.findViewById(R.id.textDetailSurname);
        email = (TextView)view.findViewById(R.id.textDetailEmail) ;
        position = (TextView) view.findViewById(R.id.textDetailPosition);
        pesel = (TextView)view.findViewById(R.id.textDetailPesel);
        idNum = (TextView)view.findViewById(R.id.textDetailIdNum);
        address = (TextView)view.findViewById(R.id.textDetailAddress);
        postal = (TextView)view.findViewById(R.id.textDetailPostal);
        buttonRmv = (Button)view.findViewById(R.id.buttonRemoveAccount);
        buttonResetPassword = (Button)view.findViewById(R.id.buttonResetPasswd);
        buttonTest = (Button)view.findViewById(R.id.buttonTest);
    }
}