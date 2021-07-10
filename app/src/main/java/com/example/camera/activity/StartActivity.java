package com.example.camera.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.camera.R;
import com.example.camera.ultis.BitmapUlti;
import com.example.camera.ultis.Common;
import com.google.gson.Gson;

public class StartActivity extends BaseActivity {
    public static int REQUEST_CODE = 123;

    private RelativeLayout btnCamera, btnPhoto;

    private ImageView background1;
    public static int REQUEST_PERMISSION_CODE = 1233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnPhoto = findViewById(R.id.btnPhoto);
        background1 = findViewById(R.id.background1);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_banner);

        background1.setImageBitmap(BitmapUlti.rotateBitmap(bitmap,15));


        btnPhoto.setOnClickListener(v -> {
            checkPermissions();
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void checkPermissions(){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return;
        }
        if (checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
           startActivity(new Intent(StartActivity.this, PickPhotoActivity.class));
        }else {
            String[] permissions= {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,REQUEST_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(StartActivity.this,"Permission granted",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StartActivity.this, PickPhotoActivity.class));
            }else{
                Toast.makeText(StartActivity.this,"Permission denied",Toast.LENGTH_SHORT).show();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    finishAndRemoveTask();
//
//                }
            }
        }
    }
}