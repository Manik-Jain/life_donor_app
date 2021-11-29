package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db_user;
    private FirebaseUser current_user;
    private TextView username, summary_text, donations_text;
    private Boolean isDonor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        db_user = FirebaseFirestore.getInstance();
        current_user = mAuth.getCurrentUser();
        username = findViewById(R.id.userNameText);
        summary_text = findViewById(R.id.summaryText);
        donations_text = findViewById(R.id.donationsText);
        DocumentReference user = db_user.collection("users").document(current_user.getUid());
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userData = task.getResult();
                    if (userData.exists()) {
                        isDonor = Boolean.parseBoolean(userData.get("isDonor").toString());
                        username.setText("Hi "+ userData.get("name").toString().split(" ")[0]);
                        if(isDonor) {
                            summary_text.setText("Blood Group: " + userData.get("bloodgroup").toString());
                            donations_text.setText("Total Donations: ");
                        } else {
                            summary_text.setText("Patients served: ");
                        }

                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });


    }

    public void onMyProfileClick(View view) {
        Intent intent = new Intent(this, MyProfileActivity.class);
        intent.putExtra("isDonor", isDonor);
        startActivity(intent);
    }

    public void createNewReq(View view) {
        Intent intent = new Intent(this, CreateRequest.class);
        startActivity(intent);
    }
}