package com.example.camera.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.camera.R;
import com.example.camera.adapter.FilterDemoAdapter;
import com.example.camera.adapter.ViewPagerAddFragmentsAdapter;
import com.example.camera.callback.EditTextCallback;
import com.example.camera.dialog.BottomSheetAddSticker;
import com.example.camera.dialog.BottomSheetAddText;
import com.example.camera.dialog.CustomBackDialog;
import com.example.camera.fragment.DetailPackOverlayFragment;
import com.example.camera.fragment.DetailPackStickerFragment;
import com.example.camera.model.FilterData;
import com.example.camera.ultis.BitmapUlti;
import com.example.camera.ultis.Common;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
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
import java.util.Objects;


public class MainActivity extends BaseActivity implements FilterDemoAdapter.ItemClickListener,  DetailPackStickerFragment.CallbackSticker, DetailPackOverlayFragment.CallbackOverlay {
    public static String TAG = "MainActivity";
    private String mCurrentConfig;
    protected static final String BASIC_FILTER_CONFIG = "@adjust brightness 0 @adjust contrast 1 @adjust saturation 1 @adjust sharpen 0";
    ImageGLSurfaceView mImageView;
    RecyclerView recyclerView;
    FilterDemoAdapter adapter;
    List<FilterData> filterDataList;
    Bitmap mBitmap, bitmapFiltered;
    private SeekBar seekBarFilter, seekBarBrightness, seekBarContrast, seekBarSaturation;
    private int currentBright = 50, currentContrast = 10, currentSaturation = 0;
    private TextView tvBrightness, tvContrast, tvSaturation;
    private RelativeLayout btnSave, btnBack, btnMultiChange;
    private ImageView imageViewFrame, imageViewDate;
    private LinearLayout linearLayoutMultiChange;


    private RelativeLayout btnAddText, btnAddSticker, btnCrop, btnAddFilter, btnAddOverlay, btnAddDate;
    private ConstraintLayout constraintLayoutOverlay;

