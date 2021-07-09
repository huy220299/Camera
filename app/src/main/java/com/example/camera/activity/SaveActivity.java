package com.example.camera.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.camera.R;

public class SaveActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ImageView imageView = findViewById(R.id.imageView);

        byte[] bytes = getIntent().getByteArrayExtra("bitmapBytes");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        Bitmap bmp = getIntent().getParcelableExtra("bimapBytes");
        imageView.setImageBitmap(bmp);



    }
}