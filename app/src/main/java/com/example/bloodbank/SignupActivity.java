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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, fullName, bloodGrp, phone;
    private FirebaseAuth mAuth;
    private Button signupBtn;
    private FirebaseFirestore db_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean isDonor = getIntent().getBooleanExtra("isDonor", false);
        if(isDonor) {
            setContentView(R.layout.activity_signup_donor);
        } else {
            setContentView(R.layout.activity_signup_bb);
        }
        db_user = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.editTextEmailAddress);
        inputPassword = findViewById(R.id.editTextPassword);
        fullName = findViewById(R.id.editTextName);
        bloodGrp = isDonor ? findViewById(R.id.editTextBloodGrp) : null;
        phone = findViewById(R.id.editTextPhone);

        signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                final String name = fullName.getText().toString();
                final String phoneNum = phone.getText().toString();
                final String bloodGroup = isDonor ? bloodGrp.getText().toString() : "";

                try {
                    if (name.length() <= 2) {
                        ShowError("Name");
                        fullName.requestFocusFromTouch();
                    } else if (phoneNum.length() < 10) {
                        ShowError("Contact Number");
                        phone.requestFocusFromTouch();
                    } else if (email.length() == 0) {
                        ShowError("Email ID");
                        inputEmail.requestFocusFromTouch();
                    } else if (password.length() <= 5) {
                        ShowError("Password");
                        inputPassword.requestFocusFromTouch();
                    } else {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Registration failed! Try again.", Toast.LENGTH_LONG).show();
                                    Log.v("error", task.getException().getMessage());
                                } else {
                                    Map userData = new HashMap();
                                    userData.put("name", name);
                                    userData.put("phone", phoneNum);
                                    userData.put("bloodgroup", bloodGroup);
                                    userData.put("isDonor", isDonor);
                                    userData.put("email", email);
                                    userData.put("password", password);
                                    userData.put("emergencyPhone", "");
                                    String id = mAuth.getCurrentUser().getUid();
                                    db_user.collection("users").document(id).set(userData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "Welcome, your account has been created!", Toast.LENGTH_LONG).show();
                                                    Log.d("success", "DocumentSnapshot successfully written!");
                                                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Not added data!", Toast.LENGTH_LONG).show();
                                                    Log.w("error", "Error writing document", e);
                                                }
                                            });
                                }

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void ShowError(String error) {
        Toast.makeText(SignupActivity.this, "Please, Enter a valid "+error, Toast.LENGTH_LONG).show();
    }
}