    private boolean isVisibility = true, isDone;
    private StickerView stickerView;
    private BottomSheetAddText bottomSheetAddText;
    private BottomSheetAddSticker bottomSheetAddSticker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        onActionEvent();


    }


    private void seekBarChange() {

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

                Bitmap bitmap = BitmapUlti.adjustedContrast(bitmapFiltered, currentContrast);
                bitmap = BitmapUlti.adjustSaturation(bitmap, currentSaturation);
                bitmap = BitmapUlti.adjustBrightness(bitmap, progress);
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

                Bitmap bitmap = BitmapUlti.adjustBrightness(bitmapFiltered, currentBright);
                bitmap = BitmapUlti.adjustSaturation(bitmap, currentSaturation);
                bitmap = BitmapUlti.adjustedContrast(bitmap, progress);

                mImageView.setImageBitmap(bitmap);

                currentContrast = progress;
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

                Bitmap bitmap = BitmapUlti.adjustedContrast(bitmapFiltered, currentContrast);
                bitmap = BitmapUlti.adjustBrightness(bitmap, currentBright);
                bitmap = BitmapUlti.adjustSaturation(bitmap, progress);
                mImageView.setImageBitmap(bitmap);

                currentSaturation = progress;
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
        btnAddDate.setOnClickListener(v -> {

        });
        btnAddOverlay.setOnClickListener(v -> {

            recyclerView.setVisibility(View.GONE);
            linearLayoutMultiChange.setVisibility(View.GONE);
            constraintLayoutOverlay.setVisibility(View.VISIBLE);
        });

        btnMultiChange.setOnClickListener(v ->
        {
            bitmapFiltered = mImageView.getBitmapData();
            if (isVisibility) {
                isVisibility = false;
                mImageView.setImageBitmap(mImageView.getBitmapData());
                mImageView.setFilterWithConfig(BASIC_FILTER_CONFIG);
                seekBarFilter.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                constraintLayoutOverlay.setVisibility(View.GONE);
                linearLayoutMultiChange.setVisibility(View.VISIBLE);
            } else {
                isVisibility = true;
                seekBarFilter.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                constraintLayoutOverlay.setVisibility(View.GONE);
                linearLayoutMultiChange.setVisibility(View.GONE);
            }

        });
        btnAddFilter.setOnClickListener(v -> {
            if (!isVisibility) {
                recyclerView.setVisibility(View.VISIBLE);
                linearLayoutMultiChange.setVisibility(View.GONE);
                constraintLayoutOverlay.setVisibility(View.GONE);
                isVisibility = true;
            }
        });
        btnCrop.setOnClickListener(v -> {
            isDone = false;
            new getBitmap().execute();
        });
        btnSave.setOnClickListener(v ->
        {
            isDone = true;
            new getBitmap().execute();
        });
        btnBack.setOnClickListener(v -> onBackPressed());

        btnAddText.setOnClickListener(v -> {
            btnAddText.setEnabled(false);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> btnAddText.setEnabled(true), 2000);
            bottomSheetAddText = new BottomSheetAddText(editTextCallback);
            bottomSheetAddText.show(getSupportFragmentManager(), "ModalBottomSheet");

        });

        btnAddSticker.setOnClickListener(v -> {
            btnAddText.setEnabled(false);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> btnAddText.setEnabled(true), 2000);

            Bundle bundle = new Bundle();
            bottomSheetAddSticker = new BottomSheetAddSticker();
            bottomSheetAddSticker.show(getSupportFragmentManager(), "ModalBottomSheet");
            bottomSheetAddSticker.setArguments(bundle);

        });

        stickerView.setOnStickerOperationListener(sticker -> {

            if (sticker instanceof TextSticker) {
                stickerView.remove(sticker);
                TextPaint textPaint = ((TextSticker) sticker).getTextPaint();
                String text = ((TextSticker) sticker).getText();
                bottomSheetAddText = new BottomSheetAddText(text,textPaint,editTextCallback);
                bottomSheetAddText.show(getSupportFragmentManager(),"BottomSheet Add Text");
            }
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
    public void onBackPressed() {

        CustomBackDialog cdd=new CustomBackDialog(MainActivity.this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cdd.show();
    }

    @Override
    public void onStickerClicked(Bundle bundle) {
        String nameSticker = bundle.getString("nameSticker", "");
        String namePack = bundle.getString("namePack", "");
        try {
            Drawable addedSticker = Common.getDrawableInAssets(this, "sticker", namePack, nameSticker);
            stickerView.addSticker(new DrawableSticker(addedSticker));

        } catch (IOException e) {
            e.printStackTrace();
        }
        bottomSheetAddSticker.dismiss();
    }

    @Override
    public void onOverlayClicked(Bundle bundle) {
        String nameOverlay = bundle.getString("nameOverlay", "");
        String namePack = bundle.getString("namePack", "");
        if (!nameOverlay.equals("0.png")) {
            try {
                Drawable addedOverlay = Common.getDrawableInAssets(this, "overlay", namePack, nameOverlay);
                imageViewFrame.getLayoutParams().width = mBitmap.getWidth();
                imageViewFrame.getLayoutParams().height = mBitmap.getHeight();
                imageViewFrame.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageViewFrame.setImageDrawable(addedOverlay);
            } catch (IOException e) {
                Log.e("_____________", new Gson().toJson(e));
            }
        }
        else {
            imageViewFrame.setImageDrawable(null);
        }
    }

    class getBitmap extends AsyncTask<Void, Void, Void> {
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap bmFi = mImageView.getBitmapData();
            Bitmap fBitmap = stickerView.getBitmap();

            Bitmap bmAll = Bitmap.createBitmap(bmFi.getWidth(), bmFi.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmAll);
            c.drawBitmap(bmFi, 0, 0, null);
            c.drawBitmap(fBitmap, 0, 0, null);
            try {
                Bitmap bitmapOverlay = ((BitmapDrawable)imageViewFrame.getDrawable()).getBitmap();
                bitmapOverlay = BitmapUlti.getResizedBitmap(bitmapOverlay,mBitmap.getWidth(),mBitmap.getHeight());
                Paint alphaPaint = new Paint();
                alphaPaint.setAlpha(100);

                c.drawBitmap(bitmapOverlay,0,0,alphaPaint);
            }catch (Exception e){
                Log.e(TAG, new Gson().toJson(e));
            }
            String encoded = Base64.encodeToString(BitmapUlti.convertToArray(bmAll), Base64.DEFAULT);
            SharedPreferences preferences = getSharedPreferences("test", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Image", encoded);
            editor.apply();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postAtTime(() -> {
                if (isDone) {
                    startActivity(new Intent(MainActivity.this, SaveActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, CropImageActivity.class));
                }

            }, 500);

        }
    }

    private void initView() {
        imageViewFrame = findViewById(R.id.imageViewFrame);
        imageViewDate = findViewById(R.id.imageViewDate);
        btnAddText = findViewById(R.id.btnAddText);
        btnAddFilter = findViewById(R.id.btnAddFilter);
        btnAddOverlay = findViewById(R.id.btnAddOverlay);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        btnMultiChange = findViewById(R.id.btnMultiChange);
        btnAddDate = findViewById(R.id.btnAddDate);
        linearLayoutMultiChange = findViewById(R.id.linearLayoutMultiChange);
        stickerView = findViewById(R.id.stickerView);
        mImageView = findViewById(R.id.image);
        btnAddSticker = findViewById(R.id.btnAddSticker);
        btnCrop = findViewById(R.id.btnCrop);
        seekBarFilter = findViewById(R.id.globalRestoreSeekBar);
        seekBarBrightness = findViewById(R.id.seekBarBrightness);
        seekBarContrast = findViewById(R.id.seekBarContrast);
        seekBarSaturation = findViewById(R.id.seekBarSaturation);
        tvBrightness = findViewById(R.id.tvBrightness);
        tvContrast = findViewById(R.id.tvContrast);
        tvSaturation = findViewById(R.id.tvSaturation);
        constraintLayoutOverlay = findViewById(R.id.constraintLayoutOverlay);

        //seekbar
        seekBarBrightness.setProgress(currentBright);
        tvBrightness.setText(String.valueOf(currentBright));

        seekBarContrast.setProgress(currentContrast);
        tvContrast.setText(String.valueOf((currentContrast / 10)));

        seekBarSaturation.setProgress(currentSaturation);
        tvSaturation.setText(String.valueOf(currentSaturation));

        //________________________________________________________________get bitmap from intent


        String myUri = (getIntent().getStringExtra("imageUri"));
        if (myUri==null){
            SharedPreferences preferences = getSharedPreferences("test",MODE_PRIVATE);
            String image = preferences.getString("Image", "");
            byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
            mBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        }else {
            mBitmap = BitmapFactory.decodeFile(myUri, null);
        }

        //scale bitmap
        mBitmap = BitmapUlti.fitScreen(mBitmap);
        mImageView.getLayoutParams().height = mBitmap.getHeight();
        mImageView.getLayoutParams().width = mBitmap.getWidth();
        mImageView.setSurfaceCreatedCallback(() -> {

            mImageView.setImageBitmap(mBitmap);
            mImageView.setFilterWithConfig(mCurrentConfig);
            mImageView.setFilterIntensity(1.0f);
            CGENativeLibrary.setLoadImageCallback(mLoadImageCallback, null);
        });

        //________________________________________________________________set up sticker view
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
        //set size stickerView
        stickerView.getLayoutParams().width = mBitmap.getWidth();
        stickerView.getLayoutParams().height = mBitmap.getHeight();


        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);

        //Show list filter
        recyclerView = findViewById(R.id.recyclerView);
        filterDataList = new ArrayList<>();
        filterDataList = getListFilter();
        adapter = new FilterDemoAdapter(filterDataList, this, mBitmap);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //show list overlay
        TabLayout tabLayoutOverlay = findViewById(R.id.tabLayoutOverlay);
        ViewPager viewPagerOverlay = findViewById(R.id.viewPagerOverlay);
        ViewPagerAddFragmentsAdapter adapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager());
        List<String> list ;
        list = Common.getListOverlayFromAssets(this);
        for (int i = 0; i < list.size(); i++) {
            adapter.addFrag(new DetailPackOverlayFragment(list.get(i)));
        }
        viewPagerOverlay.setAdapter(adapter);
        tabLayoutOverlay.setupWithViewPager(viewPagerOverlay);
        tabLayoutOverlay.setSmoothScrollingEnabled(true);
        for (int i = 0; i < list.size(); i++) Objects.requireNonNull(tabLayoutOverlay.getTabAt(i)).setText(list.get(i));
    }

    private void addTextSticker(String text, TextPaint textPaint) {
        TextSticker sticker = new TextSticker(this);

        sticker.setText(text);
        sticker.setTextColor(textPaint.getColor());
        switch (textPaint.getTextAlign()){
            case LEFT:
                sticker.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                break;
            case CENTER:
                sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                break;
            case RIGHT:
                sticker.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                break;
        }
        sticker.resizeText();
        sticker.setTypeface(textPaint.getTypeface());
        stickerView.addSticker(sticker);
    }

    private List<FilterData> getListFilter() {
        List<FilterData> list = new ArrayList<>();
        List<String> listRules = getListRules();
        for (int i = 0; i < listRules.size(); i++) {
            list.add(new FilterData(String.valueOf(i), listRules.get(i)));
        }
        return list;
    }

    public CGENativeLibrary.LoadImageCallback mLoadImageCallback = new CGENativeLibrary.LoadImageCallback() {

        //Notice: the 'name' passed in is just what you write in the rule, e.g: image1.jpg
        @Override
        public Bitmap loadImage(String name, Object arg) {
            AssetManager am = getAssets();
            InputStream is;
            try {
                is = am.open("filter/" + name);
            } catch (IOException e) {
                return null;
            }
            return BitmapFactory.decodeStream(is);
        }
        @Override
        public void loadImageOK(Bitmap bmp, Object arg) {
            bmp.recycle();
        }
    };

    @Override
    public void onClick(View view, int position, String type) {
        seekBarFilter.setVisibility(View.VISIBLE);
        mCurrentConfig = filterDataList.get(position).getRule();
//        Bitmap newBitmap =mark(mBitmap,position+" ");
//        mImageView.setImageBitmap(newBitmap);
        mImageView.setFilterWithConfig(mCurrentConfig);

    }

    @Override
    protected void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    private List<String> getListRules() {
        List<String> ListNameFilter = Common.getNameFilter(this);
        List<String> listRules = new ArrayList<>();
        for (int i = 0; i < ListNameFilter.size(); i++) {
            listRules.add("@adjust lut " + ListNameFilter.get(i));
        }
        return listRules;
    }

    EditTextCallback editTextCallback = new EditTextCallback() {
        @Override
        public void edit_callback(String text, TextPaint textPaint) {
            if (!text.equals("")){
                addTextSticker(text,textPaint);
            }
                bottomSheetAddText.dismiss();
        }
    };


}