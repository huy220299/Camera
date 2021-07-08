package com.example.camera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.example.camera.R;
import com.example.camera.ultis.BitmapUlti;
import com.example.camera.ultis.FileUtil;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class CropImageActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout ratioDefault, ratio11, ratio43, ratio169, shapeRectangle, shapeOval, showGrid, btnRotate;
    private RelativeLayout btnBack, btnDone;
    CropImageView imageView;
    Bitmap bmp;

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

        byte[] bytes = getIntent().getByteArrayExtra("bitmapBytes");
        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


        imageView.setImageBitmap(bmp);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Bitmap bm = imageView.getCroppedImage();

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
                imageView.setCropShape(CropImageView.CropShape.RECTANGLE);
                break;
            case R.id.shapeOval:
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

                try {
                    FileUtil.saveImage(CropImageActivity.this,bm,"test");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnBack:
                Intent intent = new Intent();

                intent.putExtra("resultBitmap",BitmapUlti.convertToArray(bm));
                setResult(RESULT_OK,intent);
                finish();
                break;

            default:
                break;


        }
    }
}