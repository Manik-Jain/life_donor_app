package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class CreateRequest extends AppCompatActivity {
    private FirebaseFirestore db_request;
    private EditText name, address, bloodGrp, phone;
    private CheckBox emergencyYes,emergencyNo, fillAddress;
    private Button createBtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        name = findViewById(R.id.createReqName);
        address =findViewById(R.id.createReqAddress);
        bloodGrp =findViewById(R.id.createReqBloodType);
        phone =findViewById(R.id.createReqContactNo);

        createBtn = findViewById(R.id.createBtn);

        emergencyYes = findViewById(R.id.createReqChkYes);
        emergencyNo = findViewById(R.id.createReqChkNo);


        db_request = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strName= name.getText().toString();
                final String strAddress= address.getText().toString();
                final String strBloodGroup= bloodGrp.getText().toString();
                final String strPhone= phone.getText().toString();

                final boolean boolEmergencyY =emergencyYes.isChecked();
                final boolean boolEmergencyN =emergencyNo.isChecked();
                final boolean boolEmergency;
                final boolean boolFillAdd =emergencyYes.isChecked();

                if(boolEmergencyY)
                    boolEmergency=true;
                else boolEmergency=false;

                String strRequestorID = mAuth.getCurrentUser().getUid();

                long time = System.currentTimeMillis();
                String strTime= String.valueOf(time);
                Toast.makeText(getApplicationContext(),strTime, Toast.LENGTH_SHORT).show();

                Map requestData = new HashMap();
                requestData.put("name", strName);
                requestData.put("phone", strPhone);
                requestData.put("bloodGroup", strBloodGroup);
                requestData.put("address", strAddress);
                requestData.put("emergency", boolEmergency);
                requestData.put("requestorID", strRequestorID);
                String id = strRequestorID+"_"+strTime ;
                db_request.collection("Requests").document(id).set(requestData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Your Request has been created", Toast.LENGTH_LONG).show();
                                Log.d("success", "New request successfully created");
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
                                Log.w("error", "Error creating the request", e);
                            }
                        });
              db_request.collection("Requests").document(id).get();
            }
        });

    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.createReqChkYes:
            {
                emergencyNo.setChecked(false);
                emergencyYes.setChecked(true);
            }
                break;
            case R.id.createReqChkNo:
             {
                emergencyNo.setChecked(true);
                emergencyYes.setChecked(false);
            }
            default: emergencyNo.setChecked(false);
        }
    }
}