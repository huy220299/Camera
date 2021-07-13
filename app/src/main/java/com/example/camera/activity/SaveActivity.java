package com.example.camera.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.camera.R;
import com.example.camera.ultis.BitmapUlti;
import com.example.camera.ultis.FileUtil;

import java.io.IOException;

public class SaveActivity extends BaseActivity {
    private RelativeLayout btnHome, btnSave, btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ImageView imageView = findViewById(R.id.imageView);
        btnHome = findViewById(R.id.btnHome);
        btnSave = findViewById(R.id.btnSave);
        btnShare = findViewById(R.id.btnShare);
        SharedPreferences preferences = getSharedPreferences("test",MODE_PRIVATE);
        String image = preferences.getString("Image", "");

        byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        imageView.setImageBitmap(bmp);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(new Intent(SaveActivity.this,StartActivity.class));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        btnSave.setOnClickListener(v -> {
            try {
                FileUtil.saveImage(this,bmp,String.valueOf(System.currentTimeMillis() % 1000));
            } catch (IOException e) {
                Toast.makeText(this,"Save failed",Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        });
        btnShare.setOnClickListener(v -> {
            BitmapUlti.shareImageUri(this,BitmapUlti.saveImageToShare(this,bmp));
        });



    }
}