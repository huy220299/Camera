package com.example.camera.activity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.R;
import com.example.camera.adapter.FolderAdapter;
import com.example.camera.model.Bucket;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PickPhotoActivity extends AppCompatActivity  {
    RecyclerView recyclerView;
    FolderAdapter adapter;
    List<String> listFolder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        recyclerView = findViewById(R.id.recyclerView);
//        listFolder= getListFolder(this);
        adapter=new FolderAdapter(getImageBuckets(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                Uri.parse("package:" + getPackageName()));
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);






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