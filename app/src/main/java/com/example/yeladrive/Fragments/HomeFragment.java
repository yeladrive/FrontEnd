//https://www.youtube.com/watch?v=kyGVgrLG3KU help from this tutorial

package com.example.yeladrive.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yeladrive.Adapters.UpcomingRidesAdapter;
import com.example.yeladrive.HomeActivity;
import com.example.yeladrive.Model.UpcomingRides;
import com.example.yeladrive.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "Firelog";
    private RecyclerView mMainList;
    private FirebaseFirestore mFirestore;
    private UpcomingRidesAdapter upcomingRidesAdapter;
    private List<UpcomingRides> upcomingRidesList;
    private TextView emptyTextView;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ImageButton ava = (ImageButton) view.findViewById(R.id.imageButton);
        ImageButton elle = (ImageButton) view.findViewById(R.id.imageButton2);
        ImageButton finn = (ImageButton) view.findViewById(R.id.imageButton3);
        emptyTextView = view.findViewById(R.id.emptyTextView);
        ava.setOnClickListener(this);
        elle.setOnClickListener(this);
        finn.setOnClickListener(this);

        upcomingRidesList = new ArrayList<>();
        mMainList = (RecyclerView) view.findViewById(R.id.main_list);
        mMainList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mMainList.setHasFixedSize(true);
       // initializeData();

        upcomingRidesAdapter = new UpcomingRidesAdapter(upcomingRidesList);
       // upcomingRidesAdapter.notifyDataSetChanged();
        mMainList.setAdapter(upcomingRidesAdapter);

        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection(getString(R.string.UP_COMING_RIDES_PATH)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Error : " + e.getMessage());

                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        UpcomingRides upcomingRides = doc.getDocument().toObject(UpcomingRides.class);
                        upcomingRidesList.add(upcomingRides);
                        upcomingRidesAdapter.notifyDataSetChanged();

                    }

                    /*String kid_name = doc.getString("name");
                    String pickup_loc = doc.getString("pickup_loc");
                    Log.d(TAG, "Pick up : " + kid_name);*/


                }

            }
        });

        Log.d("Size of List", String.valueOf(upcomingRidesList.size()));

        /*if(upcomingRidesList.size()==0){
            mMainList.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            mMainList.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }*/


    }

    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.imageButton:
                //getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SchedulerFragment()).commit();
                Intent intent = new Intent(getActivity().getBaseContext(), HomeActivity.class);
                String kid = "Ava";
                intent.putExtra("kid_name", kid );
                getActivity().startActivity(intent);
                break;
            case R.id.imageButton2:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SchedulerFragment()).commit();
                break;
            case R.id.imageButton3:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SchedulerFragment()).commit();
                break;

        }
    }




    private void initializeData(){
        upcomingRidesList = new ArrayList<>();
        upcomingRidesList.add(new UpcomingRides("Ava", "school"));

    }
}


