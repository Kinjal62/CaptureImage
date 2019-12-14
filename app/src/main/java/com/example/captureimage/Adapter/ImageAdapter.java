package com.example.captureimage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.captureimage.MainActivity;
import com.example.captureimage.Model.ImageModel;
import com.example.captureimage.R;
import java.util.ArrayList;


public class ImageAdapter extends  RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    Context ctx;
    ArrayList<String> image;

    public ImageAdapter(Context ctx, ArrayList<String> image) {
        this.ctx = ctx;
        this.image = image;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(ctx).inflate(R.layout.raw_image_selection, parent, false);
        return new ImageAdapter.ViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
       // holder.imageview.setImageResource(image.get(position).getImage());
        Glide.with(ctx).load(image.get(position)).into(holder.imageview);
//        Glide.with(ctx).clear(image.get(position).indexOf(holder.cancel));
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.remove(position);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return image.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        ImageButton cancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = (ImageView) itemView.findViewById(R.id.imageview);
            cancel = itemView.findViewById(R.id.cancel);
        }
    }
}

