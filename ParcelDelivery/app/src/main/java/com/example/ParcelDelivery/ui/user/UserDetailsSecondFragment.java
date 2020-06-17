package com.example.ParcelDelivery.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.HashMap;



public class UserDetailsSecondFragment extends Fragment {
    private  int userId;
    TextView text;

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
                R.layout.fragment_userdetails2, container, false);
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



    }

    private void findLayoutItems(View view){
        View monday = view.findViewById(R.id.includedLayoutMonday);
        text = monday.findViewById(R.id.textScheduleDay);
        text.setText("Poniedziałek");
//        name = (TextView)view.findViewById(R.id.textDetailName);
//        surname = (TextView)view.findViewById(R.id.textDetailSurname);
//        email = (TextView)view.findViewById(R.id.textDetailEmail) ;
//        position = (TextView) view.findViewById(R.id.textDetailPosition);
//        pesel = (TextView)view.findViewById(R.id.textDetailPesel);
//        idNum = (TextView)view.findViewById(R.id.textDetailIdNum);
//        address = (TextView)view.findViewById(R.id.textDetailAddress);
//        postal = (TextView)view.findViewById(R.id.textDetailPostal);
//        buttonRmv = (Button)view.findViewById(R.id.buttonRemoveAccount);
//        buttonResetPassword = (Button)view.findViewById(R.id.buttonResetPasswd);
    }

}