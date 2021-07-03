package com.example.camera;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.model.FilterData;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import org.wysaid.common.Common;
import org.wysaid.nativePort.CGENativeLibrary;
import org.wysaid.view.ImageGLSurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilterDemoAdapter.ItemClickListener {
    public  static String TAG = "MainActivity";

    private String mCurrentConfig;
    public static final String EFFECT_CONFIGS[] = {
            "@adjust lut edgy_amber.png",
            "@adjust lut filmstock.png",
            "@adjust lut foggy_night.png",
            "@adjust lut warm_layer.png",
            "@adjust lut late_sunset.png",
            "@adjust lut foggy_night.png",

            "@adjust lut foggy_night.png",

    };
    ImageGLSurfaceView mImageView;
    RecyclerView recyclerView;
    FilterDemoAdapter adapter;
    List<FilterData> filterDataList;
    Bitmap mBitmap;
    SeekBar seekBar;

    private ImageView btnAddText, btnEditText, btnAddSticker;
    private boolean isVisibility = true;
    private int currentFont;
    StickerView stickerView;

    int[] imageFilterId = {R.drawable.natural, R.drawable.natural1,
            R.drawable.natural, R.drawable.natural1,
            R.drawable.natural, R.drawable.natural1,
            R.drawable.natural, R.drawable.natural1,

           };
    int[] fontList = {R.font.bold,R.font.kaushan_script,R.font.muli_bolditalic,R.font.regular,R.font.toolbar};
    RecyclerView recyclerViewTextStyle;
    TextStyleAdapter adapterTextStyle;
    private ConstraintLayout constraintLayoutTextStyle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        onActionEvent();


    }

    private void onActionEvent() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float intensity = progress / 100.0f;
                mImageView.setFilterIntensity(intensity);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnAddText.setOnClickListener(v -> {
            if (isVisibility){
                recyclerView.setVisibility(View.GONE);
                constraintLayoutTextStyle.setVisibility(View.VISIBLE);
                isVisibility = false;
            }else {
                recyclerView.setVisibility(View.VISIBLE);
                constraintLayoutTextStyle.setVisibility(View.GONE);
                isVisibility =true;
            }

        });

        btnEditText.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Title");

            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("OK", (dialog, which) -> {
                String inputText = input.getText().toString();
                addTextSticker(inputText);
                Log.e("~~~", inputText);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        btnAddSticker.setOnClickListener(v -> {
            Drawable drawable =
                    ContextCompat.getDrawable(this, R.drawable.natural);
            Drawable drawable1 =
                    ContextCompat.getDrawable(this, R.drawable.natural1);
            stickerView.addSticker(new DrawableSticker(drawable));
            stickerView.addSticker(new DrawableSticker(drawable1));

        });

        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                //stickerView.removeAllSticker();
//                if (sticker instanceof TextSticker) {
//                    ((TextSticker) sticker).setTextColor(Color.RED);
//                    stickerView.replace(sticker);
//                    stickerView.invalidate();
//                }
                Log.d(TAG, "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerTouchedDown");
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
            }
        });

    }

    private void initView() {
        btnAddText= findViewById(R.id.btnAddText);
        stickerView= findViewById(R.id.stickerView);
        mImageView = (ImageGLSurfaceView) findViewById(R.id.image);
        btnEditText =  findViewById(R.id.btnEditText);
        btnAddSticker =  findViewById(R.id.btnAddSticker);
        seekBar = (SeekBar) findViewById(R.id.globalRestoreSeekBar);
        constraintLayoutTextStyle =  findViewById(R.id.constraintLayoutTextStyle);


        //set main image
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.natural);
        mImageView.setScaleX(0.8f);
        mImageView.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
            @Override
            public void surfaceCreated() {

                mImageView.setImageBitmap(mBitmap);
                mImageView.setFilterWithConfig(mCurrentConfig);
                mImageView.setFilterIntensity(1.0f);
                CGENativeLibrary.setLoadImageCallback(mLoadImageCallback, null);
            }
        });
        //todo hashcode
