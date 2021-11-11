package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db_user;
    private FirebaseUser current_user;
    private EditText name, bloodGroup, phone, emergencyPhone, email, password;
    private DocumentReference user;
    private Boolean isDonor;
    private String userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDonor = getIntent().getBooleanExtra("isDonor", false);
        if(isDonor) {
            setContentView(R.layout.activity_my_profile);
        } else {
            setContentView(R.layout.activity_my_bbprofile);
        }

        mAuth = FirebaseAuth.getInstance();
        db_user = FirebaseFirestore.getInstance();
        current_user = mAuth.getCurrentUser();

        name = findViewById(R.id.editTextName);
        phone = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);

        if(isDonor) {
            bloodGroup = findViewById(R.id.editTextBloodGroup);
            emergencyPhone = findViewById(R.id.editTextEmergencyPhone);
        }

        user = db_user.collection("users").document(current_user.getUid());
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userData = task.getResult();
                    if (userData.exists()) {
                        userEmail = userData.get("email").toString();
                        userPassword = userData.get("password").toString();
                        name.setText(userData.get("name").toString());
                        phone.setText(userData.get("phone").toString());
                        email.setText(userEmail);
                        password.setText(userPassword);
                        if(isDonor) {
                            bloodGroup.setText(userData.get("bloodgroup").toString());
                            emergencyPhone.setText(userData.get("emergency").toString());
                        }
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });
    }

    public void onLogoutClick(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSave(View view) {
        Map updatedData = new HashMap();
        updatedData.put("email", email.getText().toString());
        updatedData.put("phone", phone.getText().toString());
        updatedData.put("password", password.getText().toString());
        if(isDonor) {
            updatedData.put("emergency", emergencyPhone.getText().toString());
        }
        if(!userEmail.equals(updatedData.get("email").toString())) {
            System.out.println("email changes");
            current_user.updateEmail(updatedData.get("email").toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        System.out.println("updated email");
                        Log.d("Success", "User email address updated.");
                    }
                }
            });
        }
        if(!userPassword.equals(updatedData.get("password").toString())) {
            System.out.println("password changes");
            current_user.updatePassword(updatedData.get("password").toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        System.out.println("updated password");
                        Log.d("Success", "User password updated.");
                    }
                }
            });
        }
        user.update(updatedData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),
                        "Account data successfully updated!",
                        Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Some error occurred. Please try again.",
                        Toast.LENGTH_LONG).show();
                Log.w("Error", "Error updating document", e);
            }
        });
    }
}