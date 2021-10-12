package com.example.final_project2;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.final_project2.App.CHANNEL_ID;

public class ZoomInActivity extends AppCompatActivity {


    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1000;
    private static final String nutritional_Values = "nutritional_values";


    private ImageView img;
    private FirebaseStorage mStorage;
    private NotificationManager notificationManager;

    private Uri filePath;
    private URL url;


    //Menu && About
    private Button About;
    private Button Menu;

    private ArrayList<Meal> mMeal;
    private ProgressDialog mProgressDialog;
    private ArrayList<Upload> mUploads;
    private FirebaseUser user;

    private Button share_button;
    private Button download_button;


    private TextView nameFood;



    private TextView protein_space;
    private TextView Energy_space;
    private TextView Sugars_space;
    private TextView fatTxt_space;
    private TextView Carbohydrate_space;


    private String imageName;

    // firebase
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoomin_layout);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        img = (ImageView) findViewById(R.id.selected_image);

        share_button = (Button) findViewById(R.id.btnSend);
        download_button = (Button) findViewById(R.id.btnSave);

        user = FirebaseAuth.getInstance().getCurrentUser();


        About = (Button) findViewById(R.id.btnAbout);
        Menu = (Button) findViewById(R.id.btnMenu1);

        nameFood = (TextView) findViewById(R.id.nameFoodTxt);
        protein_space=(TextView) findViewById(R.id.proteinTxt_space);
        Energy_space=(TextView) findViewById(R.id.EnergyTxt_space);
        Sugars_space=(TextView) findViewById(R.id.SugarsTxt_space);
        fatTxt_space=(TextView) findViewById(R.id.fatTxt_space);
        Carbohydrate_space=(TextView) findViewById(R.id.CarbohydrateTxt_space);


        Intent intent = getIntent();
        imageName = (String) getIntent().getStringExtra("ImageName");
        nameFood.setText(imageName);
        try {
            url = new URL(intent.getStringExtra("ImageUrl"));
            filePath = Uri.parse(url.toURI().toString());
            Log.d("url", url.toString());
            Picasso.with(this)
                    .load(url.toString())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(img);
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            img.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        nutritionalValuesFromFirebase();


        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePicture();
            }
        });

        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadClick();
            }
        });

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ZoomInActivity.this, "menu prasses", Toast.LENGTH_LONG).show();

                PopupMenu pm = new PopupMenu(ZoomInActivity.this, Menu);
                pm.getMenuInflater().inflate(R.menu.menu_layout, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_page_item:
                                Intent intentHomePage = new Intent(ZoomInActivity.this, HomePage.class);
                                startActivity(intentHomePage);
                                return true;

                            case R.id.upload_item:
                                Intent intentUpload = new Intent(ZoomInActivity.this, ChooseModel.class);
                                startActivity(intentUpload);
                                return true;

                            case R.id.history_item:
                                Intent intentHistory = new Intent(ZoomInActivity.this, History.class);
                                startActivity(intentHistory);
                                return true;

                            case R.id.program_diet_item:

                                // need to do
                                Toast.makeText(ZoomInActivity.this, "program diet", Toast.LENGTH_LONG).show();
                                dietProgram();

                                return true;

                            case R.id.settings_item:
                                // need to do
                                Toast.makeText(ZoomInActivity.this, "Need to do", Toast.LENGTH_LONG).show();
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
                AlertDialog alertDialog = new AlertDialog.Builder(ZoomInActivity.this).create();
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
                                Toast.makeText(ZoomInActivity.this, "No Data", Toast.LENGTH_LONG).show();
                            } else {
                                mMeal.add(new Meal(upload.getName(), foodModel.getEnergy()));
                            }
                            if (mUploads.size() == mMeal.size()) {
                                mProgressDialog.dismiss();
                                Intent intentProgramDiet = new Intent(ZoomInActivity.this, MealMenu.class);
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
                mProgressDialog = new ProgressDialog(ZoomInActivity.this);
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



    // nutritional_values
    public void nutritionalValuesFromFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("nutritional_values").child(imageName).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        FoodModel foodModel = dataSnapshot.getValue(FoodModel.class);
                        Log.d("foodModel", dataSnapshot.getValue(FoodModel.class).toString());
                        // [START_EXCLUDE]
                        if (foodModel == null) {
                            // User is null, error out
                            Toast.makeText(ZoomInActivity.this, "No Data", Toast.LENGTH_LONG).show();
                        } else {
                            // Write new post
                            //Toast.makeText(ZoomInActivity.this, "Data", Toast.LENGTH_LONG).show();

                           protein_space.setText(""+foodModel.getProtein()+ "  g");
                           Energy_space.setText(foodModel.getEnergy()+ "  kcal");
                           Sugars_space.setText(foodModel.getSugar()+ "  g");
                           fatTxt_space.setText(foodModel.getFat()+ "  g");
                           Carbohydrate_space.setText(foodModel.getCarb()+ "  g");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        // [END single_value_read]

    }

    public void sharePicture() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        if (url != null) {
            sendIntent.putExtra(Intent.EXTRA_TEXT, "" + url);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share"));
        } else {
            Log.d("SharePicture", "Url null");
        }
    }
    /////////////////////showNotification////////////////

    public void showNotification() {
        RemoteViews expandedView = new RemoteViews(getPackageName(),
                R.layout.notification_expanded);
        Intent clickIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                0, clickIntent, 0);


        expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.saved__safe);
        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);

        Notification notification = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setCustomBigContentView(expandedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();

        notificationManager.notify(1, notification);
    }


    public void downloadClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ////// cant make the download///////
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, WRITE_EXTERNAL_STORAGE_CODE);
            } else {
                showNotification();
                Upload selectedItem = new Upload(imageName, filePath.toString());
                Intent intent = new Intent(getApplicationContext(), DownloadService.class);
                intent.putExtra("url", selectedItem.getImageUrl());
                startService(intent);

            }
        }
    }
}
