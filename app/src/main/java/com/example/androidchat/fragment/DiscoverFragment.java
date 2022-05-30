package com.example.androidchat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidchat.R;
import com.example.androidchat.adapter.ImageViewPagerAdapter;
import com.example.androidchat.domin.ImageItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class DiscoverFragment extends Fragment {


    FirebaseAuth mAuth;
    FirebaseFirestore mFireStore;
    ArrayList<ImageItem> mImageItems;

    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        ViewPager2 imagesViewPager = view.findViewById(R.id.imagesViewPager);
        mImageItems = new ArrayList<>();     // this will store the objects of ImageItems class

        // setting our own adapter
        Log.i("bhbh", mImageItems.size()+"ml");
        ImageViewPagerAdapter mImageAdapter = new ImageViewPagerAdapter(mImageItems, getContext());
        imagesViewPager.setAdapter(mImageAdapter);



        mFireStore.collection("OurUsers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (isAdded()) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            String docId = doc.getId();
                            // we want only the images with not current user profile
                            if (!docId.equals(mAuth.getCurrentUser().getUid())) {
                                if (task.isSuccessful()) {

                                    String img_url_f = doc.getString("UserImgUrl");
                                    String hobby_f = doc.getString("UserHobby");
                                    String name_f = doc.getString("name");
                                    if(img_url_f != null){
                                        ImageItem imageItem = new ImageItem(img_url_f, name_f, hobby_f);
                                        mImageItems.add(imageItem);
                                        mImageAdapter.notifyDataSetChanged();

                                    }

                                }

                            }

                        }
                    }
                }
            }
        });


        return view;
    }
}