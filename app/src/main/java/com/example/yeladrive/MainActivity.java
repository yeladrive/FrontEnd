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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth user;

    TextView textDisplay;
    EditText PICKUPLOC, PICKUPTIME, DROPOFFLOC, DROPOFFTIME;
    Button request,offer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        textDisplay = findViewById(R.id.textDisplay);

        request = findViewById(R.id.request);
        offer = findViewById(R.id.offer);
        PICKUPLOC = findViewById(R.id.PICKUPLOC);
        DROPOFFLOC = findViewById(R.id.DROPOFFLOC);
        PICKUPTIME = findViewById(R.id.PICKUPTIME);
        DROPOFFTIME = findViewById(R.id.DROPOFFTIME);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRide();
            }
        });

        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerToDrive();
            }
        });

    }

    private void offerToDrive(){

        String mPUL = PICKUPLOC.getText().toString();
        String mDOL = DROPOFFLOC.getText().toString();
        String mPUT = PICKUPTIME.getText().toString();
        String mDOT = DROPOFFTIME.getText().toString();
        Timestamp mTIM = new Timestamp(new Date());
        String mUSR = user.getUid();

        Map<String, Object>newDrive = new HashMap<>();
        newDrive.put(getString(R.string.PICKUPLOC_KEY), mPUL);
        newDrive.put(getString(R.string.DROPOFFLOC_KEY), mDOL);
        newDrive.put(getString(R.string.PICKUPTIME_KEY), mPUT);
        newDrive.put(getString(R.string.DROPOFFTIME_KEY), mDOT);
        newDrive.put(getString(R.string.DRIVER_ID), mUSR);
        newDrive.put(getString(R.string.TIMESTAMP), mTIM);


        db.collection(getString(R.string.DRIVE_PATH)).document().set(newDrive)
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

        String mPUL = PICKUPLOC.getText().toString();
        String mDOL = DROPOFFLOC.getText().toString();
        String mPUT = PICKUPTIME.getText().toString();
        String mDOT = DROPOFFTIME.getText().toString();
        Timestamp mTIM = new Timestamp(new Date());
        String mUSR = user.getUid();

        Map<String, Object>newRide = new HashMap<>();
        newRide.put(getString(R.string.PICKUPLOC_KEY), mPUL);
        newRide.put(getString(R.string.DROPOFFLOC_KEY), mDOL);
        newRide.put(getString(R.string.PICKUPTIME_KEY), mPUT);
        newRide.put(getString(R.string.DROPOFFTIME_KEY), mDOT);
        newRide.put(getString(R.string.DRIVER_ID), mUSR);
        newRide.put(getString(R.string.TIMESTAMP), mTIM);

        db.collection(getString(R.string.RIDE_PATH)).document().set(newRide)
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