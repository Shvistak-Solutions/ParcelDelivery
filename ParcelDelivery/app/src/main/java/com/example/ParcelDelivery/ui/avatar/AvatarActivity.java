package com.example.ParcelDelivery.ui.avatar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class AvatarActivity extends AppCompatActivity {

    int usrID;

    Class cls;
    int control;
    // control falg values:
    // 0 - user has no avatar
    // 1 - user has avatar
    // 2 - user has changed avatar

    private ImageView selectedImageView;
    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private DatabaseHelper dbH;
    Bitmap imageBitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        //========================= SETUP
        usrID = getIntent().getIntExtra("userId", 0);
        Bundle extras = getIntent().getExtras();

        String classname = extras.getString("class");
        try {
            cls = Class.forName(classname);
            } catch(ClassNotFoundException ex) {
            System.out.println(ex.toString());
        }

        selectedImageView = (ImageView)findViewById(R.id.ID_AVATAR_PICTURE);
        dbH = new DatabaseHelper(this);

        printAvatar();

        //======================== BUTTONS
        Button gallery = (Button)findViewById(R.id.ID_GALLERY);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Button camera = (Button)findViewById(R.id.ID_CAMERA);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        Button save =  (Button)findViewById(R.id.ID_SAVE_AVATAR);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAvatar();
            }
        });

        Button delete =  (Button)findViewById(R.id.ID_CLEAR_AVATAR);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAvatar();
            }
        });


        // back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                Intent intent = new Intent(AvatarActivity.this,cls);
                intent.putExtra("userId", usrID);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);


    }


    //================================ FUNCTIONS
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Wybierz z galrii"), GALLERY_REQUEST_CODE);
    }

    public void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                selectedImageView.setImageBitmap(convertToSquare(BitmapFactory.decodeStream(imageStream)));
                imageBitmap = ((BitmapDrawable)selectedImageView.getDrawable()).getBitmap();
               //dbH.updateAvatar(usrID,bitmapToString(imageBitmap));
                control = 2;

            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }

        if( resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE){
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap)extras.get("data");
            selectedImageView.setImageBitmap(convertToSquare(image));
            imageBitmap = ((BitmapDrawable)selectedImageView.getDrawable()).getBitmap();
            //dbH.updateAvatar(usrID,bitmapToString(imageBitmap));
            control = 2;
        }
    }

    private static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }


    public void saveAvatar(){
        if (control == 2)
        {
            dbH.updateAvatar(usrID,bitmapToString(imageBitmap));
            Toast.makeText(this,"Zdjęcie zostało zmienione",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Nie wybrono nowego zdjęcia",Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAvatar(){
        if (control != 0){
            dbH.deleteProfilePicture(usrID);
            Toast.makeText(this,"Usinuęto zdjęcie",Toast.LENGTH_SHORT).show();
            control = 0;
            printAvatar();
        }
        else{
            Toast.makeText(this,"Nie masz zdjęcia profilowego",Toast.LENGTH_SHORT).show();
        }
    }

    public void printAvatar(){
        int avatarExists = dbH.doesUserHasAvatar(usrID);
        if( avatarExists == 0){
            selectedImageView.setImageResource(R.drawable.avatar0);
            control = 0;
        }
        else{
            imageBitmap = dbH.getAvatarAsBitmap(usrID);
            selectedImageView.setImageBitmap(imageBitmap);
            control = 1;
        }

    }

    public static Bitmap convertToSquare(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

}