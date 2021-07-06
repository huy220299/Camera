package com.example.camera.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.example.camera.R;
import com.example.camera.ultis.Common;
import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {
    RecyclerView rcvBanner;
    List<String> urls;
    int[] listBanner = {
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
            R.drawable.banner4,
            R.drawable.banner5,
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        rcvBanner = findViewById(R.id.rcvBanner);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvBanner.setLayoutManager(layoutManager);

        BannerAdapter bannerAdapter = new BannerAdapter(this, listBanner);
        rcvBanner.setAdapter(bannerAdapter);

        if (urls != null) {
            position = Integer.MAX_VALUE / 2;
            rcvBanner.scrollToPosition(position);
        }

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rcvBanner);
        rcvBanner.smoothScrollBy(10000, 0);

        rcvBanner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == 1) {
                    stopAutoScrollBanner();
                } else if (newState == 0) {
                    position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    runAutoScrollBanner();


                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        runAutoScrollBanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoScrollBanner();
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