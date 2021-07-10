package com.example.camera.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.example.camera.R;

public class SaveActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ImageView imageView = findViewById(R.id.imageView);
        SharedPreferences preferences = getSharedPreferences("test",MODE_PRIVATE);
        String image = preferences.getString("Image", "");

        byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        imageView.setImageBitmap(bmp);



    }
}