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

    private Button test;

    private Spinner spinner;

    private Double bmr;
    private static int calConsuption;

    private double[] sportActivityArray;


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
        sportActivityArray = new double[]{1.2, 1.375, 1.55, 1.725, 1.9};
        test = findViewById(R.id.buttonTest);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sAge;
                int sWeight;
                int sHeight;
                boolean isMale = false;
                int sportActivity;

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
                sportActivity = (int) spinner.getSelectedItemId();
                if (isMale) {
                    //BMR = 66.5 + ( 13.75 × weight in kg ) + ( 5.003 × height in cm ) – ( 6.755 × age in years )
                    bmr = 66.5 + (13.75 * sWeight) + (5.003 * sHeight) - (6.755 * sAge);
                } else {
                    // BMR = 655 + (9.563 ×weight in kg )+(1.850 ×height in cm ) –(4.676 ×age in years )
                    bmr = 665 + (9.563 * sWeight) + (1.850 * sHeight) - (4.676 * sAge);
                }
                calConsuption = (int) (bmr * sportActivityArray[sportActivity] * 1.1);
                Toast.makeText(Harris_Benedict.this, Integer.toString(calConsuption), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static int getCalConsuption() {
        return calConsuption;
    }

    public static void setCalConsuption(int calConsuption) {
        Harris_Benedict.calConsuption = calConsuption;
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
}
