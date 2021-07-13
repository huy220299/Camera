package com.example.camera.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.camera.R;
import com.example.camera.dialog.CustomBackDialog;
import com.example.camera.ultis.BitmapUlti;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropImageActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout ratioDefault, ratio11, ratio43, ratio169, shapeRectangle, shapeOval, showGrid, btnRotate;
    private RelativeLayout btnBack, btnDone;
    CropImageView imageView;
    Bitmap bmp;
    private boolean isOval=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        btnDone = findViewById(R.id.btnDone);
        btnBack = findViewById(R.id.btnBack);


        btnRotate = findViewById(R.id.btnRotate);
        ratioDefault = findViewById(R.id.ratioDefault);
        ratio11 = findViewById(R.id.ratio11);
        ratio43 = findViewById(R.id.ratio43);
        ratio169 = findViewById(R.id.ratio169);
        shapeRectangle = findViewById(R.id.shapeRectangle);
        shapeOval = findViewById(R.id.shapeOval);
        showGrid = findViewById(R.id.showGrid);
        imageView = findViewById(R.id.imageView);

//        SharedPreferences preferences = getSharedPreferences("test",MODE_PRIVATE);
//        String image = preferences.getString("Image", "");
//
//        byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
//        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
//        Log.e("~~~", bmp.getWidth()+"");
//        imageView.setImageBitmap(bmp);
        new setBitmapImage().execute();

        btnRotate.setOnClickListener(this);
        ratioDefault.setOnClickListener(this);
        ratio11.setOnClickListener(this);
        ratio43.setOnClickListener(this);
        ratio169.setOnClickListener(this);
        shapeRectangle.setOnClickListener(this);
        shapeOval.setOnClickListener(this);
        showGrid.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    }
    class setBitmapImage extends AsyncTask<Void, Void, Void> {
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences preferences = getSharedPreferences("test",MODE_PRIVATE);
            String image = preferences.getString("Image", "");

            byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
             bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postAtTime(() -> {
              imageView.setImageBitmap(bmp);

            }, 500);

        }
    }

    @Override
    public void onClick(View v) {
        Bitmap bm = imageView.getCroppedImage();
        Bitmap bitmapOval= BitmapUlti.createOvalBitmap(bm);
        switch (v.getId()){
            case R.id.ratioDefault:
                imageView.clearAspectRatio();
                break;
            case R.id.ratio11:
                imageView.setAspectRatio(1,1);
                break;
            case R.id.ratio43:
                imageView.setAspectRatio(4,3);
                break;
            case R.id.ratio169:
                imageView.setAspectRatio(16,9);
                break;
            case R.id.shapeRectangle:
                isOval =false;
                imageView.setCropShape(CropImageView.CropShape.RECTANGLE);
                break;
            case R.id.shapeOval:
                isOval=true;
                imageView.setCropShape(CropImageView.CropShape.OVAL);
                break;
            case R.id.showGrid:
                if (imageView.getGuidelines()== CropImageView.Guidelines.OFF){
                    imageView.setGuidelines(CropImageView.Guidelines.ON);
                }else {
                    imageView.setGuidelines(CropImageView.Guidelines.OFF);
                }
                break;
            case R.id.btnRotate:
                imageView.rotateImage(90);
                break;
            case R.id.btnDone:
                String encoded;
                if (isOval){
                    encoded= Base64.encodeToString(BitmapUlti.convertToArray(bitmapOval), Base64.DEFAULT);
                }
                else {
                     encoded = Base64.encodeToString(BitmapUlti.convertToArray(bm), Base64.DEFAULT);
                }

                SharedPreferences preferences = getSharedPreferences("test", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Image", encoded);
                editor.apply();
                startActivity(new Intent(CropImageActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.btnBack:
                onBackPressed();
                break;

            default:
                break;


        }
    }

    @Override
    public void onBackPressed() {
        CustomBackDialog cdd=new CustomBackDialog(CropImageActivity.this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cdd.show();
    }
}