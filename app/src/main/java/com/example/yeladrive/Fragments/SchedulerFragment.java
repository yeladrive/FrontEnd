package com.example.yeladrive.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeladrive.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
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

import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class SchedulerFragment extends Fragment {

    final private String API_KEY= "AIzaSyA5HoDdcx53N788n9QEiO_X3VMuAHVm7Bo"; //might be passed from home activity ?
    private static final String TAG = "MainActivity";
    private FirebaseFirestore db;
    private FirebaseAuth user;

    private PlacesClient placesClient;
    private AutocompleteSessionToken token;
    private RectangularBounds bounds;
    private String [] locations ={"", "", "", "", ""};
    private String query;
    private ArrayAdapter<String> adapter;

    private AutoCompleteTextView PICKUPLOC, DROPOFFLOC;
    private TextView PICKUPTIME, DROPOFFTIME, selectedKid;
    private DatePickerDialog.OnDateSetListener date_pick_listener, date_drop_listener;
    private Button request,offer, date_pick, date_drop, time_pick;
    private CheckBox kid1, kid2, kid3;

    private int kid_num;
    private int flag1 = 1;
    private int flag2 = 1;
    private int flag3  = 1;

    private String [] kids = {"Ava", "Elle", "Finn"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheduler, container, false);


        Places.initialize(view.getContext(), API_KEY);
        user = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //selectedKid = view.findViewById(R.id.SelectYourKids);
        request = view.findViewById(R.id.request);
        offer = view.findViewById(R.id.offer);

        PICKUPLOC = view.findViewById(R.id.pickup_loc_auto);
        DROPOFFLOC = view.findViewById(R.id.dropoff_loc_auto);
        PICKUPTIME = view.findViewById(R.id.PICKUPTIME);
        DROPOFFTIME = view.findViewById(R.id.DROPOFFTIME);

        date_pick = view.findViewById(R.id.select_date_button);
        date_drop = view.findViewById(R.id.select_date_button2);
        time_pick = view.findViewById(R.id.select_time_button);

        kid1 = view.findViewById(R.id.checkBox);
        kid2 = view.findViewById(R.id.checkBox2);
        kid3 = view.findViewById(R.id.checkBox3);

        placesClient = Places.createClient(view.getContext());
        token = AutocompleteSessionToken.newInstance();
        bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596));

        adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.select_dialog_item, locations);
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

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
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

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), // MAY CRASH
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

        kid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag1 == 1){
                    kid_num += 1;
                    flag1 = 0;
                }
                else {
                    kid_num -= 1;
                    flag1 = 1;
                }

            }
        });

        kid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag2 == 1){
                    kid_num += 1;
                    flag2 = 0;
                }
                else {
                    kid_num -= 1;
                    flag2 = 1;
                }

            }
        });

        kid3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag3 == 1){
                    kid_num += 1;
                    flag3 = 0;
                }
                else {
                    kid_num -= 1;
                    flag3 = 1;
                }

            }
        });



        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRide(kid_num);
            }
        });

        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerToDrive(kid_num);
            }
        });


        return view;
    }

    private void offerToDrive(int kid){
        String [] kid_names = {"Ava", "Elle", "Finn"};
        String [] updated_array = new String[3];
        String mPUL = PICKUPLOC.getText().toString();
        String mDOL = DROPOFFLOC.getText().toString();
        Timestamp mPUT = new Timestamp(new Date());
        Timestamp mDOT = new Timestamp(new Date());
        Timestamp mTIM = new Timestamp(new Date());
        String mUSR = user.getUid();
        int seats_available = 5;

        Map<String, Object>newDrive = new HashMap<>();
        newDrive.put(getString(R.string.PICKUPLOC_KEY), mPUL);
        newDrive.put(getString(R.string.DROPOFFLOC_KEY), mDOL);
        newDrive.put(getString(R.string.PICKUPTIME_KEY), mPUT);
        newDrive.put(getString(R.string.DROPOFFTIME_KEY), mDOT);
        newDrive.put(getString(R.string.DRIVER_ID), mUSR);
        newDrive.put(getString(R.string.TIMESTAMP), mTIM);

        for(int i =0; i < kid; i++){
            updated_array[i] = kid_names[i];
        }
        seats_available = seats_available - updated_array.length;
        newDrive.put("seats_available", seats_available);

        newDrive.put("kid", Arrays.asList(updated_array));

        db.collection(getString(R.string.DRIVE_PATH)).document().set(newDrive)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Information Submitted",
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "submitted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "ERROR" +e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }


    private void requestRide(int kid){
        String [] kid_names = {"Ava", "Elle", "Finn"};
        String [] updated_array = new String[3];
        String mPUL = PICKUPLOC.getText().toString();
        String mDOL = DROPOFFLOC.getText().toString();
        Timestamp mPUT = new Timestamp(new Date());
        Timestamp mDOT = new Timestamp(new Date());
        Timestamp mTIM = new Timestamp(new Date());
        String mUSR = user.getUid();
        int seats_needed = 5;


        Map<String, Object> newRide = new HashMap<>();
        newRide.put(getString(R.string.PICKUPLOC_KEY), mPUL);
        newRide.put(getString(R.string.DROPOFFLOC_KEY), mDOL);
        newRide.put(getString(R.string.PICKUPTIME_KEY), mPUT);
        newRide.put(getString(R.string.DROPOFFTIME_KEY), mDOT);
        newRide.put(getString(R.string.RIDER_ID), mUSR);
        newRide.put(getString(R.string.TIMESTAMP), mTIM);





        for(int i =0; i < kid; i++){
            updated_array[i] = kid_names[i];
        }

        seats_needed = seats_needed - updated_array.length;
        newRide.put("seats_needed", seats_needed);

        newRide.put("kid", Arrays.asList(updated_array));

        db.collection(getString(R.string.RIDE_PATH)).document().set(newRide)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Information Submitted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "ERROR" +e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }



}


