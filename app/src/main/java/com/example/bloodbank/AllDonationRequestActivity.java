package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AllDonationRequestActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser current_user;
    private Boolean isDonor, isRequestPage;
    private TextView pageHeading, noResultsHeading;
    private QuerySnapshot q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CollectionReference collection;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_dontaion_request);

        isDonor = getIntent().getBooleanExtra("isDonor", false);
        isRequestPage = getIntent().getBooleanExtra("isRequestsPage", false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        current_user = mAuth.getCurrentUser();
        pageHeading = findViewById(R.id.pageHeading);
        noResultsHeading = findViewById(R.id.noResultsHeading);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if(isRequestPage) {
            pageHeading.setText("Requests");
            collection = db.collection("Requests");
        } else {
            pageHeading.setText(isDonor ? "My Donations" : "All Donors");
            collection = db.collection("donations");
        }


        Query query = collection.whereEqualTo("requestorID", current_user.getUid());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    q = task.getResult();
                    if(task.getResult().size() == 0) {
                        noResultsHeading.setVisibility(View.VISIBLE);
                        noResultsHeading.setText("Nothing to show here!");
                    }
                    adapter = new RecyclerViewAdapter(getApplicationContext(), task.getResult());
                    adapter.setClickListener(AllDonationRequestActivity.this::onItemClick);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DonationRequestDetailsActivity.class);
        intent.putExtra("isDonor", isDonor);
        intent.putExtra("isRequestsPage", isRequestPage);
        intent.putExtra("itemID", q.getDocuments().get(position).getId());
        startActivity(intent);
    }
}