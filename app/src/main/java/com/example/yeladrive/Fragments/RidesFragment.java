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
import android.widget.Toast;

import com.example.yeladrive.Adapters.RideAdapter;
import com.example.yeladrive.Model.Ride;
import com.example.yeladrive.Model.UpcomingRides;
import com.example.yeladrive.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static com.google.firebase.firestore.DocumentChange.Type.ADDED;


public class RidesFragment extends Fragment {
    private RecyclerView rideRecyclerView;
    private RideAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseFirestore mFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rides, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ArrayList<Ride> rideList = new ArrayList<>();
        //rideList.add(new Ride(R.drawable.school, "Going to school", "Noa's Father"));
        //rideList.add(new Ride(R.drawable.cake, "Elle's birthday", "Phillip"));
        //rideList.add(new Ride(R.drawable.soccer_ball, "Monday Soccer", "Maria"));

        rideRecyclerView = view.findViewById(R.id.rideRecycler);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RideAdapter(rideList);

        rideRecyclerView.setLayoutManager(mLayoutManager);
        rideRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("match").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    //Log.d(TAG, "Error : " + e.getMessage());

                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == ADDED) {

                        Map<String, Object> match = doc.getDocument().getData();
                        Log.d("data of a match:", match.toString());
                        Map<String, Object> ride = (Map<String, Object>) match.get("ride");
                        Map<String, String> ride_data = (Map<String, String>) ride.get("ride_data");
                        String title = ride_data.get("title");

                        Ride rideElmnt = new Ride(R.drawable.school, title, "Driver 1");

                        rideList.add(rideElmnt);
                        mAdapter.notifyDataSetChanged();

                    }

                    /*String kid_name = doc.getString("name");
                    String pick_up_loc = doc.getString("pick_up_loc");
                    Log.d(TAG, "Pick up : " + kid_name);*/


                }

            }
        });


        mAdapter.setOnItemClickListener(new RideAdapter.OnItemClickListener() {
            @Override
            public void onCheckClick(int i) {
                Toast.makeText(getActivity(), "Item " + i +" accepted",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClearClick(int i) {
                Toast.makeText(getActivity(), "Item " + i +" deleted",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
