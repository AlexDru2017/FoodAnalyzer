package com.example.final_project2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity implements MyAdapter.OnItemClickListener {
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1000;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;


    //// to use in the profile page/////
    private String loginEmail;
    private Button About;
    private Button Menu;

    private ArrayList<Meal> mMeal;
    private ProgressDialog mProgressDialog;


    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser user;

    private ValueEventListener mDBListener;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        String login = getIntent().getStringExtra("login");
        if (login != null) {
            if (!login.matches("")) {
                loginEmail = login;
            }
        }
        mRecyclerView = findViewById(R.id.recycler_view);
        About = (Button) findViewById(R.id.btnAbout);
        Menu = (Button) findViewById(R.id.btnMenu1);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mAdapter = new MyAdapter(History.this, mUploads);

        mRecyclerView.setLayoutManager(new GridLayoutManager(History.this, 2));

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(History.this);
        user = FirebaseAuth.getInstance().getCurrentUser();

        mStorage = FirebaseStorage.getInstance();
        //  mDatabaseRef = FirebaseDatabase.getInstance().getReference("images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images/" + user.getUid());
////////////////////////////////////////////////mDatabaseRef
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }


                mProgressCircle.setVisibility(View.INVISIBLE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(History.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onOptionsItemSelected_menu_about");
                AlertDialog alertDialog = new AlertDialog.Builder(History.this).create();
                alertDialog.setTitle("About");
                alertDialog.setMessage("Food Analyzer\n\nWas created by:\nBar Plaisant\nShani Harris\n\nSupervisor:\n Prof. Zeev Volkovich\n\n©2020 All Rights Reserved");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(History.this, "menu prasses", Toast.LENGTH_LONG).show();

                PopupMenu pm = new PopupMenu(History.this, Menu);
                pm.getMenuInflater().inflate(R.menu.menu_layout, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_page_item:
                                Intent intentRegisterHomePage = new Intent(History.this, HomePage.class);
                                startActivity(intentRegisterHomePage);
                                return true;

                            case R.id.upload_item:
                                Intent intentRegisterUpload = new Intent(History.this, ChooseModel.class);
                                startActivity(intentRegisterUpload);
                                return true;

                            case R.id.history_item:
                                //This page. Don't do nothing

                                return true;

                            case R.id.program_diet_item:

                                // need to do
                                Toast.makeText(History.this, "program diet", Toast.LENGTH_LONG).show();
                                dietProgram();
                                return true;

                            case R.id.settings_item:
                                // need to do
                                Toast.makeText(History.this, "Need to do", Toast.LENGTH_LONG).show();
                                return true;

                            case R.id.logout_item:
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                return true;


                        }

                        return true;
                    }
                });

                pm.show();

            }
        });


    }

    private void dietProgram() {
        mUploads = new ArrayList<>();
        mMeal = new ArrayList<>();
        readData(FirebaseDatabase.getInstance().getReference("images/" + user.getUid()), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                    // nutritionalValuesFromFirebase(upload.getName());
                    readData(FirebaseDatabase.getInstance().getReference().child("nutritional_values").child(upload.getName()), new OnGetDataListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(DataSnapshot data) {
                            FoodModel foodModel = data.getValue(FoodModel.class);
                            Log.d("MealMenu", data.getValue(FoodModel.class).toString());
                            // [START_EXCLUDE]
                            if (foodModel == null) {
                                // User is null, error out
                                Toast.makeText(History.this, "No Data", Toast.LENGTH_LONG).show();
                            } else {
                                mMeal.add(new Meal(upload.getName(), foodModel.getEnergy()));
                            }
                            if (mUploads.size() == mMeal.size()) {
                                mProgressDialog.dismiss();
                                Intent intentProgramDiet = new Intent(History.this, MealMenu.class);
                                Bundle args = new Bundle();
                                args.putSerializable("mMeal", (Serializable) mMeal);
                                args.putSerializable("mUploads", (Serializable) mUploads);
                                intentProgramDiet.putExtra("BUNDLE", args);
                                // intentProgramDiet.putParcelableArrayListExtra("mMeal", mMeal);
                                //intentProgramDiet.putParcelableArrayListExtra("mUploads", mUploads);
                                startActivity(intentProgramDiet);
                            }
                        }

                        @Override
                        public void onFailed(DatabaseError databaseError) {

                        }
                    });
                    // mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }

            @Override
            public void onStart() {
                //when starting
                mProgressDialog = new ProgressDialog(History.this);
                mProgressDialog.setMessage("Retrieving data...");
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.show();
            }
        });

    }

    public void readData(DatabaseReference mDatabase, final OnGetDataListener listener) {
        listener.onStart();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }



    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onZoomInClick(int position) {
        Toast.makeText(this, "ZoomIn click at position: " + position, Toast.LENGTH_SHORT).show();
        Intent intentZoomin = new Intent(History.this, ZoomInActivity.class);
        intentZoomin.putExtra("ImageUrl", mUploads.get(position).getImageUrl());
        intentZoomin.putExtra("ImageName", mUploads.get(position).getName());
        startActivity(intentZoomin);
    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(this, "Delete click at position: " + position, Toast.LENGTH_SHORT).show();
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(History.this, "Item delete", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }


    /////////////////to the download function///////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}