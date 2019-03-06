package com.example.yeladrive;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String PICKUPLOC_KEY = "Pick up location";
    private static final String PICKUPTIME_KEY = "Pick up time";
    private static final String DROPOFFLOC_KEY = "Drop off location";
    private static final String DROPOFFTIME_KEY = "Drop off time";
    FirebaseFirestore db;
    TextView textDisplay;
    TextView message;
    EditText PICKUPLOC, PICKUPTIME, DROPOFFLOC, DROPOFFTIME;
    Button request,offer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        textDisplay = findViewById(R.id.textDisplay);
        request = findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRide();
            }
        });
        offer = findViewById(R.id.offer);
        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerToDrive();
            }
        });

    }

    private void offerToDrive(){
        offer = findViewById(R.id.offer);
        PICKUPLOC = findViewById(R.id.PICKUPLOC);
        DROPOFFLOC = findViewById(R.id.DROPOFFLOC);
        PICKUPTIME = findViewById(R.id.PICKUPTIME);
        DROPOFFTIME = findViewById(R.id.DROPOFFTIME);
        String mPUL = PICKUPLOC.getText().toString();
        String mDOL = DROPOFFLOC.getText().toString();
        String mPUT = PICKUPTIME.getText().toString();
        String mDOT = DROPOFFTIME.getText().toString();
        Map<String, Object>newDrive = new HashMap<>();
        newDrive.put(PICKUPLOC_KEY, mPUL);
        newDrive.put(DROPOFFLOC_KEY, mDOL);
        newDrive.put(PICKUPTIME_KEY, mPUT);
        newDrive.put(DROPOFFTIME_KEY, mDOT);
        db.collection("OfferToDrive").document("Offered_Drives").set(newDrive)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Information Submitted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR" +e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });
    }


    private void requestRide(){
        request = findViewById(R.id.request);
        PICKUPLOC = findViewById(R.id.PICKUPLOC);
        DROPOFFLOC = findViewById(R.id.DROPOFFLOC);
        PICKUPTIME = findViewById(R.id.PICKUPTIME);
        DROPOFFTIME = findViewById(R.id.DROPOFFTIME);
        String mPUL = PICKUPLOC.getText().toString();
        String mDOL = DROPOFFLOC.getText().toString();
        String mPUT = PICKUPTIME.getText().toString();
        String mDOT = DROPOFFTIME.getText().toString();
        Map<String, Object>newRide = new HashMap<>();
        newRide.put(PICKUPLOC_KEY, mPUL);
        newRide.put(DROPOFFLOC_KEY, mDOL);
        newRide.put(PICKUPTIME_KEY, mPUT);
        newRide.put(DROPOFFTIME_KEY, mDOT);
        db.collection("RequestARide").document("Requested_Rides").set(newRide)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Information Submitted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR" +e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });
    }
}