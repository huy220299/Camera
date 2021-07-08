package com.example.camera.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.R;
import com.example.camera.activity.TestActivity;
import com.example.camera.model.Butket;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder>{
    List<Butket> list;
    public FolderAdapter(List<Butket> list){
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvFolder.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (list==null){
            return 0;
        }else return list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFolder;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvFolder = itemView.findViewById(R.id.tvFolder);

        }


    }
}
