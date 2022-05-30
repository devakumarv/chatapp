package com.example.androidchat.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androidchat.MainActivity;
import com.example.androidchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {


    CircleImageView mProfileImg;   //  This view is from git hub for circle the image(Not by default in android)
    TextView mName;
    EditText mHobby;
    EditText mLang;
    EditText mDesc;
    ChipGroup mHobbyChipGroup;
    ChipGroup mLangChipGroup;
    List<String> mHobbyList;
    List<String > mLangList;
    Button mLogOutBtn;
    Button mSaveProfBtn;
    Uri mProImgUri=null;
    private FirebaseAuth mAuth;
    private FirebaseFirestore myFireStore;
    private StorageReference myFireStorage;      //this is use for storing images


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        myFireStore = FirebaseFirestore.getInstance();
        myFireStorage = FirebaseStorage.getInstance().getReference();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mProfileImg = view.findViewById(R.id.pro_image);
        mName = view.findViewById(R.id.pro_name);
        mHobby = view.findViewById(R.id.pro_hobby);
        mLang = view.findViewById(R.id.pro_lang);
        mDesc = view.findViewById(R.id.pro_desc);
        mHobbyChipGroup = view.findViewById(R.id.hobby_chip_group);
        mLangChipGroup = view.findViewById(R.id.lang_chip_group);
        mLogOutBtn = view.findViewById(R.id.logout_btn);
        mSaveProfBtn = view.findViewById(R.id.save_pro_btn);

        mHobbyList = new ArrayList<>();      //all strings for hobbies
        displayHobbyChipList(mHobbyList);         // to display all the hobbies

        mLangList = new ArrayList<>();      //all strings for language
        displayLangChipList(mLangList);         // to display all the language

        getUserProfileData();       // to get all the information of the user in the profile page

        //click on the profile image then have to ask from internal storage
        mProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });



        //imeOption is for it like in keyboard we can get button for what action to be take when they click on it
        // this is use to listen when they click on the imeoption for hobby
        mHobby.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO){
                    mHobbyList.add(mHobby.getText().toString());
                    mHobby.setText("");     // after adding into the list make the text view empty
                    displayHobbyChipList(mHobbyList);         // to display all the hobbies in chip wise
                    return true;
                }
                return false;
            }
        });

        // for language
        mLang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO){
                    mLangList.add(mLang.getText().toString());
                    mLang.setText("");
                    displayLangChipList(mLangList);
                    return true;
                }
                return false;
            }
        });

        //For logOut Button
        mLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });

        //save data
        mSaveProfBtn.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //upload the image to Storage in firebase
                // to put image name , just name it as same as time stamp , save image in the ProfileImg folder


                // Here progress dialog will start and end when user go into
                ProgressDialog dialog = ProgressDialog.show(getContext(), "Loading joo...", "Please wait...", true);
                Long timeStampLo = System.currentTimeMillis()/1000;
                String timStam = timeStampLo.toString();
                if(mProImgUri != null){
                    // first save the profile pic in the storage , then save image url and hobby, language, desc in the fire store
                    myFireStorage.child("ProfileImages/"+timStam+"/").putFile(mProImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        // If storing the image in the Storage is successful hen save image url and hobby, language, desc in the fire store
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // getting a image uri snapshot from the Storage, if it is successful hen save image url and hobby, language, desc in the fire store
                            Task<Uri> res = taskSnapshot.getStorage().getDownloadUrl();
                            res.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUri = uri.toString();        // this is use to get the image uri to string from the storage

                                    Map<String, Object> mMap = new HashMap<>();
                                    mMap.put("UserHobby", String.join(",", mHobbyList));                //convert list to string based on comma
                                    mMap.put("UserLanguage", String.join(",", mLangList));              //convert list to string based on comma
                                    mMap.put("UserDescription", mDesc.getText().toString());
                                    mMap.put("UserImgUrl", downloadUri);

                                    //save image url and hobby, language, desc in the OurUsers Table (collection)
                                    myFireStore.collection("OurUsers").document(mAuth.getCurrentUser().getUid())
                                            .update(mMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                                        Log.i("Probha", "Comp");
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });
                                }
                            });

                        }
                    });


                }else{
                    // there is no profile image update, so update remaining fields

                    Map<String, Object> mMap = new HashMap<>();
                    mMap.put("UserHobby", String.join(",", mHobbyList));                //convert list to string based on comma
                    mMap.put("UserLanguage", String.join(",", mLangList));              //convert list to string based on comma
                    mMap.put("UserDescription", mDesc.getText().toString());

                    //save image url and hobby, language, desc in the OurUsers Table (collection)
                    myFireStore.collection("OurUsers").document(mAuth.getCurrentUser().getUid())
                            .update(mMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                        Log.i("Probha", "Comp");
                                        dialog.dismiss();
                                    }
                                }
                            });
                }

            }
        });


        return view;
    }


    //for picking image from te gallery for profile pic
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            final Uri imageUri = data.getData();
            if(imageUri != null){
                mProImgUri = imageUri;
                Glide.with(getContext()).load(imageUri).into(mProfileImg);
            }else {
                mProImgUri = null;
            }
        }else {
            Toast.makeText(getContext(), "No image Picked", Toast.LENGTH_SHORT).show();
        }


    }

    private void displayHobbyChipList(List<String> mHobbyList) {
        mHobbyChipGroup.removeAllViews();            //remove all the chips if any present
        for(String s : mHobbyList) {
            if (getActivity() != null) {

                Chip chip = (Chip) getActivity().getLayoutInflater().inflate(R.layout.single_chip_item, null, false);
                chip.setText(s);
                mHobbyChipGroup.addView(chip);

                //if he log click on the chip then delete it
                chip.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mHobbyChipGroup.removeView(v);           // remove the chip from the Chip group display
                        Chip c = (Chip) v;                  // get the chip
                        Toast.makeText(getContext(), c.getText().toString() + " removed", Toast.LENGTH_SHORT).show();
                        mHobbyList.remove(c.getText().toString());   // remove the chip name in the chip list
                        return true;
                    }
                });
            }
        }
    }


    private void displayLangChipList(List<String> mLangList) {
        mLangChipGroup.removeAllViews();
        for (String s : mLangList) {
            if (getActivity() != null) {
                Chip chip = (Chip) getActivity().getLayoutInflater().inflate(R.layout.single_chip_item, null, false);
                chip.setText(s);
                mLangChipGroup.addView(chip);

                chip.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mLangChipGroup.removeView(v);
                        Chip c = (Chip) v;
                        Toast.makeText(getContext(), c.getText().toString() + " removed", Toast.LENGTH_SHORT).show();
                        mLangList.remove(c.getText().toString());
                        return true;
                    }
                });
            }
        }
    }


    // to get all the profile information of the current user
    private void getUserProfileData() {
        myFireStore.collection("OurUsers").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    if (task.isSuccessful() && null != task.getResult()) {
                        String nam = task.getResult().getString("name");
                        String img_url = task.getResult().getString("UserImgUrl");
                        String hob = task.getResult().getString("UserHobby");
                        String lan = task.getResult().getString("UserLanguage");
                        String des = task.getResult().getString("UserDescription");

                        Log.i("njn", "mkmk" + mAuth.getCurrentUser().getUid());

                        if (nam != null && !nam.isEmpty()) {
                            //load the name
                            mName.setText(nam);
                        }

                        if (des != null && !des.isEmpty()) {
                            //load the data
                            mDesc.setText(des);
                        }


                        if (hob != null && !hob.isEmpty()) {
                            List<String> hobList = Arrays.asList(hob.split("\\s*,\\s*"));     //convert string to list based on comma
                            mHobbyList.addAll(hobList);             // adding all hob strings list into our hobby list which we used
                            displayHobbyChipList(mHobbyList);           //to display hobby like chips
                        }

                        if (lan != null && !lan.isEmpty()) {
                            List<String> lanList = Arrays.asList(lan.split("\\s*,\\s*"));     //convert string to list based on comma
                            mLangList.addAll(lanList);             // adding all hob strings list into our lan list which we used
                            displayLangChipList(mLangList);             //to display lang like chips
                        }

                        if (img_url != null) {
                            Glide.with(requireContext()).load(img_url).into(mProfileImg);       // to load image in the mProfileImg image view
                        }

                    }

                } catch (Exception e) {
                    Log.e(e.getMessage(), "Exception occurred while loading fragments");
                    e.printStackTrace();
                }
            }
        });
    }


}