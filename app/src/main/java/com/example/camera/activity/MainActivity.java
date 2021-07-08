package com.example.camera.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.R;
import com.example.camera.adapter.FilterDemoAdapter;
import com.example.camera.fragment.BottomSheetAddSticker;
import com.example.camera.fragment.BottomSheetAddText;
import com.example.camera.fragment.DetailPackStickerFragment;
import com.example.camera.model.FilterData;
import com.example.camera.ultis.BitmapUlti;
import com.example.camera.ultis.Common;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import org.wysaid.nativePort.CGENativeLibrary;
import org.wysaid.view.ImageGLSurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MainActivity extends BaseActivity implements FilterDemoAdapter.ItemClickListener, BottomSheetAddText.Callback, DetailPackStickerFragment.CallbackSticker {
    public  static String TAG = "MainActivity";

    private String mCurrentConfig;
    protected static final String BASIC_FILTER_CONFIG = "@adjust brightness 0 @adjust contrast 1 @adjust saturation 1 @adjust sharpen 0";
    public static final String[] EFFECT_CONFIGS = {
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
    Bitmap mBitmap, bitmapFiltered;
    private SeekBar seekBarFilter, seekBarBrightness, seekBarContrast, seekBarSaturation;
    private int currentBright=50, currentContrast=10, currentSaturation=0;
    private TextView tvBrightness, tvContrast, tvSaturation;
    private RelativeLayout btnSave, btnBack, btnMultiChange;
    private ImageView photoView;
    private LinearLayout linearLayoutMultiChange;

    private RelativeLayout btnAddText, btnAddSticker, btnCrop,btnAddFilter, btnAddDate;

    private boolean isVisibility = true;
    private int currentFont;
    private Bitmap bm;
    private Drawable addedSticker;
    private StickerView stickerView;

    BottomSheetAddText bottomSheetAddText;
    BottomSheetAddSticker bottomSheetAddSticker;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    private void seekBarChange(){

        seekBarFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvBrightness.setText(String.valueOf(progress));

                Bitmap bitmap = BitmapUlti.adjustedContrast(bitmapFiltered,currentContrast);
                bitmap = BitmapUlti.adjustSaturation(bitmap,currentSaturation);
                bitmap = BitmapUlti.adjustBrightness(bitmap,progress);
                Log.e("~~~",progress+"_______"+ currentContrast+"_______"+currentSaturation);
                mImageView.setImageBitmap(bitmap);

                currentBright = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvContrast.setText(String.valueOf(progress));

                Bitmap bitmap = BitmapUlti.adjustBrightness(bitmapFiltered,currentBright);
                bitmap = BitmapUlti.adjustSaturation(bitmap,currentSaturation);
                bitmap = BitmapUlti.adjustedContrast(bitmap,progress);
                Log.e("~~~",progress+"_______"+ currentContrast+"_______"+currentSaturation);
                mImageView.setImageBitmap(bitmap);

                currentContrast =progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSaturation.setText(String.valueOf(progress));

                Bitmap bitmap = BitmapUlti.adjustedContrast(bitmapFiltered,currentContrast);
                bitmap = BitmapUlti.adjustBrightness(bitmap,currentBright);
                bitmap = BitmapUlti.adjustSaturation(bitmap,progress);
                Log.e("~~~",progress+"_______"+ currentContrast+"_______"+currentSaturation);
                mImageView.setImageBitmap(bitmap);

                currentSaturation =progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void onActionEvent() {
        seekBarChange();

        btnMultiChange.setOnClickListener(v ->
        {
            bitmapFiltered = mImageView.getBitmapData();
            if (isVisibility){
                isVisibility=false;
                mImageView.setImageBitmap(mImageView.getBitmapData());
                mImageView.setFilterWithConfig(BASIC_FILTER_CONFIG);
                seekBarFilter.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                linearLayoutMultiChange.setVisibility(View.VISIBLE);
            }else {
                isVisibility=true;
                seekBarFilter.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                linearLayoutMultiChange.setVisibility(View.GONE);
            }

        });
        btnAddFilter.setOnClickListener(v -> {
            if (!isVisibility){
                recyclerView.setVisibility(View.VISIBLE);
                linearLayoutMultiChange.setVisibility(View.GONE);
                isVisibility = true;
            }
        });
        btnCrop.setOnClickListener(v -> {
            new getBitmap().execute();

        });
        btnSave.setOnClickListener(v -> new getBitmap().execute());
        btnBack.setOnClickListener(v -> {
            startActivity(getIntent());
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
    @Override
    public void onPause() {
        super.onPause();
        mImageView.release();
        mImageView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageView.onResume();
    }



    @Override
    public void onStickerClicked(Bundle bundle) {
        String nameSticker = bundle.getString("nameSticker","");
        String namePack = bundle.getString("namePack","");
        try {
             addedSticker = Common.getStickerInAssets(this,nameSticker,namePack);
             stickerView.addSticker(new DrawableSticker(addedSticker));

        } catch (IOException e) {
            e.printStackTrace();
        }
        bottomSheetAddSticker.dismiss();
    }


    class getBitmap extends AsyncTask<Void, Void, Void>{
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            bm = mImageView.getBitmapData();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            photoView.setImageBitmap(bm);
            photoView.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postAtTime(new Runnable() {
                @Override
                public void run() {
//                    File file = FileUtil.getNewFile(MainActivity.this, "VintageCamera");
//                    if (file != null) {

                        Bitmap fBitmap = stickerView.getBitmap(mImageView.getWidth(), mImageView.getHeight());

                        photoView.setVisibility(View.INVISIBLE);

//                        FileUtil.saveImage(MainActivity.this,fBitmap, String.valueOf(Calendar.getInstance().getTimeInMillis()));

                    Intent intent = new Intent(MainActivity.this, CropImageActivity.class);
                        intent.putExtra("bitmapBytes", BitmapUlti.convertToArray(fBitmap));
                        startActivityForResult(intent, 123);
//                    } else {
//                        Toast.makeText(MainActivity.this, "the file is null", Toast.LENGTH_SHORT).show();
//                    }
                }
            }, 500);
        }
    }

    private void initView() {
        photoView = findViewById(R.id.photoView);
        btnAddText= findViewById(R.id.btnAddText);
        btnAddFilter= findViewById(R.id.btnAddFilter);
        btnAddDate= findViewById(R.id.btnAddDate);
        btnSave= findViewById(R.id.btnSave);
        btnBack= findViewById(R.id.btnBack);
        btnMultiChange= findViewById(R.id.btnMultiChange);
        linearLayoutMultiChange= findViewById(R.id.linearLayoutMultiChange);
        stickerView= findViewById(R.id.stickerView);
        mImageView = findViewById(R.id.image);

        btnAddSticker =  findViewById(R.id.btnAddSticker);
        btnCrop =  findViewById(R.id.btnCrop);
        seekBarFilter = findViewById(R.id.globalRestoreSeekBar);
        seekBarBrightness = findViewById(R.id.seekBarBrightness);
        seekBarContrast = findViewById(R.id.seekBarContrast);
        seekBarSaturation = findViewById(R.id.seekBarSaturation);
        tvBrightness = findViewById(R.id.tvBrightness);
        tvContrast = findViewById(R.id.tvContrast);
        tvSaturation = findViewById(R.id.tvSaturation);

        //seekbar

            seekBarBrightness.setProgress(currentBright);
            tvBrightness.setText(currentBright+ "%");

            seekBarContrast.setProgress(currentContrast);
            tvContrast.setText((int) (currentContrast/10)+"");


            seekBarSaturation.setProgress(currentSaturation);
            tvSaturation.setText(currentSaturation+"");



        //set main image
        Uri myUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        if (myUri != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), myUri);
                    mBitmap = ImageDecoder.decodeBitmap(source);
                }else {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myUri);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mg2);
        }
//        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mg2);
        //scale bitmap
        mBitmap = BitmapUlti.fitScreen(mBitmap);

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
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mBitmap.getWidth(), mBitmap.getHeight());
//        stickerView.setLayoutParams(layoutParams);
        stickerView.getLayoutParams().width= mBitmap.getWidth();
        stickerView.getLayoutParams().height= mBitmap.getHeight();


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
        adapter= new FilterDemoAdapter(filterDataList, this, mBitmap);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


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
        list.add(new FilterData("test1", EFFECT_CONFIGS[0]));
        list.add(new FilterData("test2", EFFECT_CONFIGS[1]));
        list.add(new FilterData("test3", EFFECT_CONFIGS[2]));
        list.add(new FilterData("test4", EFFECT_CONFIGS[3]));
        list.add(new FilterData("test5", EFFECT_CONFIGS[4]));
        list.add(new FilterData("test6", EFFECT_CONFIGS[5]));
        list.add(new FilterData("test7", EFFECT_CONFIGS[6]));
        return list;
    }


    public CGENativeLibrary.LoadImageCallback mLoadImageCallback = new CGENativeLibrary.LoadImageCallback() {

        //Notice: the 'name' passed in is just what you write in the rule, e.g: image1.jpg
        @Override
        public Bitmap loadImage(String name, Object arg) {


            AssetManager am = getAssets();
            InputStream is;
            try {
                is = am.open(name);
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
        seekBarFilter.setVisibility(View.VISIBLE);

        mCurrentConfig = EFFECT_CONFIGS[position];
//        Bitmap newBitmap =mark(mBitmap,position+" ");
//        mImageView.setImageBitmap(newBitmap);
        mImageView.setFilterWithConfig(mCurrentConfig);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== 123){
            if (resultCode==RESULT_OK){
                byte[] bytes = data.getByteArrayExtra("resultBitmap");
                Bitmap resultBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
               mImageView.setImageBitmap(resultBitmap);
            }
        }
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
               String textStyle = bundle.getString("textStyle",getString(R.string.default_text_style));
               String stringColor = bundle.getString("textColor",getString(R.string.default_text_color));
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