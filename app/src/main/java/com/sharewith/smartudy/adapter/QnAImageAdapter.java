package com.sharewith.smartudy.adapter;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sharewith.smartudy.smartudy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simjae on 2018-07-17.
 */

public class QnAImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Integer> mImages;

    public QnAImageAdapter(List<Integer> mImages) {
        this.mImages = mImages;
    }

    public void setImageDatas(List<Integer> mImages){
        this.mImages = mImages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler_item,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position<=mImages.size())
            ((ImageViewHolder)holder).img.setImageResource(mImages.get(position));
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder{
         ImageView img;
        public ImageViewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.recycler_img);
        }

    }
}
