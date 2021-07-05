package com.example.camera;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.fragment.BottomSheetAddSticker;
import com.example.camera.fragment.BottomSheetAddText;
import com.example.camera.model.FilterData;
import com.example.camera.ultis.FileUtil;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;


import org.wysaid.nativePort.CGENativeLibrary;
import org.wysaid.view.ImageGLSurfaceView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilterDemoAdapter.ItemClickListener, BottomSheetAddText.Callback, BottomSheetAddSticker.Callback {
    public  static String TAG = "MainActivity";

    private String mCurrentConfig;
    public static final String[] EFFECT_CONFIGS = {
            "@adjust lut 001.jpg",
            "@adjust lut 002.jpg",
            "@adjust lut 003.jpg",
            "@adjust lut 004.jpg",
            "@adjust lut 005.jpg",
            "@adjust lut 006.jpg",
            "@adjust lut 007.jpg",
            "@adjust lut 008.jpg",
            "@adjust lut 008.jpg",
            "@adjust lut 009.jpg",
            "@adjust lut 010.jpg",

    };

    ImageGLSurfaceView mImageView;
    RecyclerView recyclerView;
    FilterDemoAdapter adapter;
    List<FilterData> filterDataList;
    Bitmap mBitmap;
    SeekBar seekBar;
    private RelativeLayout btnSave, btnBack;
    private ImageView photoView;

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


    BottomSheetAddText bottomSheetAddText;
    BottomSheetAddSticker bottomSheetAddSticker;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        initView();
        onActionEvent();


    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    private void onActionEvent() {
        btnSave.setOnClickListener(v -> new getBitmapFiltered().execute());
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

            Bundle bundle = new Bundle();
            bottomSheetAddText = new BottomSheetAddText();
            bottomSheetAddText.show(getSupportFragmentManager(), "ModalBottomSheet");
            bottomSheetAddText.setArguments(bundle);

        });

        btnAddSticker.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bottomSheetAddSticker = new BottomSheetAddSticker();
            bottomSheetAddSticker.show(getSupportFragmentManager(), "ModalBottomSheet");
            bottomSheetAddSticker.setArguments(bundle);

        });

    }

    Bitmap bm;
    class getBitmapFiltered extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
//            bm = mImageView.getBitmapData();

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            photoView.setImageBitmap(bm);
            Handler handler = new Handler();
            handler.postAtTime(new Runnable() {
                @Override
                public void run() {
                    File file = FileUtil.getNewFile(MainActivity.this, "VintageCamera");
                    if (file != null) {
                        stickerView.save(file);
                        Toast.makeText(MainActivity.this, "saved in " + file.getAbsolutePath(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "the file is null", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 500);
        }
    }

    private void initView() {
        photoView = findViewById(R.id.photoView);
        btnAddText= findViewById(R.id.btnAddText);
        btnSave= findViewById(R.id.btnSave);
        btnBack= findViewById(R.id.btnBack);
        stickerView= findViewById(R.id.stickerView);
        mImageView = findViewById(R.id.image);

        btnAddSticker =  findViewById(R.id.btnAddSticker);
        seekBar = findViewById(R.id.globalRestoreSeekBar);


        //set main image
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample1);
        mImageView.setSurfaceCreatedCallback(() -> {

            mImageView.setImageBitmap(mBitmap);
            mImageView.setFilterWithConfig(mCurrentConfig);
            mImageView.setFilterIntensity(1.0f);
            CGENativeLibrary.setLoadImageCallback(mLoadImageCallback, null);
        });
        mImageView.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT);


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




        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon));








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
//        recyclerViewTextStyle = findViewById(R.id.recyclerViewTextStyle);
//        adapterTextStyle =new TextStyleAdapter("xin chao", fontList,this)  ;
//        recyclerViewTextStyle.setAdapter(adapterTextStyle);
//        recyclerViewTextStyle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }
    private void addTextSticker(String text, String textStyle,int align, String stringColor) {
        TextSticker sticker = new TextSticker(this);
        sticker.setText(text);
        sticker.setTextColor(Color.parseColor(stringColor));
        if (align==1){
            sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        }else if (align==2){
            sticker.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
        }else {
            sticker.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
        }
        sticker.resizeText();
        Typeface font = Typeface.createFromAsset(getAssets(),"font/"+ textStyle);
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
        @Override
        public Bitmap loadImage(String name, Object arg) {


            AssetManager am = getAssets();

            InputStream is;
            try {
//                is = am.open(name);
                is = am.open("lut/"+name);
                Log.e("~~~",name);
            } catch (IOException e) {

                return null;
            }

            return BitmapFactory.decodeStream(is);
        }

        @Override
        public void loadImageOK(Bitmap bmp, Object arg) {


            //The bitmap is which you returned at 'loadImage'.
            //You can call recycle when this function is called, or just keep it for further usage.

            bmp.recycle();
        }
    };

    @Override
    public void onClick(View view, int position, String type) {
        try {
            currentFont = fontList[position];
        }catch (Exception ignored){


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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onButtonClicked(View view, Bundle bundle) {

       switch (view.getId()){
           case R.id.btnCancel:
               bottomSheetAddText.dismiss();
               break;
           case R.id.btnDone:
               String text = bundle.getString("text","");
               String textStyle = bundle.getString("textStyle","regular.ttf");
               String stringColor = bundle.getString("textColor","#000000");
               int align = bundle.getInt("align",1);

               if (!text.equals("")){
                   addTextSticker(text, textStyle,align, stringColor);
               }
                bottomSheetAddText.dismiss();

       }

    }
    public List<String> getNameFilterFromAssets(Context context) {
        List<String> list = new ArrayList<>();
        AssetManager assetManager;
        try {
            assetManager = context.getAssets();
            list = Arrays.asList(assetManager.list("sticker"));
        } catch (IOException e) {

        }
        return list;
    }
    private List<String> getListRules(List<String> list){
        List<String> listRules = new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            listRules.add("@adjust lut "+list.get(i)) ;
        }
        return listRules;
    }
}