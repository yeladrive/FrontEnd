package com.example.yeladrive;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.android.libraries.places.api.Places;

public class MainActivity extends AppCompatActivity {
    final private String API_KEY= "AIzaSyA5HoDdcx53N788n9QEiO_X3VMuAHVm7Bo";

    private FirebaseFirestore db;
    private FirebaseAuth user;

    private PlacesClient placesClient;
    private AutocompleteSessionToken token;
    private RectangularBounds bounds;
    private String [] locations ={"", "", "", "", ""};
    private String query;
    private ArrayAdapter<String> adapter;

    private AutoCompleteTextView PICKUPLOC, DROPOFFLOC;
    private TextView PICKUPTIME, DROPOFFTIME;
    private DatePickerDialog.OnDateSetListener date_pick_listener, date_drop_listener;
    Button request,offer, date_pick, date_drop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Places.initialize(getApplicationContext(), API_KEY);
        user = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        request = findViewById(R.id.request);
        offer = findViewById(R.id.offer);
        PICKUPLOC = findViewById(R.id.pickup_loc_auto);
        DROPOFFLOC = findViewById(R.id.dropoff_loc_auto);
        PICKUPTIME = findViewById(R.id.PICKUPTIME);
        DROPOFFTIME = findViewById(R.id.DROPOFFTIME);
        date_pick = findViewById(R.id.select_date_button);
        date_drop = findViewById(R.id.select_date_button2);

        placesClient = Places.createClient(this);
        token = AutocompleteSessionToken.newInstance();
        bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596));

        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, locations);
        PICKUPLOC.setThreshold(1);
        PICKUPLOC.setAdapter(adapter);
        DROPOFFLOC.setAdapter(adapter);

        date_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_Alert,date_pick_listener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        date_pick_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + month + "-" + year;
                PICKUPTIME.setText(date);
            }
        };

        date_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_Alert,date_drop_listener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        date_drop_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + month + "-" + year;
                DROPOFFTIME.setText(date);
            }
        };



        PICKUPLOC.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                query = PICKUPLOC.getText().toString();
                // Use the builder to create a FindAutocompletePredictionsRequest.
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        // Call either setLocationBias() OR setLocationRestriction().
                        .setLocationBias(bounds)
                        //.setLocationRestriction(bounds)
                        .setCountry("us")
                        .setTypeFilter(TypeFilter.ESTABLISHMENT)
                        .setSessionToken(token)
                        .setQuery(query)
                        .build();

                placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onSuccess(FindAutocompletePredictionsResponse response) {
                        adapter.clear();
                        int i = 0;
                        for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                            locations[i]=prediction.getFullText(null).toString();
                            adapter.add(locations[i]);
                            //Log.i(TAG, prediction.getPlaceId());
                            //Log.i(TAG, prediction.getPrimaryText(null).toString());
                            Log.i("location", locations[i] + " nn " + i);
                            i++;
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            //Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                });
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        DROPOFFLOC.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                query = DROPOFFLOC.getText().toString();
                // Use the builder to create a FindAutocompletePredictionsRequest.
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        // Call either setLocationBias() OR setLocationRestriction().
                        .setLocationBias(bounds)
                        //.setLocationRestriction(bounds)
                        .setCountry("us")
                        .setTypeFilter(TypeFilter.ESTABLISHMENT)
                        .setSessionToken(token)
                        .setQuery(query)
                        .build();

                placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onSuccess(FindAutocompletePredictionsResponse response) {
                        adapter.clear();
                        int i = 0;
                        for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                            locations[i]=prediction.getFullText(null).toString();
                            adapter.add(locations[i]);
                            //Log.i(TAG, prediction.getPlaceId());
                            //Log.i(TAG, prediction.getPrimaryText(null).toString());
                            Log.i("location", locations[i] + " nn " + i);
                            i++;
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            //Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                });
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

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