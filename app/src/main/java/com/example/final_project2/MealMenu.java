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
import java.util.Random;


public class MealMenu extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1000;


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
        int numOfGeneration =0;

        //MealMenu demo = new MealMenu();

        //Initialize population
        population.initializePopulation(5);//לשנות ל10

        //Check if the calories the user consumes are equal to what he needs to consumes.
        while (population.checkDistance()==0 && numOfGeneration<10)
        {

            selection();

            crossover();




        }



        Toast.makeText(MealMenu.this, "aaaaaaaaa", Toast.LENGTH_LONG).show();


    }


    public static ArrayList<Meal> getmMeal() {
        return mMeal;
    }

    public static void setmMeal(ArrayList<Meal> mMeal) {
        MealMenu.mMeal = mMeal;
    }

    void selection()
    {
        //Calculate fitness of each individual
        population.calculateFitness();

        //create new generation of individual
        population.getNewIndividual();
    }

    private void crossover() {
        population.crossoverOfParent();
    }


}







