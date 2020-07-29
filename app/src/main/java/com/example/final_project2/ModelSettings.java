package com.example.final_project2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ModelSettings extends AppCompatActivity {

    private Button btnChooseSettings;
    private Button btnChooseLabel;
    private Button btnSaveModel;

    private String model;
    private String label;


    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_model);

        btnChooseSettings = findViewById(R.id.chooseModel);
        btnChooseLabel = findViewById(R.id.chooseLabel);
        btnSaveModel = findViewById(R.id.saveModel);

        btnChooseSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getModel();
            }
        });

        btnChooseLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLabel();
            }
        });
        btnSaveModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateModelSettings();
            }
        });

    }

    private void updateModelSettings() {
        if (model == null) {
            Toast.makeText(ModelSettings.this, "Please pick model", Toast.LENGTH_LONG).show();
            return;
        }
        if (label == null) {
            Toast.makeText(ModelSettings.this, "Please pick label file", Toast.LENGTH_LONG).show();
            return;
        }

        Classify.setChosen(model);
        Classify.setLabels(label);
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Do you really want to whatever?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(ModelSettings.this, "Saved successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "onActivityResult");
        //return from file choose
        if (requestCode == 123 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mSelectedfile = data.getData(); //The uri with the location of the file
            model = (mSelectedfile.toString());
            Log.d("model location", model.toString());
        }
        if (requestCode == 124 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mSelectedfile = data.getData(); //The uri with the location of the file
            label = (mSelectedfile.toString());
            Log.d("label location", label.toString());
        }
    }


    private void getModel() {
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }

    private void getLabel() {
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), 124);
    }
}

