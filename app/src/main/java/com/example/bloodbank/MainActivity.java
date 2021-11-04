package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
//            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//            startActivity(intent);
//            finish();
        }
    }


    public void onLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupDonorClick(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra("isDonor", true);
        startActivity(intent);
        finish();
    }

    public void onSignupBBClick(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra("isDonor", false);
        startActivity(intent);
        finish();
    }
}