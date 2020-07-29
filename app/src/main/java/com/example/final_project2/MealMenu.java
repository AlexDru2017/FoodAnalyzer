package com.example.final_project2;


import android.app.ProgressDialog;
import android.icu.lang.UProperty;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class MealMenu extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1000;

    private FirebaseUser user;

    private ArrayList<Upload> mUploads;

    private ArrayList<Meal> mMeal;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MealMenu", "onCreate: " + "MealMenu");
        setContentView(R.layout.meal_menu_layout);
        Bundle args = getIntent().getBundleExtra("BUNDLE");
        mUploads = (ArrayList<Upload>) args.getSerializable("mUploads");
        mMeal = (ArrayList<Meal>) args.getSerializable("mMeal");
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

}


