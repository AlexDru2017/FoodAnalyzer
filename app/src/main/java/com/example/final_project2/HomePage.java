package com.example.final_project2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private FirebaseUser user;
    private TextView userName;
    private LinearLayout UplodePoto;
    private LinearLayout History;
    private Button About;
    private Button Menu;
    private ArrayList<Upload> mUploads;
    private ArrayList<Meal> mMeal;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_latout);

        user = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("user id", user.getUid());
        Log.d("user email", user.getEmail());

        userName = (TextView) findViewById(R.id.txtUserName);
        UplodePoto = (LinearLayout) findViewById(R.id.btnUplodePoto);
        History = (LinearLayout) findViewById(R.id.btnHistory);
        About = (Button) findViewById(R.id.btnAbout);
        Menu = (Button) findViewById(R.id.btnMenu1);

        userName.setText(user.getEmail().split("@")[0]);


        UplodePoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(HomePage.this, ChooseModel.class);
                startActivity(intentRegister);
            }
        });

        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(HomePage.this, History.class);
                startActivity(intentRegister);
            }
        });

        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onOptionsItemSelected_menu_about");
                AlertDialog alertDialog = new AlertDialog.Builder(HomePage.this).create();
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
                Toast.makeText(HomePage.this, "menu prasses", Toast.LENGTH_LONG).show();

                PopupMenu pm = new PopupMenu(HomePage.this, Menu);
                pm.getMenuInflater().inflate(R.menu.menu_layout, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_page_item:
                                //This page. Don't do nothing
                                return true;

                            case R.id.upload_item:
                                Intent intentRegisterUpload = new Intent(HomePage.this, ChooseModel.class);
                                startActivity(intentRegisterUpload);
                                return true;

                            case R.id.history_item:
                                Intent intentRegisterHistory = new Intent(HomePage.this, History.class);
                                startActivity(intentRegisterHistory);
                                return true;

                            case R.id.program_diet_item:
                                Toast.makeText(HomePage.this, "program diet item", Toast.LENGTH_LONG).show();
                                dietProgram();

                                // need to do
                                return true;

                            case R.id.settings_item:
                                // need to do
                                Toast.makeText(HomePage.this, "Need to do", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(HomePage.this, "No Data", Toast.LENGTH_LONG).show();
                            } else {
                                mMeal.add(new Meal(upload.getName(), foodModel.getEnergy()));
                            }
                            if (mUploads.size() == mMeal.size()) {
                                mProgressDialog.dismiss();
                                Intent intentProgramDiet = new Intent(HomePage.this, MealMenu.class);
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
                mProgressDialog = new ProgressDialog(HomePage.this);
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


}
