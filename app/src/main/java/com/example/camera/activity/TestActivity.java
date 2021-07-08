package com.example.camera.activity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.camera.R;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity  {
    ArrayList<Button> mDemos;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);

        LinearLayout mLayout =  findViewById(R.id.buttonLayout);


        List<Bucket> list = getImageBuckets(this);
        Log.e("~~~", new Gson().toJson(list));





    }
    public class Bucket {
        String name, imagePath;
        public Bucket(String name, String imagePath){
            this.name = name;
            this.imagePath = imagePath;
        }
    }


    public  List<Bucket> getImageBuckets(Context mContext){
        List<Bucket>  buckets = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA};

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        ArrayList<String> listPath = new ArrayList<>();
        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                String bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]));
                String firstImage = cursor.getString(cursor.getColumnIndex(projection[1]));
                file = new File(firstImage);
                if (file.exists() && !listPath.contains(bucketPath)) {
                    buckets.add(new Bucket(bucketPath, firstImage));
                    listPath.add(bucketPath);
                }
            }
            cursor.close();
        }
        if (buckets.isEmpty()){

        }
        return buckets;
    }




}