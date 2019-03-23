//https://www.youtube.com/watch?v=kyGVgrLG3KU help from this tutorial

package com.example.yeladrive.Fragments;

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

import com.example.yeladrive.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "Firelog";
    private RecyclerView mMainList;
    private FirebaseFirestore mFirestore;
    private UpcomingRidesAdapter upcomingRidesAdapter;
    private List<UpcomingRides> upcomingRidesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        upcomingRidesList = new ArrayList<>();


        mMainList = (RecyclerView) view.findViewById(R.id.main_list);


        mMainList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mMainList.setHasFixedSize(true);
       // initializeData();

        upcomingRidesAdapter = new UpcomingRidesAdapter(upcomingRidesList);
       // upcomingRidesAdapter.notifyDataSetChanged();
        mMainList.setAdapter(upcomingRidesAdapter);



        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("Upcoming_Rides").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    String pick_up_loc = doc.getString("pick_up_loc");
                    Log.d(TAG, "Pick up : " + kid_name);*/


                }

            }
        });


    }


    private void initializeData(){
        upcomingRidesList = new ArrayList<>();
        upcomingRidesList.add(new UpcomingRides("Ava", "school"));

    }
}


