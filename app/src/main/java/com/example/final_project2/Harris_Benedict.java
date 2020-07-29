package com.example.final_project2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Harris_Benedict extends AppCompatActivity {

    private TextView age;
    private TextView weight;
    private TextView height;

    private RadioGroup radioGroup;
    private RadioButton radioMale;
    private RadioButton radioFemale;

    private Button test;

    private Spinner spinner;

    private Double calorieConsumption;

    private ArrayList<Double> sportActivityArray;


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harris_benedict);
        setSpinner();
        init();
    }

    private void init() {
        age = findViewById(R.id.editTextAge);
        weight = findViewById(R.id.editTextWeight);
        height = findViewById(R.id.editTextHeight);
        radioGroup = findViewById(R.id.radioGroupGender);
        sportActivityArray=new ArrayList<>();
        test = findViewById(R.id.buttonTest);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sAge;
                int sWeight;
                int sHeight;
                boolean isMale = false;
                long sportActivity;

                if (!age.getText().toString().matches("")) {
                    sAge = Integer.parseInt(age.getText().toString());
                } else {
                    Toast.makeText(Harris_Benedict.this, "Please enter age", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!weight.getText().toString().matches("")) {
                    sWeight = Integer.parseInt(weight.getText().toString());
                } else {
                    Toast.makeText(Harris_Benedict.this, "Please enter weight", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!height.getText().toString().matches("")) {
                    sHeight = Integer.parseInt(height.getText().toString());
                } else {
                    Toast.makeText(Harris_Benedict.this, "Please enter height", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case -1:
                        Toast.makeText(Harris_Benedict.this, "Please choose your gender", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_Male:
                        isMale = true;
                        break;
                    case R.id.rb_female:
                        isMale = false;
                        break;
                }
                sportActivity = spinner.getSelectedItemId();
                if (isMale) {
                    //BMR = 66.5 + ( 13.75 × weight in kg ) + ( 5.003 × height in cm ) – ( 6.755 × age in years )
                } else {

                }
            }
        });
    }

    private void setSpinner() {
        spinner = (Spinner) findViewById(R.id.exercise_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.diet_plan, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void hbEquation() {

    }
}
