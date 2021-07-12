package com.example.camera.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.camera.R;
import com.example.camera.activity.MainActivity;
import com.example.camera.model.Bucket;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder>{
    Context context;
    List<Bucket> list;



    public FolderAdapter(List<Bucket> list ){
        this.list = list;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvFolder.setText(list.get(position).getName());
        InnerAdapter adapter = new InnerAdapter(loadUriFromPath(list.get(position)));
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,false));
        if (position==0){
            holder.recyclerView.setVisibility(View.VISIBLE);
            list.get(position).setOpened(true);
        }
        holder.itemView.setOnClickListener(v -> {
            if (list.get(position).isOpened()){
                holder.btnHide.setImageResource(R.drawable.down);
                holder.recyclerView.setVisibility(View.GONE);
                list.get(position).setOpened(false);
            }else {
                holder.btnHide.setImageResource(R.drawable.up);
                holder.recyclerView.setVisibility(View.VISIBLE);
                list.get(position).setOpened(true);
            }

        });
    }

    @Override
    public int getItemCount() {
        if (list==null){
            return 0;
        }else return list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFolder;
        RecyclerView recyclerView;
        ImageView btnHide;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvFolder = itemView.findViewById(R.id.tvFolder);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            btnHide= itemView.findViewById(R.id.btnHide);

        }


    }
    class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.InnerViewHolder>{
        Context context;
        List<String> listUri;
        public InnerAdapter(List<String> listUri){
            this.listUri = listUri;

        }
        @NonNull
        @Override
        public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
            return new InnerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {

            Glide.with(context).load(listUri.get(position)).override(300,300).into(holder.imageView);
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("imageUri", listUri.get(position));
                context.startActivity(intent);
            });
//            holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(300,300));
        }

        @Override
        public int getItemCount() {
            return listUri.size();
        }

        class InnerViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;

            public InnerViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView= itemView.findViewById(R.id.imageView);

            }
        }
    }
    public List<String> loadUriFromPath(Bucket folder){
        String pathFolder= folder.getFirstImageContainedPath().substring(0,folder.getFirstImageContainedPath().lastIndexOf("/"));
        File file = new File(pathFolder);
        File[] listFile;
        List<String> listPhoto1 = new ArrayList<>();
        if (file.isDirectory()){
            listFile = file.listFiles();
            for (int i=0; i<listFile.length; i++){
                String path= listFile[i].getAbsolutePath();
                if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")){
                    listPhoto1.add(path);
                }
            }

        }
        return listPhoto1;
    }

}
