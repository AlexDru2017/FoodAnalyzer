package com.example.final_project2;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import usdaFood.app.USDApp;
import usdaFood.usda.USDAClient;

public class NutritionalValues extends AppCompatActivity {

    //Menu && About
    private Button About;
    private Button Menu;

    //save image in data base
    private Button save_button;

    private TextView protein;
    private TextView energy;
    private TextView sugars;
    private TextView carbohydrate;
    private TextView fat;
    private TextView foodName;

    private ImageView selected_image;

    private Uri fileName;
    private Uri filePathImage;

    private String foodLabel;
    private String foodNdbno;

    private String id_Protein;
    private String value_Protein;

    private String id_Energy;
    private String value_Energy;

    private String id_Sugars;
    private String value_Sugars;

    private String id_Fat;
    private String value_fat;

    private String id_Carbohydrate;
    private String value_Carbohydrate;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutritional_values);

        About = (Button) findViewById(R.id.btnAbout);
        Menu = (Button) findViewById(R.id.btnMenu1);

        protein = (TextView) findViewById(R.id.proteinTxt_space);
        energy = (TextView) findViewById(R.id.EnergyTxt_space);
        sugars = (TextView) findViewById(R.id.SugarsTxt_space);
        carbohydrate = (TextView) findViewById(R.id.CarbohydrateTxt_space);
        fat = (TextView) findViewById(R.id.fatTxt_space);
        foodName = (TextView) findViewById(R.id.nameFoodTxt);

        // allows user to go back to activity to select a different image
        save_button = (Button) findViewById(R.id.save_button);

        selected_image = (ImageView) findViewById(R.id.selected_image);

        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("images");

        foodLabel = (String) getIntent().getStringExtra("label");
        foodNdbno = (String) getIntent().getStringExtra("ndbno");

        fileName = getIntent().getParcelableExtra("fileName");
        filePathImage = getIntent().getParcelableExtra("filePathImage");

        foodName.setText(foodLabel);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathImage);
            selected_image.setImageBitmap(bitmap);
            // not sure why this happens, but without this the image appears on its side
            selected_image.setRotation(selected_image.getRotation());
        } catch (IOException e) {
            e.printStackTrace();
        }

        searchItemMeasureStringRequestlabelNutrients(foodNdbno);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageToFirebase();
            }
        });

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NutritionalValues.this, "menu prasses", Toast.LENGTH_LONG).show();

                PopupMenu pm = new PopupMenu(NutritionalValues.this, Menu);
                pm.getMenuInflater().inflate(R.menu.menu_layout, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_page_item:
                                Intent intentHomePage = new Intent(NutritionalValues.this, HomePage.class);
                                startActivity(intentHomePage);
                                return true;

                            case R.id.upload_item:
                                Intent intentUpload = new Intent(NutritionalValues.this, ChooseModel.class);
                                startActivity(intentUpload);
                                return true;

                            case R.id.history_item:
                                Intent intentHistory = new Intent(NutritionalValues.this, History.class);
                                startActivity(intentHistory);
                                return true;

                            case R.id.program_diet_item:

                                // need to do
                                Toast.makeText(NutritionalValues.this, "Need to do", Toast.LENGTH_LONG).show();
                                return true;

                            case R.id.settings_item:
                                // need to do
                                Toast.makeText(NutritionalValues.this, "Need to do", Toast.LENGTH_LONG).show();
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
                AlertDialog alertDialog = new AlertDialog.Builder(NutritionalValues.this).create();
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

    private void searchItemMeasureStringRequest(String ndbno) {
        try {
            USDApp usdApp = new USDApp();
            USDAClient usdaClient = usdApp.getUSDAClient();
            JSONArray jsonArray = usdaClient.searchFoodReport(ndbno);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            JSONArray foodNutrients = jsonObject.getJSONArray("foodNutrients");
            for (int i = 0; i < foodNutrients.length(); i++) {
                JSONObject foodNutrient = (JSONObject) foodNutrients.get(i);
                JSONObject nutrient = (JSONObject) foodNutrient.get("nutrient");
                String nutrient_id = (String) nutrient.get("number");
                switch (nutrient_id) {
                    case "203":
                        id_Protein = (String) nutrient.get("name");
                        value_Protein = foodNutrient.get("amount").toString();
                        protein.setText(value_Protein);
                        break;
                    case "208":
                        id_Energy = (String) nutrient.get("name");
                        value_Energy = foodNutrient.get("amount").toString();
                        energy.setText(value_Energy);
                        break;
                    case "269":
                        id_Sugars = (String) nutrient.get("name");
                        value_Sugars = foodNutrient.get("amount").toString();
                        sugars.setText(value_Sugars);
                        break;
                    case "204":
                        id_Fat = (String) nutrient.get("name");
                        value_fat = foodNutrient.get("amount").toString();
                        fat.setText(value_fat);
                        break;
                    case "205":
                        id_Carbohydrate = (String) nutrient.get("name");
                        value_Carbohydrate = foodNutrient.get("amount").toString();
                        carbohydrate.setText(value_Carbohydrate);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // need to change the "g" in the layout values;
    private void searchItemMeasureStringRequestlabelNutrients(String ndbno) {
        try {
            USDApp usdApp = new USDApp();
            USDAClient usdaClient = usdApp.getUSDAClient();
            JSONArray jsonArray = usdaClient.searchFoodReport(ndbno);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            try {
                JSONObject labelNutrients = jsonObject.getJSONObject("labelNutrients");
                id_Protein = "Protein";
                value_Protein = ((JSONObject) labelNutrients.get("protein")).get("value").toString();
                protein.setText(value_Protein);

                id_Energy = "Energy";
                value_Energy = ((JSONObject) labelNutrients.get("calories")).get("value").toString();
                energy.setText(value_Energy);

                id_Sugars = "Sugars";
                value_Sugars = ((JSONObject) labelNutrients.get("sugars")).get("value").toString();
                sugars.setText(value_Sugars);

                id_Fat = "Fat";
                value_fat = ((JSONObject) labelNutrients.get("fat")).get("value").toString();
                fat.setText(value_fat);

                id_Carbohydrate = "Carb";
                value_Carbohydrate = ((JSONObject) labelNutrients.get("carbohydrates")).get("value").toString();
                carbohydrate.setText(value_Carbohydrate);
            } catch (JSONException e) {
                JSONArray foodNutrients = jsonObject.getJSONArray("foodNutrients");
                for (int i = 0; i < foodNutrients.length(); i++) {
                    JSONObject foodNutrient = (JSONObject) foodNutrients.get(i);
                    JSONObject nutrient = (JSONObject) foodNutrient.get("nutrient");
                    String nutrient_id = (String) nutrient.get("number");
                    switch (nutrient_id) {
                        case "203":
                            id_Protein = (String) nutrient.get("name");
                            value_Protein = foodNutrient.get("amount").toString();
                            protein.setText(value_Protein);
                            break;
                        case "208":
                            id_Energy = (String) nutrient.get("name");
                            value_Energy = foodNutrient.get("amount").toString();
                            energy.setText(value_Energy);
                            break;
                        case "269":
                            id_Sugars = (String) nutrient.get("name");
                            value_Sugars = foodNutrient.get("amount").toString();
                            sugars.setText(value_Sugars);
                            break;
                        case "204":
                            id_Fat = (String) nutrient.get("name");
                            value_fat = foodNutrient.get("amount").toString();
                            fat.setText(value_fat);
                            break;
                        case "205":
                            id_Carbohydrate = (String) nutrient.get("name");
                            value_Carbohydrate = foodNutrient.get("amount").toString();
                            carbohydrate.setText(value_Carbohydrate);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void saveImageToFirebase() {

        //save NutritionalValues on firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("nutritional_values");
        imagePath = (user.getUid() + "/" + fileName.getLastPathSegment() + "." + getExtenstion(fileName));

        FoodModel foodModel = new FoodModel(value_Energy, value_Protein, value_Sugars, value_fat, value_Carbohydrate);
        mDatabase.child(foodLabel).setValue(foodModel);

        //save image on firebase
        //checking if file is available
        if (filePathImage != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Saving Image in DataBase");
            progressDialog.show();


            //getting the storage reference
            final StorageReference sRef = storageReference.child(imagePath);
            mDatabase = FirebaseDatabase.getInstance().getReference("images");


            //adding the file to reference
            sRef.putFile(filePathImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();
                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    String imageName = foodLabel;

                                    Upload upload = new Upload(imageName, url);
                                    //String uploadId = mDatabase.push().getKey();
                                    String uploadId = fileName.getLastPathSegment();
                                    mDatabase.child(user.getUid() + "/" + uploadId).setValue(upload);
                                }
                            });

                            Toast.makeText(getApplicationContext(), "Saving Image ", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Saving Image " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }


    private String getExtenstion(Uri uri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}

