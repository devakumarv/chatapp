package com.example.androidchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidchat.adapter.ChatRecyclerAdapter;
import com.example.androidchat.domin.Chat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mChatRecyclerView;
    private EditText mChatText;
    private ImageView mSendImg;

    private FirebaseAuth mAuth;
    private FirebaseFirestore myFireStore;

    private ChatRecyclerAdapter mChatRecyclerAdapter;

    private String receiver_id="";

    private List<Chat> mChatList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toast.makeText(this, getIntent().getStringExtra("receiver_id"), Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        myFireStore = FirebaseFirestore.getInstance();

        receiver_id = getIntent().getStringExtra("receiver_id");



        mChatRecyclerView = findViewById(R.id.chat_recycler);
        mChatText = findViewById(R.id.chat_msg);
        mSendImg = findViewById(R.id.chat_send_img);

        mChatList = new ArrayList<>();
        mChatRecyclerAdapter = new ChatRecyclerAdapter(ChatActivity.this, mChatList);

        mChatRecyclerView.setHasFixedSize(true);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setAdapter(mChatRecyclerAdapter);

        //fetch the messages in ascending order by time stamp of the messages
        //for listening dynamically we use snapshot listner-->means listen continuesly like whats app chat messages
        // addSnapshotListener real time data listener
        myFireStore.collection("UsersMessages").orderBy("time_stamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange doc : value.getDocumentChanges()){
                    DocumentSnapshot snapshot=doc.getDocument();
                    Chat chat = snapshot.toObject(Chat.class);
                    // checking whether the conversation is between the from and to users
                    if(chat.getFrom().equals(mAuth.getCurrentUser().getUid()) && chat.getTo().equals(receiver_id)
                            ||(chat.getTo().equals(mAuth.getCurrentUser().getUid()) && chat.getFrom().equals(receiver_id))){
                        mChatList.add(chat);
                        mChatRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        // if user click on the send image then message should be store  message, from, to
        mSendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mChatText.getText().toString().isEmpty()){
                    Map<String, Object> mMap = new HashMap<>();
                    mMap.put("message", mChatText.getText().toString());
                    mMap.put("from", mAuth.getCurrentUser().getUid());
                    mMap.put("to", receiver_id);
                    mMap.put("time_stamp", new Date());
                    myFireStore.collection("UsersMessages").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if(task.isSuccessful()){
                                mChatText.setText("");
                                Toast.makeText(ChatActivity.this, "message Sent", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });


    }
}