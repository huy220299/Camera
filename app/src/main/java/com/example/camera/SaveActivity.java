package com.example.camera;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ImageView imageView = findViewById(R.id.imageView);

        Log.d("thunt","path: "+getIntent().getStringExtra("path"));
        Glide.with(this).load(getIntent().getStringExtra("path")).into(imageView);
    }
}