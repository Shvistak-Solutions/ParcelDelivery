package com.example.ParcelDelivery.ui.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.HashMap;

public class UserDetailsFirstFragment extends Fragment {
    private int thisUserId;
    private  int userId;

    private TextView name, surname, pesel, email, idNum, address, postal, position;
    private Button buttonRmv, buttonResetPassword, buttonEditData;
    private Spinner spinner;
    private DatabaseHelper db;
    private HashMap<String, String> details;
    private AlertDialog dialogRemove;
    private AlertDialog dialogReset;
    boolean edit = false;
    boolean sameUser = false;

    public static UserDetailsFirstFragment newInstance(int thisUserId, int userId) {
        UserDetailsFirstFragment fragment = new UserDetailsFirstFragment();

        Bundle args = new Bundle();
        args.putInt("thisUserId", thisUserId);
        args.putInt("userId", userId);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        userId = getArguments().getInt("userId", 0);
        thisUserId = getArguments().getInt("thisUserId", 0);
        if( userId == thisUserId){
            sameUser = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_userdetails1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DatabaseHelper(getContext());
        details = db.getData("Pracownicy", thisUserId);
        findLayoutItems(view);
        fillTextViews(details);

        if( sameUser){
            buttonRmv.setVisibility(View.GONE);
            buttonResetPassword.setVisibility(View.GONE);
        }
        else{
            dialogRemove = removeAlert(details.get("email"));
            dialogReset = resetAlert(details.get("email"));
            buttonRmv.setOnClickListener(v -> dialogRemove.show());
            buttonResetPassword.setOnClickListener(v -> dialogReset.show());
        }

        buttonEditData.setOnClickListener(v->{
            if(!edit) {
                editData();
                edit = true;
            }
            else {
                updateData();
                edit = false;
            }
        } );
    }

    private AlertDialog resetAlert(final String email) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Zresetuj hasło dla:\n"+email)
                .setTitle("Czy Napewno?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(db.updateDataSQL("Update Konta set haslo = Reset1234 where email ="+email+" and id = "+thisUserId) <= 0)
                    Toast.makeText(getContext(), "Nie udało się zmienić hasła", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        return builder.create();
    }
    private AlertDialog removeAlert(String email) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Usuń użytkownika:\n"+email)
                .setTitle("Czy Napewno?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.deleteUser(thisUserId);
                Intent intent = new Intent(getContext(), UserListActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        return builder.create();
    }

    private void editData(){
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        surname.setInputType(InputType.TYPE_CLASS_TEXT);
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        position.setInputType(InputType.TYPE_NULL);
        pesel.setInputType(InputType.TYPE_CLASS_NUMBER);
        idNum.setInputType(InputType.TYPE_CLASS_TEXT);
        address.setInputType(InputType.TYPE_CLASS_TEXT);
        postal.setInputType(InputType.TYPE_CLASS_NUMBER);

        spinner.setVisibility(View.VISIBLE);
        position.setVisibility(View.INVISIBLE);
        spinner.setSelection(db.positionStringToInt(details.get("stanowisko")));
        buttonEditData.setText(R.string.save_rate);
    }

    private void updateData(){
        boolean passed = true;
        boolean updated = false;
        if(!name.getText().toString().equals(details.get("imie"))){
            updated = true;
            if(db.updateDataSQL("update Pracownicy set imie="+name.getText().toString()+" where id="+thisUserId) < 1)
                passed = false;
        }
        if(!surname.getText().toString().equals(details.get("nazwisko"))){
            updated = true;
            if(db.updateDataSQL("update Pracownicy set nazwisko="+surname.getText().toString()+" where id="+thisUserId) < 1)
                passed = false;
        }
        if(!email.getText().toString().equals(details.get("email"))){
            updated = true;
            if(db.updateDataSQL("update Pracownicy set email="+email.getText().toString()+" where id="+thisUserId) < 1)
                passed = false;
        }
        if(!pesel.getText().toString().equals(details.get("pesel"))){
            updated = true;
            if(db.updateDataSQL("update Pracownicy set pesel="+pesel.getText().toString()+" where id="+thisUserId) < 1)
                passed = false;
        }
        if(!idNum.getText().toString().equals(details.get("nr_dowodu"))){
            updated = true;
            if(db.updateDataSQL("update Pracownicy set nr_dowodu="+idNum.getText().toString()+" where id="+thisUserId) < 1)
                passed = false;
        }
        if(!address.getText().toString().equals(details.get("adres"))){
            updated = true;
            if(db.updateDataSQL("update Pracownicy set adres="+address.getText().toString()+" where id="+thisUserId) < 1)
                passed = false;
        }
        if(!postal.getText().toString().equals(details.get("kod_pocztowy"))){
            updated = true;
            if(db.updateDataSQL("update Pracownicy set kod_pocztowy="+postal.getText().toString()+" where id="+thisUserId) < 1)
                passed = false;
        }
        if(!spinner.getSelectedItem().toString().equals(details.get("stanowisko"))){
            updated = true;
            int pos = db.positionStringToInt(spinner.getSelectedItem().toString());
            if(db.updateDataSQL("update Pracownicy set stanowisko="+pos+" where id="+thisUserId) < 1)
                passed = false;
        }

        if(updated){
            if(passed)
                Toast.makeText(getContext(),"Zaktualizowano dane",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(),"Przy aktualizacji wystąpił błąd",Toast.LENGTH_SHORT).show();
        }


        name.setInputType(InputType.TYPE_NULL);
        surname.setInputType(InputType.TYPE_NULL);
        email.setInputType(InputType.TYPE_NULL);
        position.setInputType(InputType.TYPE_NULL);
        pesel.setInputType(InputType.TYPE_NULL);
        idNum.setInputType(InputType.TYPE_NULL);
        address.setInputType(InputType.TYPE_NULL);
        postal.setInputType(InputType.TYPE_NULL);

        spinner.setVisibility(View.GONE);
        position.setText(spinner.getSelectedItem().toString());
        position.setVisibility(View.VISIBLE);
        buttonEditData.setText(R.string.editdetails);
        details = db.getData("Pracownicy", thisUserId);
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
        spinner = (Spinner)view.findViewById(R.id.spinnerDetailPosition);

        buttonRmv = (Button)view.findViewById(R.id.buttonRemoveAccount);
        buttonResetPassword = (Button)view.findViewById(R.id.buttonResetPasswd);
        buttonEditData = view.findViewById(R.id.buttonEditDetails);

        name.setInputType(InputType.TYPE_NULL);
        surname.setInputType(InputType.TYPE_NULL);
        email.setInputType(InputType.TYPE_NULL);
        position.setInputType(InputType.TYPE_NULL);
        pesel.setInputType(InputType.TYPE_NULL);
        idNum.setInputType(InputType.TYPE_NULL);
        address.setInputType(InputType.TYPE_NULL);
        postal.setInputType(InputType.TYPE_NULL);

        spinner.setVisibility(View.GONE);
    }
}