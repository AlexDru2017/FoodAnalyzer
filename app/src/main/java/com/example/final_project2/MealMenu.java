package com.example.final_project2;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.lang.UProperty;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.Random;


public class MealMenu extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1000;

    private TextView labGene1;
    private TextView labGene2;
    private TextView labGene3;
    private TextView labGene4;
    private TextView labGene5;

    private TextView cal1;
    private TextView cal2;
    private TextView cal3;
    private TextView cal4;
    private TextView cal5;



    private Button About;
    private Button Menu;


    private Population population;
    private int generationCount;


    private FirebaseUser user;

    private ArrayList<Upload> mUploads;

    private static ArrayList<Meal> mMeal;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MealMenu", "onCreate: " + "MealMenu");
        setContentView(R.layout.meal_menu_layout);
        Bundle args = getIntent().getBundleExtra("BUNDLE");
        mUploads = (ArrayList<Upload>) args.getSerializable("mUploads");
        mMeal = (ArrayList<Meal>) args.getSerializable("mMeal");
        user = FirebaseAuth.getInstance().getCurrentUser();
        population = new Population();
        generationCount = 0;
        int numOfGeneration = 0;

        About = (Button) findViewById(R.id.btnAbout);
        Menu = (Button) findViewById(R.id.btnMenu1);

        labGene1 = (TextView) findViewById(R.id.labgene1);
        labGene2 = (TextView) findViewById(R.id.labgene2);
        labGene3 = (TextView) findViewById(R.id.labgene3);
        labGene4 = (TextView) findViewById(R.id.labgene4);
        labGene5 = (TextView) findViewById(R.id.labgene5);

        cal1 = (TextView) findViewById(R.id.cal1);
        cal2 = (TextView) findViewById(R.id.cal2);
        cal3 = (TextView) findViewById(R.id.cal3);
        cal4 = (TextView) findViewById(R.id.cal4);
        cal5 = (TextView) findViewById(R.id.cal5);


        //MealMenu demo = new MealMenu();

        //Initialize population
        population.initializePopulation(5);//לשנות ל10

        //Check if the calories the user consumes are equal to what he needs to consumes.
        Individual individual;
        while ((individual = population.checkDistance()) == null && numOfGeneration < 10) {

            selection();

            crossover();

            mutation();

            Log.d("crossover", "onCreate: ");


        }

        Log.d("individual : ", "onCreate: " + individual);



        labGene1.setText("" + individual.getGenes().get(0).mealName);
        labGene2.setText("" + individual.getGenes().get(1).mealName);
        labGene3.setText("" + individual.getGenes().get(2).mealName);
        labGene4.setText("" + individual.getGenes().get(3).mealName);
        labGene5.setText("" + individual.getGenes().get(4).mealName);


        int calForUser = 2600; //צריך לבוא מהחלון של נתוני המתשתמש , חישוב קלוריות לפי גובה משקל..

        if(Harris_Benedict.getCalConsuption()!=0)
        {
            calForUser = Harris_Benedict.getCalConsuption(); //צריך לבוא מהחלון של נתוני המתשתמש , חישוב קלוריות לפי גובה משקל..
        }

        int calIndi1 = (int)individual.getGenes().get(0).cal*10;
        int calIndi2 = (int)individual.getGenes().get(1).cal*10;
        int calIndi3 = (int)individual.getGenes().get(2).cal*10;
        int calIndi4 = (int)individual.getGenes().get(3).cal*10;
        int calIndi5 = calForUser-(calIndi1+calIndi2+calIndi3+calIndi4);

        cal1.setText("" + calIndi1+"g");
        cal2.setText("" + calIndi2+"g");
        cal3.setText("" + calIndi3+"g");
        cal4.setText("" + calIndi4+"g");
        cal5.setText("" + calIndi5+"g");





        //Toast.makeText(MealMenu.this, "aaaaaaaaa", Toast.LENGTH_LONG).show();

        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onOptionsItemSelected_menu_about");
                AlertDialog alertDialog = new AlertDialog.Builder(MealMenu.this).create();
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
                Toast.makeText(MealMenu.this, "menu prasses", Toast.LENGTH_LONG).show();

                PopupMenu pm = new PopupMenu(MealMenu.this, Menu);
                pm.getMenuInflater().inflate(R.menu.menu_layout, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_page_item:
                                Intent intentRegisterHomePage = new Intent(MealMenu.this, HomePage.class);
                                startActivity(intentRegisterHomePage);
                                return true;

                            case R.id.upload_item:
                                Intent intentRegisterUpload = new Intent(MealMenu.this, ChooseModel.class);
                                startActivity(intentRegisterUpload);
                                return true;

                            case R.id.history_item:
                                Intent intentRegisterHistory = new Intent(MealMenu.this, History.class);
                                startActivity(intentRegisterHistory);
                                return true;

                            case R.id.program_diet_item:
                                //This page. Don't do nothing



                                return true;

                            case R.id.settings_item:
                                // need to do
                                Toast.makeText(MealMenu.this, "Need to do", Toast.LENGTH_LONG).show();
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



    public void onLabelClick(int position) {
        Toast.makeText(this, "ZoomIn click at position: " + position, Toast.LENGTH_SHORT).show();
        Intent intentZoomin = new Intent(MealMenu.this, ZoomInActivity.class);
        intentZoomin.putExtra("ImageUrl", mUploads.get(position).getImageUrl());
        intentZoomin.putExtra("ImageName", mUploads.get(position).getName());
        startActivity(intentZoomin);
    }

    public static ArrayList<Meal> getmMeal() {
        return mMeal;
    }

    public static void setmMeal(ArrayList<Meal> mMeal) {
        MealMenu.mMeal = mMeal;
    }

    void selection() {
        //Calculate fitness of each individual
        population.calculateFitness();

        //create new generation of individual
        population.getNewIndividual();
    }

    private void crossover() {
        population.crossoverOfParent();
    }

    private void mutation() {
        population.mutationForPopulation();
    }


}







