package com.example.androidchat.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.androidchat.ChatActivity;
import com.example.androidchat.R;
import com.example.androidchat.adapter.MatchRecyclerAdapter;
import com.example.androidchat.domin.Chat;
import com.example.androidchat.domin.Match;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MatchFragment extends Fragment {

    private RecyclerView mMatchRecyclerView;
    private List<Match> mMatchList;
    private MatchRecyclerAdapter mMatchRecyclerAdapter;

    private FirebaseAuth myFireAuth;
    private FirebaseFirestore myFireStore;

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFireStore = FirebaseFirestore.getInstance();
        myFireAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_match, container, false);
        mMatchList = new ArrayList<>();
        mMatchRecyclerView = view.findViewById(R.id.match_recycler);
        mMatchRecyclerView.setHasFixedSize(true);
        mMatchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMatchRecyclerAdapter = new MatchRecyclerAdapter(getContext(), mMatchList);
        mMatchRecyclerView.setAdapter(mMatchRecyclerAdapter);



        myFireStore.collection("OurUsers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        String docId = doc.getId();         //here doc id is same as user id
                        // we want only the images with not current user profile
                        if(!docId.equals(myFireAuth.getCurrentUser().getUid())){
                            if(task.isSuccessful()){

                                String img_url_f = doc.getString("UserImgUrl");
                                String name_f = doc.getString("name");

                                if(img_url_f == null){
                                    img_url_f = "";
                                }
                                Match match = new Match(name_f, docId, img_url_f);
                                Log.i("bhbhMatch", img_url_f);
                                mMatchList.add(match);
                                mMatchRecyclerAdapter.notifyDataSetChanged();
                            }

                        }

                    }
                }
            }
        });



        return view;
    }
}

