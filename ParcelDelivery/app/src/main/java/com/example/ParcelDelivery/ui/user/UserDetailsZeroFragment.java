package com.example.ParcelDelivery.ui.user;
import com.example.ParcelDelivery.ui.avatar.AvatarActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.avatar.AvatarActivity;
import com.example.ParcelDelivery.ui.coordinator.CoordinatorActivity;
import com.example.ParcelDelivery.ui.courier.CourierActivity;
import com.example.ParcelDelivery.ui.password_reset.ChangePasswordActivity;

import java.util.HashMap;

public class UserDetailsZeroFragment extends Fragment {
    private  int userId, thisUserId ;
    private HashMap<String, String> details;
    private TextView name, surname;
    private ImageView avatar;
    private Button avatarChange,passwordChange;
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

        if(userId == thisUserId) // jeżeli użytkownik przeglądający, jest użytkownikiem posiadającym to konto - uniwersalność
        {
            // TODO:
        }
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DatabaseHelper(getContext());
        details = db.getData("Pracownicy", thisUserId);
        findLayoutItems(view);
        fillTextViews(details);
        ImageView mainAvatarView = (ImageView)view.findViewById(R.id.ID_UD0_AVATAR_VIEW);
        Bitmap bitmap;
       // DatabaseHelper dbH = new DatabaseHelper(this);
        int avatarExists = db.doesUserHasAvatar(thisUserId);
        if( avatarExists == 0){
            mainAvatarView.setImageResource(R.drawable.avatar0);
        }
        else{
            bitmap = db.getAvatarAsBitmap(thisUserId);
            mainAvatarView.setImageBitmap(bitmap);
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        if(userId == thisUserId)
        {
            avatarChange.setVisibility(View.VISIBLE);
            passwordChange.setVisibility(View.VISIBLE);
        }

        avatarChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AvatarActivity.class);
                intent.putExtra("thisUserId", thisUserId);
                intent.putExtra("class","com.example.ParcelDelivery.ui.courier.CourierActivity");
                startActivity(intent);
            }
        });

        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChangePasswordActivity.class);
                intent.putExtra("thisUserId", thisUserId);
                startActivity(intent);
            }
        });


    }


    private void fillTextViews(HashMap<String,String> details) {
        name.setText(details.get("imie"));
        surname.setText(details.get("nazwisko"));

    }

    private void findLayoutItems(View view){
        name = (TextView)view.findViewById(R.id.ID_UD0_NAME);
        surname = (TextView)view.findViewById(R.id.ID_UD0_SURNAME);
        avatar = (ImageView)view.findViewById(R.id.ID_UD0_AVATAR_VIEW);
        avatarChange = (Button)view.findViewById(R.id.ID_GO_TO_AVATAR);
        avatarChange.setVisibility(View.INVISIBLE);
        passwordChange = (Button)view.findViewById(R.id.ID_CHANGE_PASSWORD);
        passwordChange.setVisibility(View.INVISIBLE);

    }



}