//        mCurrentConfig ="@adjust lut late_sunset.png";

        //set up sticker view
        //currently you can config your own icons and icon event
        //the event you can custom
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());

        BitmapStickerIcon heartIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.favorite),
                        BitmapStickerIcon.LEFT_BOTTOM);


        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon, heartIcon));




        //default icon layout
        //stickerView.configDefaultIcons();

        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);

        //Show list filter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        filterDataList = new ArrayList<>();
        filterDataList = getListFilter();
        adapter= new FilterDemoAdapter(filterDataList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Show list text style
        recyclerViewTextStyle = findViewById(R.id.recyclerViewTextStyle);
        adapterTextStyle =new TextStyleAdapter("xin chao", fontList,this)  ;
        recyclerViewTextStyle.setAdapter(adapterTextStyle);
        recyclerViewTextStyle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }
    private void addSticker(){
            stickerView.addSticker(new TextSticker(getApplicationContext()).setText("Hello").resizeText().setMaxTextSize(14));


    }
    private void addTextSticker(String text) {
//        final TextSticker sticker = new TextSticker(this);
//        sticker.setText(text);
//        sticker.setTextColor(Color.BLUE);
//        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
//        sticker.resizeText();
//        Typeface font = ResourcesCompat.getFont(this, currentFont);
//        sticker.setTypeface(font);

//        stickerView.addSticker(sticker);



        TextSticker sticker = new TextSticker(this);
        sticker.setText(text+"");
        sticker.setTextColor(Color.BLUE);
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        sticker.resizeText();
        Typeface font = ResourcesCompat.getFont(this, currentFont);
        sticker.setTypeface(font);


        stickerView.addSticker(sticker);


    }

    private List<FilterData> getListFilter() {
        List<FilterData> list = new ArrayList<>();
        list.add(new FilterData("test1", EFFECT_CONFIGS[0], imageFilterId[0]));
        list.add(new FilterData("test2", EFFECT_CONFIGS[1], imageFilterId[1]));
        list.add(new FilterData("test3", EFFECT_CONFIGS[2], imageFilterId[0]));
        list.add(new FilterData("test4", EFFECT_CONFIGS[3], imageFilterId[1]));
        list.add(new FilterData("test5", EFFECT_CONFIGS[4], imageFilterId[0]));
        list.add(new FilterData("test6", EFFECT_CONFIGS[5], imageFilterId[1]));
        list.add(new FilterData("test7", EFFECT_CONFIGS[6], imageFilterId[0]));
        return list;
    }

    public CGENativeLibrary.LoadImageCallback mLoadImageCallback = new CGENativeLibrary.LoadImageCallback() {

        //Notice: the 'name' passed in is just what you write in the rule, e.g: 1.jpg
        //注意， 这里回传的name不包含任何路径名， 仅为具体的图片文件名如 1.jpg
        @Override
        public Bitmap loadImage(String name, Object arg) {

            Log.i(Common.LOG_TAG, "Loading file: " + name);
            AssetManager am = getAssets();
            InputStream is;
            try {
                is = am.open(name);
            } catch (IOException e) {
                Log.e(Common.LOG_TAG, "Can not open file " + name);
                return null;
            }

            return BitmapFactory.decodeStream(is);
        }

        @Override
        public void loadImageOK(Bitmap bmp, Object arg) {
            Log.i(Common.LOG_TAG, "Loading bitmap over, you can choose to recycle or cache");

            //The bitmap is which you returned at 'loadImage'.
            //You can call recycle when this function is called, or just keep it for further usage.

            bmp.recycle();
        }
    };

    @Override
    public void onClick(View view, int position) {
        try {
            currentFont = fontList[position];
        }catch (Exception e){


        }
        mCurrentConfig = EFFECT_CONFIGS[position];
        Bitmap newBitmap =mark(mBitmap,position+" ");
        mImageView.setImageBitmap(newBitmap);
        mImageView.setFilterWithConfig(mCurrentConfig);


    }

    public static Bitmap mark(Bitmap src, String watermark) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(18);
        paint.setAntiAlias(true);
//        paint.setUnderlineText(true);
        canvas.drawText(watermark, 20, 25, paint);

        return result;
    }
}