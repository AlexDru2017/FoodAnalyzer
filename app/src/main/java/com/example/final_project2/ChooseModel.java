package com.example.final_project2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ChooseModel extends AppCompatActivity {

    // button for each available classifier
    private Button inceptionFloat;
    private Button inceptionQuant;
    private Button moveToLogin;
    private Button buttonChoose;
    private ImageView imageView;
    private Button About;
    private Button Menu;

    private ArrayList<Meal> mMeal;
    private ProgressDialog mProgressDialog;
    private ArrayList<Upload> mUploads;
    private FirebaseUser user;

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    // for permission requests
    public static final int REQUEST_PERMISSION = 300;

    // request code for permission requests to the os for image
    public static final int REQUEST_IMAGE = 100;

    // will hold uri of image obtained from camera
    private Uri imageUri;

    // string to send to next activity that describes the chosen classifier
    private String chosen;

    //Strung to know the source destination of the picture
    private Uri sourcDest;

    //boolean value dictating if chosen model is quantized version or not.
    private boolean quant;


    private String angle = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_model);


        user = FirebaseAuth.getInstance().getCurrentUser();


        // request permission to use the camera on the user's phone
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION);
        }

        // request permission to write data (aka images) to the user's external storage of their phone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        // request permission to read data (aka images) from the user's external storage of their phone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        // on click for inception float model
        inceptionFloat = (Button) findViewById(R.id.inception_float);
        inceptionFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // filename in assets
                chosen = "inception_float.tflite";
                // model in not quantized
                quant = false;
                // open camera
                openCameraIntent();
            }
        });

        // on click for inception quant model
        inceptionQuant = (Button) findViewById(R.id.inception_quant);
        inceptionQuant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // filename in assets
                chosen = "inception_quant.tflite";
                // model in not quantized
                quant = true;
                // open camera
                openCameraIntent();
            }
        });


        moveToLogin = (Button) findViewById(R.id.move_to_login);
        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(ChooseModel.this, LoginActivity.class);
                startActivity(intentRegister);
            }
        });


        // on click for open gallery
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonChoose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                chosen = "inception_float.tflite";
                showFileChooser();

            }
        });

        About = (Button) findViewById(R.id.btnAbout);
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onOptionsItemSelected_menu_about");
                AlertDialog alertDialog = new AlertDialog.Builder(ChooseModel.this).create();
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

        Menu = (Button) findViewById(R.id.btnMenu1);
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChooseModel.this, "menu prasses", Toast.LENGTH_LONG).show();

                PopupMenu pm = new PopupMenu(ChooseModel.this, Menu);
                pm.getMenuInflater().inflate(R.menu.menu_layout, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_page_item:
                                Intent intentRegisterHomePage = new Intent(ChooseModel.this, HomePage.class);
                                startActivity(intentRegisterHomePage);
                                return true;

                            case R.id.upload_item:
                                //This page. Don't do nothing

                                return true;

                            case R.id.history_item:
                                Intent intentRegisterHistory = new Intent(ChooseModel.this, History.class);
                                startActivity(intentRegisterHistory);
                                return true;

                            case R.id.program_diet_item:

                                Toast.makeText(ChooseModel.this, "program diet", Toast.LENGTH_LONG).show();
                                dietProgram();

                                return true;

                            case R.id.settings_item:
                                // need to do
                                Toast.makeText(ChooseModel.this, "Need to do", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(ChooseModel.this, "No Data", Toast.LENGTH_LONG).show();
                            } else {
                                mMeal.add(new Meal(upload.getName(), foodModel.getEnergy()));
                            }
                            if (mUploads.size() == mMeal.size()) {
                                mProgressDialog.dismiss();
                                Intent intentProgramDiet = new Intent(ChooseModel.this, MealMenu.class);
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
                mProgressDialog = new ProgressDialog(ChooseModel.this);
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


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    // opens camera for user
    private void openCameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        // tell camera where to store the resulting picture
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    // checks that the user has allowed all the required permission of read and write and camera. If not, notify the user and close the application
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "This application needs read, write, and camera permissions to run. Application now closing.", Toast.LENGTH_LONG);
                System.exit(0);
            }
        }
    }

    // dictates what to do after the user takes an image, selects and image, or crops an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the camera activity is finished, obtained the uri, crop it to make it square, and send it to 'Classify' activity
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            try {
                Uri source_uri = imageUri;
                Uri dest_uri = Uri.fromFile(new File(getCacheDir(), "cropped"));
                sourcDest = source_uri;
                //angle = "90";
                // need to crop it to square image as CNN's always required square input
                Crop.of(source_uri, dest_uri).asSquare().start(ChooseModel.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Uri source_uri = imageUri;
            sourcDest = source_uri;
            Uri dest_uri = Uri.fromFile(new File(getCacheDir(), "cropped"));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = findViewById(R.id.selected_image);
                imageView.setImageBitmap(bitmap);


                Crop.of(source_uri, dest_uri).asSquare().start(ChooseModel.this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("OutOfMemoryError ");
                alertDialog.setMessage("This photo can't be uploaded\nNot enough memory");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }


        // if cropping acitivty is finished, get the resulting cropped image uri and send it to 'Classify' activity
        else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            imageUri = Crop.getOutput(data);
            Intent i = new Intent(ChooseModel.this, Classify.class);
            // put image data in extras to send
            i.putExtra("resID_uri", imageUri);
            // put filename in extras
            i.putExtra("sourcDest", sourcDest);
            // put model type in extras
            //i.putExtra("chosen", chosen);
            i.putExtra("quant", quant);
            // send other required data
            i.putExtra("angle", angle);
            startActivity(i);
        }
    }
}