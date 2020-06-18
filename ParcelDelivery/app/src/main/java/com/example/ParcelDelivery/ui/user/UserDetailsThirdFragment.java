package com.example.ParcelDelivery.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.HashMap;
import java.util.Objects;


public class UserDetailsThirdFragment extends Fragment {
    private  int userId, thisUserId ;
    private float rate, salary, hours;

    private TextView textRate, textSalary, textHours;
    private Button buttonEdit;
    DatabaseHelper db;
    HashMap<String, String> details;



    public static UserDetailsThirdFragment newInstance(int thisUserId, int userId) {
        UserDetailsThirdFragment fragment = new UserDetailsThirdFragment();

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
                R.layout.fragment_userdetails3, container, false);
        db = new DatabaseHelper(getContext());
        details = db.getData("Pensje", thisUserId);
        findLayoutItems(view);
        fillTextViews(details);
        if(userId == thisUserId)
        {
            buttonEdit.setVisibility(View.GONE);
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

        buttonEdit.setOnClickListener(v -> {
            toggleOnClick();
        });
    }

    private void toggleOnClick() {
        String helper = buttonEdit.getText().toString();

        if (helper.equals(getString(R.string.save_rate))) {
            if (textRate.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Należy ustawić wartość", Toast.LENGTH_SHORT).show();
            } else {
                rate = Float.parseFloat(textRate.getText().toString());
                db.updateDataSQL("Update Pensje set stawka="+rate+" where id="+thisUserId);
                salary = rate * hours;
                String newSalary = "";
                newSalary+=salary;
                db.updateDataSQL("Update Pensje set pensja="+newSalary+" where id="+thisUserId);
                buttonEdit.setText(R.string.edit_rate);
                details = db.getData("Pensje", thisUserId);
                fillTextViews(details);
                textRate.setInputType(InputType.TYPE_NULL);
                View view1 = Objects.requireNonNull(getActivity()).getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                textRate.clearFocus();
            }

        } else {
            textRate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            textRate.requestFocus();
            View view1 = Objects.requireNonNull(getActivity()).getCurrentFocus();
            if (view1 != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(view1, 0);
                buttonEdit.setText(R.string.save_rate);
            }
        }
    }


    private void fillTextViews(HashMap<String,String> details) {

        if(details.get("stawka") != null)
            rate = Float.parseFloat((Objects.requireNonNull(details.get("stawka"))));
        else
            rate = 0;
        if(details.get("ilosc_godzin") != null)
            hours = Float.parseFloat((Objects.requireNonNull(details.get("ilosc_godzin"))));
        else
            hours = 0;
        textRate.setText(details.get("stawka"));
        textHours.setText(Float.toString(hours));

        textSalary.setText(details.get("pensja"));


    }

    private void findLayoutItems(View view){

        textRate = (TextView)view.findViewById(R.id.textSalaryRate);
        textRate.setInputType(InputType.TYPE_NULL);
        textSalary = (TextView)view.findViewById(R.id.textSalary);
        textHours = (TextView)view.findViewById(R.id.textSalaryHours);
        buttonEdit = (Button)view.findViewById(R.id.buttonSalary);

    }



}