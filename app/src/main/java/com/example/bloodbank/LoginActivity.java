package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    public EditText inputEmail, inputPasword;
    public Button login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.editTextEmailAddress);
        inputPasword = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.loginBtn);
        mAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString();
                final String password = inputPasword.getText().toString();

                try {
                    if(password.length()>0 && email.length()>0) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();
                                            Log.v("error", task.getException().getMessage());
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Authentication Success",
                                                    Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please fill all the field.", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }
}