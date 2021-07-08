//package com.example.camera.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.camera.model.Butket;
//import com.hidephoto.hidevideo.lockphoto.secretphoto.safe.free.FolderAdapter;
//import com.hidephoto.hidevideo.lockphoto.secretphoto.safe.free.R;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ImportPhotos extends AppCompatActivity{
//
//    private static final String TAG = "tag";
//    private File[] listFile;
//    private RecyclerView rcvImportPhotos;
//    private ButketAdapter butketAdapter;
//    private static ArrayList<Butket> buckets;
//    private String folder_name;
//
//    private RecyclerView rcvPhotoInFolder;
//    private PhotoAdapter photoAdapter;
//    private ArrayList<Photo> listPhoto = new ArrayList<Photo>();
//
//    private TextView countImageSelected;
//    private RecyclerView rcvImageSelected;
//    private ImageSelectedAdapter imageSelectedAdapter;
//    private ArrayList<ImageSelected> listImageSelected = new ArrayList<ImageSelected>();
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_import_photos);
//
//        Intent intent = getIntent();
//        folder_name = intent.getStringExtra("FOLDER_NAME_1");
//
//        rcvImportPhotos = findViewById(R.id.rcvImportPhotos);
//        countImageSelected = findViewById(R.id.countImageSelected);
//        rcvImageSelected = findViewById(R.id.rcvImageSelected);
//        rcvPhotoInFolder = findViewById(R.id.rcvPhotoInFolder);
//
//
//        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(ImportPhotos.this,RecyclerView.VERTICAL,false);
//        rcvImportPhotos.setLayoutManager(linearLayoutManager);
//        butketAdapter= new ButketAdapter(this);
//        butketAdapter.setData(getImageBuckets(this));
//        rcvImportPhotos.setAdapter(butketAdapter);
//        rcvImportPhotos.setHasFixedSize(true);
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
//        rcvPhotoInFolder.setLayoutManager(gridLayoutManager);
//        photoAdapter = new PhotoAdapter(ImportPhotos.this,listPhoto);
//        rcvPhotoInFolder.setHasFixedSize(true);
//
//        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
//        rcvImageSelected.setLayoutManager(linearLayoutManager1);
//        imageSelectedAdapter = new ImageSelectedAdapter(ImportPhotos.this, listImageSelected);
//        imageSelectedAdapter.setData(listImageSelected);
//        rcvImageSelected.setAdapter(imageSelectedAdapter);
//        rcvImageSelected.setHasFixedSize(true);
//
//
//        imageSelectedAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                for (int i=0; i<listPhoto.size(); i++){
//                    if (listPhoto.get(i).getPathPhoto().equals(listImageSelected.get(position).getPathImageSelected())){
//                        listPhoto.get(i).setChecked(false);
//                    }
//                }
//                listImageSelected.remove(position);
//                countImageSelected.setText(listImageSelected.size()+" Image Selected");
//
//                imageSelectedAdapter.setData(listImageSelected);
//                photoAdapter.setData(listPhoto);
//
//                rcvImageSelected.setAdapter(imageSelectedAdapter);
//                rcvPhotoInFolder.setAdapter(photoAdapter);
//
//            }
//        });
//
//        photoAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                ImageSelected imageSelected = new ImageSelected(listPhoto.get(position).getPathPhoto());
//                if (listPhoto.get(position).isChecked == false){
//                    listImageSelected.add(imageSelected);
//                }else {
//                    for (int i = 0; i<listImageSelected.size(); i++){
//                        if (listImageSelected.get(i).getPathImageSelected().equals(listPhoto.get(position).getPathPhoto())){
//                            listImageSelected.remove(i);
//                            break;
//                        }
//                    }
//                }
//                countImageSelected.setText(listImageSelected.size()+" Image Selected");
//                imageSelectedAdapter.setData(listImageSelected);
//                rcvImageSelected.setAdapter(imageSelectedAdapter);
//            }
//        });
//
//
//        butketAdapter.setOnItemClickListener(new FolderAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                if (buckets.isEmpty()){
//                    return;
//                }
//                Butket butket = buckets.get(position);
//                String pathFolder= butket.getFirstImageContainedPath().substring(0,butket.getFirstImageContainedPath().lastIndexOf("/"));
//                File file = new File(pathFolder);
//                loadDataPhotoAdapter(file);
//            }
//        });
//
//    }
//
//    private void loadDataPhotoAdapter(File file) {
//        if (file.isDirectory()){
//            listFile = file.listFiles();
//            ArrayList<Photo> listPhoto1 = new ArrayList<Photo>();
//            for (int i=0; i<listFile.length; i++){
//                String path= listFile[i].getAbsolutePath();
//                if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")){
//                    listPhoto1.add(new Photo(path,false));
//                }
//            }
//            listPhoto=listPhoto1;
//            if (listImageSelected.size()>0){
//                for (int i=0; i<listPhoto.size(); i++){
//                    for (int j=0; j<listImageSelected.size(); j++){
//                        if (listPhoto.get(i).getPathPhoto().equals(listImageSelected.get(j).getPathImageSelected())){
//                            listPhoto.get(i).setChecked(true);
//                        }
//                    }
//                }
//            }
//
//            if (listPhoto.isEmpty()){
//                Toast.makeText(ImportPhotos.this,"Have no image in this folder",Toast.LENGTH_SHORT).show();
//            }else {
//                rcvImportPhotos.setVisibility(View.GONE);
//                rcvPhotoInFolder.setVisibility(View.VISIBLE);
//            }
//            photoAdapter.setData(listPhoto);
//            rcvPhotoInFolder.setAdapter(photoAdapter);
//        }
//    }
//
//
//    public static List<Butket> getImageBuckets(Context mContext){
//        buckets = new ArrayList<>();
//        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        String [] projection = {MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA};
//
//        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
//        ArrayList<String> listPath = new ArrayList<>();
//        if(cursor != null){
//            File file;
//            while (cursor.moveToNext()){
//                String bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]));
//                String fisrtImage = cursor.getString(cursor.getColumnIndex(projection[1]));
//                file = new File(fisrtImage);
//                if (file.exists() && !listPath.contains(bucketPath)) {
//                    buckets.add(new Butket(bucketPath, fisrtImage));
//                    listPath.add(bucketPath);
//                }
//            }
//            cursor.close();
//        }
//        if (buckets.isEmpty()){
//            Log.d(TAG, "savedInstanceState is null");
//        }
//        return buckets;
//    }
//
//    public void clickBack(View view){
//        if (rcvPhotoInFolder.getVisibility()==View.VISIBLE){
//            rcvPhotoInFolder.setVisibility(View.GONE);
//            rcvImportPhotos.setVisibility(View.VISIBLE);
//        }else {
//            finish();
//        }
//    }
//
//    public void tick(View view){
//        if (listImageSelected.size()>0){
////            StringBuilder stringBuilder = new StringBuilder();
////            for (int i=0; i< listImageSelected.size(); i++){
////                stringBuilder.append(listImageSelected.get(i).getPathImageSelected());
////                stringBuilder.append("\n");
////            }
////            Toast.makeText(ImportPhotos.this,stringBuilder.toString().trim(),Toast.LENGTH_SHORT).show();
//            for (int i=0; i<listImageSelected.size(); i++){
//                String fileName = String.format("%s.txt",listImageSelected.get(i).getPathImageSelected());
//                String fileName1 = fileName.replaceAll("/","&");
//                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/.HidePhoto/.Photo/"+folder_name;
//                File outFile = new File(path,fileName1);
//                Uri uri = Uri.fromFile(new File(listImageSelected.get(i).getPathImageSelected()));
//                try {
//                    InputStream inputStream = getContentResolver().openInputStream(uri);
//                    FileOutputStream fileOutputStream = new FileOutputStream(outFile);
//                    copyStream(inputStream,fileOutputStream);
//                    fileOutputStream.close();
//                    inputStream.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                File file = new File(listImageSelected.get(i).getPathImageSelected());
//                file.delete();
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
//            }
//            finish();
//        }else {
//            Toast.makeText(ImportPhotos.this,"No photo selected",Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//    @Override
//    public void onBackPressed() {
//        if (rcvImportPhotos.getVisibility() == View.VISIBLE){
//            super.onBackPressed();
//            listPhoto=null;
//            photoAdapter.setData(listPhoto);
//        }else {
//            rcvImportPhotos.setVisibility(View.VISIBLE);
//            rcvPhotoInFolder.setVisibility(View.GONE);
//        }
//    }
//
//    public static void copyStream(InputStream input, OutputStream output)
//            throws IOException {
//
//        byte[] buffer = new byte[1024];
//        int bytesRead;
//        while ((bytesRead = input.read(buffer)) != -1) {
//            output.write(buffer, 0, bytesRead);
//        }
//    }
//}