package com.example.camera.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.R;

public class ListColorAdapter extends RecyclerView.Adapter<ListColorAdapter.ViewHolder>{
    String[] listColor;
    Context context;
    FilterDemoAdapter.ItemClickListener itemClickListener;

    public ListColorAdapter(String[] listColor, FilterDemoAdapter.ItemClickListener itemClickListener){
        this.listColor = listColor;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cardView.setCardBackgroundColor(Color.parseColor(listColor[position]));
        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onClick(v,position,"textColor");
        });
    }

    @Override
    public int getItemCount() {
        return listColor.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        CardView cardView;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
           cardView = itemView.findViewById(R.id.cardView);

        }


    }


}
