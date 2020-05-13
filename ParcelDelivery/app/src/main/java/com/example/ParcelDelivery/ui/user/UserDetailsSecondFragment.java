package com.example.ParcelDelivery.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.HashMap;



public class UserDetailsSecondFragment extends Fragment {
    private  int userId;

    private TextView name, surname, pesel, email, idNum, address, postal, position;
    private Button buttonRmv, buttonResetPassword, buttonTest;


    ViewPager viewPager;


    public static UserDetailsSecondFragment newInstance(int userId) {
        UserDetailsSecondFragment fragment = new UserDetailsSecondFragment();

        //this.userId = userId;
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getInt("userId", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.activity_userdetails, container, false);
        DatabaseHelper db = new DatabaseHelper(getContext());
        HashMap<String, String> details = db.getData("Pracownicy", userId);
        findLayoutItems(view);
        fillTextViews(details);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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