package com.example.final_project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Welcome extends AppCompatActivity {

    private Button btnLogin;
    private Button btnModelSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        btnLogin = (Button)findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(Welcome.this, LoginActivity.class);
                startActivity(intentRegister);
            }
        });

        btnModelSettings = (Button)findViewById(R.id.modelSettings);
        btnModelSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(Welcome.this, ModelSettings.class);
                startActivity(intentRegister);
            }
        });


    }






}
