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
import android.graphics.Typeface;
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

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.camera.R;
import com.example.camera.adapter.FilterDemoAdapter;
import com.example.camera.adapter.ViewPagerAddFragmentsAdapter;
import com.example.camera.callback.EditTextCallback;
import com.example.camera.fragment.BottomSheetAddSticker;
import com.example.camera.fragment.BottomSheetAddText;
import com.example.camera.fragment.CustomDialogClass;
import com.example.camera.fragment.DetailPackOverlayFragment;
import com.example.camera.fragment.DetailPackStickerFragment;
import com.example.camera.model.FilterData;
import com.example.camera.ultis.BitmapUlti;
import com.example.camera.ultis.Common;
import com.google.android.material.tabs.TabLayout;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.Sticker;
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
    private ImageView imageViewFrame;
    private LinearLayout linearLayoutMultiChange;


    private RelativeLayout btnAddText, btnAddSticker, btnCrop, btnAddFilter, btnAddDate;
    private ConstraintLayout linearLayoutOverlay;

    private boolean isVisibility = true, isDone;
    private Typeface currentFont;
    private Bitmap bm, y;
    private Drawable addedSticker;
    private StickerView stickerView;
    private TextSticker currentTextSticker;
    private BottomSheetAddText bottomSheetAddText;
    private BottomSheetAddSticker bottomSheetAddSticker;
    private TabLayout tabLayoutOverlay;
    private ViewPager viewPagerOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        onActionEvent();


    }


    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
