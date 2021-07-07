package com.example.camera.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.camera.R;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SaveActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        CropImageView imageView = findViewById(R.id.imageView);
        imageView.setCropShape(CropImageView.CropShape.OVAL);

//        Log.d("thunt","path: "+getIntent().getStringExtra("path"));
//        Glide.with(this).load(getIntent().getStringExtra("path")).into(imageView);




        byte[] bytes = getIntent().getByteArrayExtra("bitmapBytes");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bmp);



    }
}