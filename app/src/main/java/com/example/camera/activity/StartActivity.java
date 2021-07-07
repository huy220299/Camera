package com.example.camera.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.camera.R;
import com.example.camera.ultis.BitmapUlti;
import com.example.camera.ultis.Common;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends BaseActivity {
    public static int REQUEST_CODE =123;
    private RelativeLayout btnCamera, btnPhoto;
    RecyclerView rcvBanner;
    List<String> urls;
    int[] listBanner = {
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
            R.drawable.banner4,
            R.drawable.banner6,
            R.drawable.banner7,
            R.drawable.banner8,
            R.drawable.banner9,
            R.drawable.banner10,
    };
    Timer timer;
    TimerTask timerTask;
    int position;
    LinearLayoutManager layoutManager;
    private  ImageView background1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnPhoto = findViewById(R.id.btnPhoto);
        background1 = findViewById(R.id.background1);
        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_banner);

        background1.setImageBitmap(BitmapUlti.rotateBitmap(bitmap));

        btnPhoto.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, REQUEST_CODE);
        });



//        rcvBanner = findViewById(R.id.rcvBanner);
//
//        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        rcvBanner.setLayoutManager(layoutManager);
//
//        BannerAdapter bannerAdapter = new BannerAdapter(this, listBanner);
//        rcvBanner.setAdapter(bannerAdapter);




//        try {
//            if (urls != null) {
//                position = Integer.MAX_VALUE / 2;
//                rcvBanner.scrollToPosition(position);
//            }
//            SnapHelper snapHelper = new LinearSnapHelper();
//            snapHelper.attachToRecyclerView(rcvBanner);
//            rcvBanner.smoothScrollBy(10000, 0);
//            rcvBanner.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//
//                    if (newState == 1) {
//                        stopAutoScrollBanner();
//                    } else if (newState == 0) {
//                        position = layoutManager.findFirstCompletelyVisibleItemPosition();
//                        runAutoScrollBanner();
//
//                    }
//                }
//            });
//        }catch (Exception e){
//
//        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Uri myUri = data.getData();
            ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), myUri);
            try {
                Bitmap myBitmap = ImageDecoder.decodeBitmap(source);

                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("imageUri", myUri.toString());
                startActivity(intent);
//                background1.setImageBitmap(myBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        runAutoScrollBanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopAutoScrollBanner();
    }

    private void stopAutoScrollBanner() {
        if (timer != null && timerTask != null) {
            timerTask.cancel();
            timer.cancel();
            timer = null;
            timerTask = null;
            position = layoutManager.findFirstCompletelyVisibleItemPosition();
        }
    }

    private void runAutoScrollBanner() {
        if (timer == null && timerTask == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (position == Integer.MAX_VALUE) {
                        position = Integer.MAX_VALUE / 2;
                        rcvBanner.scrollToPosition(position);
                        rcvBanner.smoothScrollBy(1, 0);
                    } else {
                        try {
                            position++;
                            rcvBanner.smoothScrollToPosition(position);
                        }catch (Exception e){
                            Log.e("~~~",new Gson().toJson(e));
                        }
                    }
                }
            };
            timer.schedule(timerTask, 1000, 1);
        }
    }


    class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder> {

        private Context context;
        private int[] urls;

        public BannerAdapter(Context context, int[] urls) {
            this.context = context;
            this.urls = urls;
        }

        @NonNull
        @Override
        public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
            return new BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
            holder.imgBanner.getLayoutParams().width = Common.getScreenWidth() /3;
            holder.imgBanner.getLayoutParams().height = Common.getScreenWidth() /6;
            int url = urls[position%urls.length];
            Glide.with(context).load(url).into(holder.imgBanner);
        }

        @Override
        public int getItemCount() {
            return urls == null ? 0 : Integer.MAX_VALUE;
        }

        public class BannerHolder extends RecyclerView.ViewHolder {
            ImageView imgBanner;

            public BannerHolder(@NonNull View itemView) {
                super(itemView);
                imgBanner = itemView.findViewById(R.id.imgBanner);
            }

        }
    }
}