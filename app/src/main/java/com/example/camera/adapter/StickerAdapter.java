package com.example.camera.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.R;
import com.example.camera.ultis.Common;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder>{
    Drawable[] listSticker;
    int[] list;
    int percent;
    ItemClickListener itemClickListener;
    Context context;


    public StickerAdapter( Drawable[] listSticker, int percent, ItemClickListener itemClickListener){
        this.listSticker = listSticker;
        this.percent = percent;
        this.itemClickListener = itemClickListener;
        list = new int[listSticker.length];
        for (int i = 0; i <list.length ; i++) {
            list[i]=0;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageDrawable(listSticker[position]);
        if (list[position]==1){
            holder.itemView.setBackground(context.getDrawable(R.drawable.bg_sticker_selected));
        }else {
            holder.itemView.setBackground(null);
        }

        holder.imageView.getLayoutParams().width = Common.getScreenWidth() /percent;
        holder.imageView.getLayoutParams().height = Common.getScreenWidth() /percent;
        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onClick(v,position);

        });
    }

    @Override
    public int getItemCount() {
        return listSticker.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }


    }
    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    public void setItemSelected(int position){
        for (int i = 0; i <list.length ; i++) {
            list[i] = 0;
        }
        list[position] =1;
      notifyDataSetChanged();

    }

}
