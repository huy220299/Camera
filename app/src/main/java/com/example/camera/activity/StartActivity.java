package com.example.camera.activity;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.camera.R;
import com.example.camera.dialog.CustomBackDialog;

public class StartActivity extends BaseActivity {
    public static int REQUEST_CODE = 123;

    private RelativeLayout btnCamera, btnPhoto;

    private ImageView background1;
    private ConstraintLayout constraintLayoutPermission;
    public static int REQUEST_PERMISSION_CODE = 1233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.textViewPermission).setSelected(true);

        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                setSharedPreferences(1);
            }
        }

        btnPhoto = findViewById(R.id.btnPhoto);
        background1 = findViewById(R.id.background1);
        constraintLayoutPermission = findViewById(R.id.constraintLayout_permission);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_banner);
        background1.setImageBitmap(bitmap);
        SharedPreferences preferences = getSharedPreferences("test",MODE_PRIVATE);
        int permission = preferences.getInt("permission",-1);
        if (permission==1||permission==-1){
            constraintLayoutPermission.setVisibility(View.GONE);
        }else {
            constraintLayoutPermission.setVisibility(View.VISIBLE);
        }

        constraintLayoutPermission.setOnClickListener(v -> openSettingPermission());
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

    @Override
    public void onBackPressed() {
        CustomBackDialog cdd=new CustomBackDialog(StartActivity.this,"Do you want to quit ?");
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cdd.show();
    }

    private void checkPermissions(){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return;
        }
        if (checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            setSharedPreferences(1);
           startActivity(new Intent(StartActivity.this, PickPhotoActivity.class));
        }else {
            String[] permissions= {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,REQUEST_PERMISSION_CODE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
               setSharedPreferences(1);
                startActivity(new Intent(StartActivity.this, PickPhotoActivity.class));
            }else{
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                        ||shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ){
                    constraintLayoutPermission.setVisibility(View.VISIBLE);
                    setSharedPreferences(0);
                }


            }
        }
    }
    private void openSettingPermission(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        startActivity(intent);
    }
    private void setSharedPreferences(int i){
        SharedPreferences preferences = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("permission", i);
        editor.apply();
    }
}