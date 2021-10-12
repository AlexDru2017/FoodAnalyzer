package com.example.final_project2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import usdaFood.app.USDApp;

public class AddFoodItems extends AppCompatActivity {
    // creating que queue object

    // list for food
    private List<FoodItem> foodItems;
    private ListView foodItemsListView;
    private String labelFood;

    //Menu && About
    private Button About;
    private Button Menu;

    private ArrayList<Meal> mMeal;
    private ProgressDialog mProgressDialog;
    private ArrayList<Upload> mUploads;
    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_items);

        user = FirebaseAuth.getInstance().getCurrentUser();


        About = (Button) findViewById(R.id.btnAbout);
        Menu = (Button) findViewById(R.id.btnMenu1);

        foodItems = new ArrayList<>();
        foodItems.clear();

        foodItemsListView = (ListView) findViewById(R.id.foodItemsListView);
        foodItemsListView.setAdapter(null);


        labelFood = (String) getIntent().getStringExtra("label");
//        USDApp test = new USDApp();
        try {
            JSONObject answer = new JSONObject(getIntent().getStringExtra("answer"));
            JSONArray item = answer.getJSONArray("foods");
            String name = "";
            String group = "";
            String ndbno = "";
            for (int i = 0; i < item.length(); i++) {
                JSONObject index = (JSONObject) item.get(i);
                try {
                    name = index.getString("description");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    group = index.getString("brandOwner");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    ndbno = index.getString("fdcId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FoodItem fi = new FoodItem(name, group, ndbno, "");
                foodItems.add(fi);
            }
            Log.d("here -----", "here");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FoodItemListAdapter adapter = new FoodItemListAdapter(AddFoodItems.this, R.layout.list_food_item, foodItems);

        foodItemsListView.setAdapter(adapter);

        foodItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ndbnoVar = (String) foodItems.get(position).getItemNDB();
                Intent i = new Intent(AddFoodItems.this, NutritionalValues.class);
                i.putExtra("fileName", getIntent().getParcelableExtra("fileName"));
                i.putExtra("filePathImage", getIntent().getParcelableExtra("filePathImage"));
                i.putExtra("label", labelFood);
                i.putExtra("ndbno", ndbnoVar);

                // send other required data
                startActivity(i);
            }
        });

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddFoodItems.this, "menu prasses", Toast.LENGTH_LONG).show();

                PopupMenu pm = new PopupMenu(AddFoodItems.this, Menu);
                pm.getMenuInflater().inflate(R.menu.menu_layout, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_page_item:
                                Intent intentHomePage = new Intent(AddFoodItems.this, HomePage.class);
                                startActivity(intentHomePage);
                                return true;

                            case R.id.upload_item:
                                Intent intentUpload = new Intent(AddFoodItems.this, ChooseModel.class);
                                startActivity(intentUpload);
                                return true;

                            case R.id.history_item:
                                Intent intentHistory = new Intent(AddFoodItems.this, History.class);
                                startActivity(intentHistory);
                                return true;

                            case R.id.program_diet_item:

                                Toast.makeText(AddFoodItems.this, "program diet", Toast.LENGTH_LONG).show();
                                dietProgram();
                                return true;

                            case R.id.settings_item:
                                // need to do
                                Toast.makeText(AddFoodItems.this, "Need to do", Toast.LENGTH_LONG).show();
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

        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onOptionsItemSelected_menu_about");
                AlertDialog alertDialog = new AlertDialog.Builder(AddFoodItems.this).create();
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
                                Toast.makeText(AddFoodItems.this, "No Data", Toast.LENGTH_LONG).show();
                            } else {
                                mMeal.add(new Meal(upload.getName(), foodModel.getEnergy()));
                            }
                            if (mUploads.size() == mMeal.size()) {
                                mProgressDialog.dismiss();
                                Intent intentProgramDiet = new Intent(AddFoodItems.this, MealMenu.class);
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
                mProgressDialog = new ProgressDialog(AddFoodItems.this);
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