package com.example.camera.activity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.R;
import com.example.camera.adapter.FolderAdapter;
import com.example.camera.model.Butket;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity  {
    RecyclerView recyclerView;
    FolderAdapter adapter;
    List<String> listFolder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        recyclerView = findViewById(R.id.recyclerView);
        listFolder= getListFolder(this);
        adapter=new FolderAdapter(getImageBuckets(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));



        Butket butket = getImageBuckets(this).get(0);
        String pathFolder= butket.getFirstImageContainedPath().substring(0,butket.getFirstImageContainedPath().lastIndexOf("/"));
        File file = new File(pathFolder);
        List<String> list1 =loadDataPhotoAdapter(file);
        Log.e("~~~", new Gson().toJson(list1));





    }
    public class Bucket {
        String name, imagePath;
        public Bucket(String name, String imagePath){
            this.name = name;
            this.imagePath = imagePath;
        }
    }

    private List<String> loadDataPhotoAdapter(File file) {
        File[] listFile;
        List<String> listPhoto1 = new ArrayList<>();
        if (file.isDirectory()){

            listFile = file.listFiles();
            for (int i=0; i<listFile.length; i++){
                String path= listFile[i].getAbsolutePath();
                if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")){
                    listPhoto1.add(path);
                }
            }

        }
        return listPhoto1;
    }
    public  List<Butket> getImageBuckets(Context mContext){
        List<Butket>  buckets = new ArrayList<>();
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
                    buckets.add(new Butket(bucketPath, firstImage));
                    listPath.add(bucketPath);
                }
            }
            cursor.close();
        }
        if (buckets.isEmpty()){

        }
        return buckets;
    }
    public  List<String> getListFolder(Context mContext){
        List<String>  listFolder = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA};

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);

        if(cursor != null){

            while (cursor.moveToNext()){
                String strFolder = cursor.getString(cursor.getColumnIndex(projection[0]));
               listFolder.add(strFolder);
            }
            cursor.close();
        }

        return listFolder;
    }




}