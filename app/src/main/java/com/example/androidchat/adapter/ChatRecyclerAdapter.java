package com.example.androidchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchat.R;
import com.example.androidchat.domin.Chat;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<Chat> mChatList;
    FirebaseAuth mAuth;

    public ChatRecyclerAdapter(Context mContext, List<Chat> mChatList) {
        this.mContext = mContext;
        this.mChatList = mChatList;
        mAuth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = null;
        //getItemViewType(int position) returns 0  (0 means sender=current user)
        if(viewType == 0){
            view = LayoutInflater.from(mContext).inflate(R.layout.sender_single_item, parent, false);
            return new SenderViewHolder(view);

        }else{
            //for receiver
            view = LayoutInflater.from(mContext).inflate(R.layout.reciver_single_item, parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // returning sender view holder means 0
        if(holder.getItemViewType() == 0){
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.mMessage.setText(mChatList.get(position).getMessage());

        }else if(holder.getItemViewType() == 1){
            // For receiver
            ReceiverViewHolder receiverViewHolder= (ReceiverViewHolder) holder;
            receiverViewHolder.mMessage.setText(mChatList.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // if the chat if from is equal to the current user
        if(mChatList.get(position).getFrom().equals(mAuth.getCurrentUser().getUid())){
            return 0;
        }
        return 1;
    }

    public class myChatViewHolder extends RecyclerView.ViewHolder{

        public myChatViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    // this Recycler View holder is for sender
    public class SenderViewHolder extends RecyclerView.ViewHolder{

        private TextView mMessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.sender_message_textView);

        }
    }

    // this Recycler View holder is for receiver
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        private TextView mMessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.receiver_message_textView);
        }
    }



}
