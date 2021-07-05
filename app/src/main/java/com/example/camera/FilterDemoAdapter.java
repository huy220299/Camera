package com.example.camera;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Insets;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.model.FilterData;

import org.wysaid.view.ImageGLSurfaceView;

import java.util.List;

public class FilterDemoAdapter extends RecyclerView.Adapter<FilterDemoAdapter.ViewHolder> {

    ItemClickListener itemClickListener;

    Activity activity;
    List<FilterData> listFilter;
    Context context;

    public FilterDemoAdapter(List<FilterData> listFilter, ItemClickListener itemClickListener) {
        this.listFilter = listFilter;
        this.itemClickListener = itemClickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_demo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        int devicewidth = displaymetrics.widthPixels /5;
        Log.e("~~~",displaymetrics.widthPixels+"");

        //if you need 4-5-6 anything fix imageview in height
        int deviceheight = displaymetrics.heightPixels / 4;

        holder.itemView.getLayoutParams().width = devicewidth;

//        //if you need same height as width you can set devicewidth in holder.image_view.getLayoutParams().height
//        holder.img.getLayoutParams().height = deviceheight;

       FilterData currentItem = listFilter.get(position);
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(),currentItem.getImageId());
        holder.img.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
            @Override
            public void surfaceCreated() {
                holder.img.setImageBitmap(mBitmap);
               holder.img.setFilterWithConfig(currentItem.getRule());
            }
        });
        holder.img_name.setText(currentItem.getName());
        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onClick(v, position, "filter");
        });
    }

    @Override
    public int getItemCount() {
        return listFilter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageGLSurfaceView img;
        TextView img_name;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            img  = itemView.findViewById(R.id.image);
            img_name = itemView.findViewById(R.id.tvImage);

        }


    }
    interface ItemClickListener {
        void onClick(View view, int position, String type);
    }






}

