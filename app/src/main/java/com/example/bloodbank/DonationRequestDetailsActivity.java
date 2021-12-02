package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DonationRequestDetailsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser current_user;
    private TextView name, addressOrBG, contact, date, pageHeading, hospital, isEmergency;
    private Boolean isDonor, isRequestPage;
    private String itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DocumentReference doc;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_request_details);

        isDonor = getIntent().getBooleanExtra("isDonor", false);
        isRequestPage = getIntent().getBooleanExtra("isRequestsPage", false);
        itemID = getIntent().getStringExtra("itemID");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        current_user = mAuth.getCurrentUser();
        name = findViewById(R.id.nameText);
        addressOrBG = findViewById(R.id.addressOrBGText);
        contact = findViewById(R.id.contactText);
        date = findViewById(R.id.dateText);
        hospital = findViewById(R.id.hospitalNameText);
        isEmergency = findViewById(R.id.emergencyText);
        pageHeading = findViewById(R.id.pageHeading);

        if(isRequestPage) {
            pageHeading.setText("Request Details");
            doc = db.collection("Requests").document(itemID);
        } else {
            pageHeading.setText(isDonor ? "Donation Details" : "Donor Details");
            doc = db.collection("donations").document(itemID);
        }

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String hospitalName = document.get("hospital") != null ? document.get("hospital").toString() : "";
                    Boolean isEmergencyBool = document.get("emergency") != null ? Boolean.parseBoolean(document.get("emergency").toString()): false;
                    name.setText("Name: " + document.get("name").toString());
                    contact.setText("Contact: " + document.get("phone").toString());
                    addressOrBG.setText(isDonor ? "Blood Group: " + document.get("bloodGroup").toString() :
                            "Address: " + document.get("address").toString());
                    Long dateCreated = Long.parseLong(document.getId().toString().split("_")[1]);
                    Date dateObj = new Date(dateCreated);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    String dateString = format.format(dateObj);
                    date.setText("Date: " + dateString);
                    hospital.setText(hospitalName);
                    isEmergency.setText(isEmergencyBool ? "URGENT" : "");
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

    public void showRequestsList(View view) {
        Intent intent = new Intent(this, AllDonationRequestActivity.class);
        intent.putExtra("isDonor", isDonor);
        intent.putExtra("isRequestsPage", true);
        startActivity(intent);
    }

    public void showDonationsList(View view) {
        Intent intent = new Intent(this, AllDonationRequestActivity.class);
        intent.putExtra("isDonor", isDonor);
        intent.putExtra("isRequestsPage", false);
        startActivity(intent);
    }
    public void createNewReq(View view) {
        Intent intent = new Intent(this, CreateRequest.class);
        startActivity(intent);
    }
}