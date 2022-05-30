package com.example.androidchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidchat.R;
import com.example.androidchat.domin.ImageItem;

import java.util.ArrayList;

public class ImageViewPagerAdapter extends RecyclerView.Adapter<ImageViewPagerAdapter.MyImageViewHolder>{

    // attributes to the VideosAdapter class
    private ArrayList<ImageItem> imageItems;
    private Context mContext;

    // constructor of VideosAdapter
    public ImageViewPagerAdapter(ArrayList<ImageItem> imageItems, Context mContext){

        this.imageItems = imageItems;
        this.mContext = mContext;
    }



    // below 3 function are from RecyclerView.Adapter<VideosAdapter.VideoViewHolder>

    // adapter should go to item_container_video xml to show the video, when ever a video turn comes up
    // returning an object that is from MyVideoViewHolder class which we created below
    @NonNull
    @Override
    public MyImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyImageViewHolder myImageViewHolder = new MyImageViewHolder( LayoutInflater.from(parent.getContext()).inflate( R.layout.single_image_item, parent, false ) );
        return  myImageViewHolder;
    }

    // setting only that video to show
    // we created setVideoData function below VideoViewHolder class
    @Override
    public void onBindViewHolder(@NonNull ImageViewPagerAdapter.MyImageViewHolder holder, int position) {
        holder.setImageData(imageItems.get(position));
    }

    // telling how many videos are there, so that RecyclerView can be prepared
    @Override
    public int getItemCount() {

        return imageItems.size();
    }

    // RecyclerView help in only loading some part into memory which screen can be visible to the user
    // we use static class because even if don't like to create an object when this class is called
    public class MyImageViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView imageNameTextView, imageHobbyTextView;

        // constructor
        public MyImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imageNameTextView = itemView.findViewById(R.id.imageNameTextView);
            imageHobbyTextView = itemView.findViewById(R.id.imageHobbyTextView);

        }

        void setImageData(ImageItem imageItem){
            imageNameTextView.setText(imageItem.name);
            imageHobbyTextView.setText(imageItem.UserHobby);
            Glide.with(mContext).load(imageItem.UserImgUrl).placeholder(R.drawable.progress_bar).into(imageView);

        }

    }

}