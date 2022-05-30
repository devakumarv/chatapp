package com.example.androidchat.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidchat.HomeActivity;
import com.example.androidchat.MainActivity;
import com.example.androidchat.R;
import com.example.androidchat.SplashScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText mName;
    private EditText mBirthDate;
    private EditText mEmail;
    private EditText mPassword;
    private Button mSignup_btn;

    private int age;
    private String sBirth;

    private ProgressDialog mProgressDialog;

    private FirebaseFirestore myFireStore;


    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        myFireStore = FirebaseFirestore.getInstance();

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Sign in taking place");
        mProgressDialog.setMessage("You are going to see new World....");

        mName = view.findViewById(R.id.signup_name);
        mEmail = view.findViewById(R.id.signup_email);
        mPassword = view.findViewById(R.id.signup_password);
        mSignup_btn = view.findViewById(R.id.signup_btn);

        mBirthDate = view.findViewById(R.id.signup_birth_date);
        Calendar calendar = Calendar.getInstance();
        int YY = calendar.get(Calendar.YEAR);
        int MM = calendar.get(Calendar.MONTH);
        int DD = calendar.get(Calendar.DAY_OF_MONTH);

        mBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        sBirth = DD + "/" + MM + "/" + YY;
                        mBirthDate.setText(sBirth);
                        age = YY - year;            //YY = present year, year = selected year
                    }
                }, YY, MM, DD).show();
            }
        });

        mSignup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mName.getText())) {
                    mName.setError("Name is required");
                } else if (TextUtils.isEmpty(mBirthDate.getText())) {
                    mBirthDate.setError("DOB required!");
                } else if (TextUtils.isEmpty(mEmail.getText())) {
                    mEmail.setError("Email required!");
                } else if (TextUtils.isEmpty(mPassword.getText())) {
                    mPassword.setError("Password required!");
                } else {
                    mProgressDialog.show();
                    String sEmail = mEmail.getText().toString();
                    String sPassword = mPassword.getText().toString();

                    //while after sign up email, password are saved by mAuth while creating but for saving name we have to create other
                    // new collection and save it by mYFireStore
                    mAuth.createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> mMap = new HashMap<>();
                                mMap.put("name", mName.getText().toString());
                                mMap.put("age", age);
                                mMap.put("BirthDate", sBirth);

                                myFireStore.collection("OurUsers").document(mAuth.getCurrentUser().getUid())
                                        .set(mMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mProgressDialog.dismiss();
                                            Toast.makeText(getContext(), "SuccessFul", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(), HomeActivity.class);
                                            //before starting clear all intent before we used
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            getActivity().finish();

                                        }

                                    }
                                });

                            } else {
                                mProgressDialog.dismiss();
                                Toast.makeText(getContext(), "UnSuccess " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }


        });

        return view;
    }
}