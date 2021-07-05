package com.example.camera;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListTextStyleAdapter extends RecyclerView.Adapter<ListTextStyleAdapter.ViewHolder> {
    Context context;

    ItemClickListener itemClickListener;
    List<String> listStyle;
    public  ListTextStyleAdapter( List<String> listStyle, ItemClickListener itemClickListener){
        this.listStyle =listStyle;
        this.itemClickListener = itemClickListener;
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
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//
//        int devicewidth = displaymetrics.widthPixels /4;
//        int deviceheight = displaymetrics.heightPixels / 10;
//        holder.itemView.getLayoutParams().width = devicewidth;
//        holder.itemView.getLayoutParams().height = deviceheight;

        holder.textView.setText(R.string.DefaultString);

//        Typeface font = ResourcesCompat.getFont(context, listStyle.get(position));
//        holder.textView.setTypeface(font);
        Typeface font = Typeface.createFromAsset(context.getAssets(),"font/"+  listStyle.get(position));
        holder.textView.setTypeface(font);
//        holder.textView.setTypeface(font,Typeface.BOLD);
        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onClick(v,position,"");
        });
    }

    @Override
    public int getItemCount() {
        return listStyle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView textView;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            textView = itemView.findViewById(R.id.textView);

        }


    }
    public interface ItemClickListener {
        public void onClick(View view, int position, String type);
    }

}
