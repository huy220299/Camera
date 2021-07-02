package com.example.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TextStyleAdapter extends RecyclerView.Adapter<TextStyleAdapter.ViewHolder> {
    Context context;
    String addedString;
    FilterDemoAdapter.ItemClickListener itemClickListener;
    int[] listStyle;
    public  TextStyleAdapter( String addedString, int[] listStyle, FilterDemoAdapter.ItemClickListener itemClickListener){
        this.listStyle =listStyle;
        this.itemClickListener = itemClickListener;
        this.addedString = addedString;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_style, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int devicewidth = displaymetrics.widthPixels /5;
        holder.itemView.getLayoutParams().width = devicewidth;

            holder.textView.setText(addedString);

        Typeface font = ResourcesCompat.getFont(context, listStyle[position]);
        holder.textView.setTypeface(font);

            holder.itemView.setOnClickListener(v -> {
                itemClickListener.onClick(v,position);
            });
    }

    @Override
    public int getItemCount() {
        return listStyle.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView textView;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            textView = itemView.findViewById(R.id.textView);

        }


    }
}
