package com.example.androidchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidchat.ChatActivity;
import com.example.androidchat.R;
import com.example.androidchat.domin.Match;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchRecyclerAdapter extends RecyclerView.Adapter<MatchRecyclerAdapter.myMatchViewHolder> {

    private Context mContext;
    private List<Match> mMatchList;

    public MatchRecyclerAdapter(Context context, List<Match> mMatchList) {
        this.mContext = context;
        this.mMatchList = mMatchList;
    }

    @NonNull
    @Override
    public myMatchViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_match_user_item, parent, false);
        return new myMatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchRecyclerAdapter.myMatchViewHolder holder, int position) {
        holder.setMatchData(mMatchList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMatchList.size();
    }

    public class myMatchViewHolder extends RecyclerView.ViewHolder{
        TextView mName;
        CircleImageView mImg;
        View mView;

        public myMatchViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mName = itemView.findViewById(R.id.match_chat_name);
            mImg = itemView.findViewById(R.id.match_chat_image);
        }

        public void setMatchData(Match match){
            mName.setText(match.getName());
            if(!match.getPro_img_url().equals("")){
                Glide.with(mContext).load(match.getPro_img_url()).placeholder(R.drawable.progress_bar).into(mImg);
            }
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("receiver_id", match.getUser_id());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
