package com.example.final_project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private Button logoutBT;
    private TextView profileText;
    private Button loginEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        logoutBT = findViewById(R.id.logout_button);
        logoutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        profileText = findViewById(R.id.textProfile);
        loginEmail = findViewById(R.id.loginEmail);
        String[] stringEmail = getIntent().getStringExtra("login").split("@");
        profileText.setText("Hello " + stringEmail[0]);

        loginEmail.setText("" + stringEmail[0].charAt(0));
    }
}