//        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
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

            recyclerView.setVisibility(View.GONE);
            linearLayoutMultiChange.setVisibility(View.GONE);
            linearLayoutOverlay.setVisibility(View.VISIBLE);
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
                linearLayoutMultiChange.setVisibility(View.VISIBLE);
            } else {
                isVisibility = true;
                seekBarFilter.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                linearLayoutMultiChange.setVisibility(View.GONE);
            }

        });
        btnAddFilter.setOnClickListener(v -> {
            if (!isVisibility) {
                recyclerView.setVisibility(View.VISIBLE);
                linearLayoutMultiChange.setVisibility(View.GONE);
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
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnAddText.setOnClickListener(v -> {
            btnAddText.setEnabled(false);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> btnAddText.setEnabled(true), 2000);
            bottomSheetAddText = new BottomSheetAddText();
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

        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {

                if (sticker instanceof TextSticker) {
                    TextPaint textPaint = ((TextSticker) sticker).getTextPaint();
                    int align = 0;
                    switch (textPaint.getTextAlign()) {
                        case LEFT:
                            align = 0;
                            break;
                        case CENTER:
                            align = 1;
                            break;
                        case RIGHT:
                            align = 2;
                            break;
                    }
                    currentTextSticker = (TextSticker) sticker;
                    stickerView.remove(sticker);
//                    stickerView.invalidate();
                    currentFont = textPaint.getTypeface();
                    Bundle bundle = new Bundle();

                    bundle.putInt("color", textPaint.getColor());
                    bundle.putInt("align", align);
                    bundle.putString("text", ((TextSticker) sticker).getText());

                    bottomSheetAddText = new BottomSheetAddText();
                    bottomSheetAddText.setArguments(bundle);
                    bottomSheetAddText.show(getSupportFragmentManager(), "ModalBottomSheet");

                }

            }

            @Override
            public void onTextStickerDoubleTapped(@NonNull TextSticker textSticker) {
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

        CustomDialogClass cdd=new CustomDialogClass(MainActivity.this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cdd.show();
    }

    @Override
    public void onStickerClicked(Bundle bundle) {
        String nameSticker = bundle.getString("nameSticker", "");
        String namePack = bundle.getString("namePack", "");
        try {
            addedSticker = Common.getStickerInAssets(this, nameSticker, namePack);
            stickerView.addSticker(new DrawableSticker(addedSticker));

        } catch (IOException e) {
            e.printStackTrace();
        }
        bottomSheetAddSticker.dismiss();
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
            bm = bmAll;
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postAtTime(() -> {

                String encoded = Base64.encodeToString(BitmapUlti.convertToArray(bm), Base64.DEFAULT);
                SharedPreferences preferences = getSharedPreferences("test", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Image", encoded);
                editor.apply();
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
        btnAddText = findViewById(R.id.btnAddText);
        btnAddFilter = findViewById(R.id.btnAddFilter);
        btnAddDate = findViewById(R.id.btnAddDate);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        btnMultiChange = findViewById(R.id.btnMultiChange);
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

        linearLayoutOverlay = findViewById(R.id.linearLayoutOverlay);

        //seekbar

        seekBarBrightness.setProgress(currentBright);
        tvBrightness.setText(String.valueOf(currentBright));

        seekBarContrast.setProgress(currentContrast);
        tvContrast.setText(String.valueOf((currentContrast / 10)));


        seekBarSaturation.setProgress(currentSaturation);
        tvSaturation.setText(String.valueOf(currentSaturation));


        //get bitmap from intent
        String myUri = (getIntent().getStringExtra("imageUri"));
        mBitmap = BitmapFactory.decodeFile(myUri, null);
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
        // mImageView.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT);


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

        stickerView.getLayoutParams().width = mBitmap.getWidth();
        stickerView.getLayoutParams().height = mBitmap.getHeight();


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
        adapter = new FilterDemoAdapter(filterDataList, this, mBitmap);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //show list overlay
        tabLayoutOverlay = findViewById(R.id.tabLayoutOverlay);
        viewPagerOverlay = findViewById(R.id.viewPagerOverlay);
        ViewPagerAddFragmentsAdapter adapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager());
        List<String> list ;
        list = Common.getListOverlayFromAssets(this);
        for (int i = 0; i < list.size(); i++) {
            adapter.addFrag(new DetailPackOverlayFragment(list.get(i)));
        }
        viewPagerOverlay.setAdapter(adapter);
        tabLayoutOverlay.setupWithViewPager(viewPagerOverlay);
        tabLayoutOverlay.setSmoothScrollingEnabled(true);
//        tabLayout.setScrollPosition(targetChannelPosition, 0f, true);
        for (int i = 0; i < list.size(); i++) {
            tabLayoutOverlay.getTabAt(i).setText(list.get(i));
        }
//        bottomSheetAddText = new BottomSheetAddText();




    }

    private void loadOverlay(String path) {
        try {
            // get input stream
//            InputStream ims = getAssets().open("overley/heart/Heart 1.webp");
            InputStream ims = getAssets().open(path);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            imageViewFrame.getLayoutParams().width = mBitmap.getWidth();
            imageViewFrame.getLayoutParams().height = mBitmap.getHeight();
            imageViewFrame.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViewFrame.setImageDrawable(d);
            ims.close();
        } catch (IOException ex) {
            Log.e("~~~", ex.getMessage());
        }
    }

    private void addTextSticker(String text, String textStyle, int align, String stringColor) {
        TextSticker sticker = new TextSticker(this);

        sticker.setText(text);
        sticker.setTextColor(Color.parseColor(stringColor));
        if (align == 1) {
            sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        } else if (align == 2) {
            sticker.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
        } else {
            sticker.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
        }
        sticker.resizeText();
        Typeface font = Typeface.createFromAsset(getAssets(), "font/" + textStyle);
        sticker.setTypeface(font);
        stickerView.addSticker(sticker);

    }

    private void addTextSticker(String text, String textStyle, int align, int stringColor) {
        TextSticker sticker = new TextSticker(this);

        sticker.setText(text);
        sticker.setTextColor(stringColor);
        if (align == 1) {
            sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        } else if (align == 2) {
            sticker.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
        } else {
            sticker.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
        }
        sticker.resizeText();
        Typeface font = Typeface.createFromAsset(getAssets(), "font/" + textStyle);
        sticker.setTypeface(font);
        stickerView.addSticker(sticker);

    }

    private void addTextSticker(String text, Typeface textStyle, int align, String stringColor) {
        TextSticker sticker = new TextSticker(this);

        sticker.setText(text);
        sticker.setTextColor(Color.parseColor(stringColor));
        if (align == 1) {
            sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        } else if (align == 2) {
            sticker.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
        } else {
            sticker.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
        }
        sticker.resizeText();
        sticker.setTypeface(textStyle);
        stickerView.addSticker(sticker);

    }

    private void addTextSticker(String text, Typeface textStyle, int align, int stringColor) {
        TextSticker sticker = new TextSticker(this);

        sticker.setText(text);
        sticker.setTextColor(stringColor);

        if (align == 1) {
            sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        } else if (align == 2) {
            sticker.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
        } else {
            sticker.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
        }
        sticker.resizeText();
        sticker.setTypeface(textStyle);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
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
        switch (view.getId()) {
            case R.id.btnCancel:
                String type1 = bundle.getString("type", "");
                if (type1.equals("edit")) {
                    stickerView.addSticker(currentTextSticker);
                }
                bottomSheetAddText.dismiss();
                break;
            case R.id.btnDone:
                String text = bundle.getString("text", "");
                String textStyle = bundle.getString("textStyle", getString(R.string.default_text_style));
                String stringColor = bundle.getString("textColor", getString(R.string.default_text_color));
                int align = bundle.getInt("align", 1);
                String type = bundle.getString("type", "");
                boolean isChangeFont = bundle.getBoolean("isChangeFont");
                boolean isChangeColor = bundle.getBoolean("isChangeColor");
                if (type.equals("edit")) {
                    if (!isChangeColor && !isChangeFont) {
                        addTextSticker(text, currentTextSticker.getTextPaint().getTypeface(), align, currentTextSticker.getTextPaint().getColor());
                    } else if (!isChangeColor && isChangeFont) {
                        addTextSticker(text, textStyle, align, currentTextSticker.getTextPaint().getColor());
                    } else if (!isChangeFont && isChangeColor) {
                        addTextSticker(text, currentTextSticker.getTextPaint().getTypeface(), align, stringColor);
                    }
                } else if (!text.equals("")) {
                    addTextSticker(text, textStyle, align, stringColor);
                }
                bottomSheetAddText.dismiss();

        }

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
        public void edit_callback(int color, String text, Typeface typeface) {

        }
    };
}