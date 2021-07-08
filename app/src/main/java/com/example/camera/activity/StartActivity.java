package com.example.camera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.R;
import com.example.camera.ultis.BitmapUlti;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends BaseActivity {
    public static int REQUEST_CODE = 123;
    private RelativeLayout btnCamera, btnPhoto;
    RecyclerView rcvBanner;
    List<String> urls;
    int[] listBanner = {
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
            R.drawable.banner4,
            R.drawable.banner6,
            R.drawable.banner7,
            R.drawable.banner8,
            R.drawable.banner9,
            R.drawable.banner10,
    };
    Timer timer;
    TimerTask timerTask;
    int position;
    LinearLayoutManager layoutManager;
    private ImageView background1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnPhoto = findViewById(R.id.btnPhoto);
        background1 = findViewById(R.id.background1);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_banner);

        background1.setImageBitmap(BitmapUlti.rotateBitmap(bitmap));

        btnPhoto.setOnClickListener(v -> {
//            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(gallery, REQUEST_CODE);

            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_CODE);
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Uri myUri = data.getData();
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myUri);
//
//            ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), myUri);
//            Bitmap myBitmap = ImageDecoder.decodeBitmap(source);


            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("imageUri", myUri.toString());
            startActivity(intent);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }





}