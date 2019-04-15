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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
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
    private static final String TAG ="Ride Fragment" ;
    private RecyclerView rideRecyclerView;
    private RideAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseFirestore mFirestore;
    private ArrayList<Ride> rideList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rides, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createRideList();
        buildRecyclerView(view);

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
                        String pick = ride_data.get("pickup_location");
                        String drop = ride_data.get("dropoff_location");
                        //String pick_time = ride_data.get("pickup_time");


                        Ride rideElmnt = new Ride(R.drawable.noa_pic, title, "Noa", pick, drop);

                        rideList.add(rideElmnt);
                        mAdapter.notifyDataSetChanged();

                    }
                }

            }
        });


        mAdapter.setOnItemClickListener(new RideAdapter.OnItemClickListener() {
            @Override
            public void onCheckClick(int i) {
                Toast.makeText(getActivity(), "Match " + i +" accepted",
                        Toast.LENGTH_SHORT).show();
                checkItem(i);

            }

            @Override
            public void onClearClick(int i) {
                Toast.makeText(getActivity(), "Match " + i + " deleted",
                        Toast.LENGTH_SHORT).show();
                clearItem(i);

            }
        });

    }

    private void buildRecyclerView(View view) {
        rideRecyclerView = view.findViewById(R.id.rideRecycler);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RideAdapter(rideList);

        rideRecyclerView.setLayoutManager(mLayoutManager);
        rideRecyclerView.setAdapter(mAdapter);
    }

    private void createRideList() {
        rideList = new ArrayList<>();
       /* rideList.add(new Ride(R.drawable.school, "Going to school", "Noa's Father"));
        rideList.add(new Ride(R.drawable.cake, "Elle's birthday", "Phillip"));
        rideList.add(new Ride(R.drawable.soccer_ball, "Monday Soccer", "Maria"));*/
    }

    private void clearItem(int position) {
        rideList.remove(position);
        mAdapter.notifyItemRemoved(position);

    }

    private void checkItem(int position) {
        //push to upcoming rides
        Ride rideSelected = rideList.get(position);

        UpcomingRides upcomingRide = new UpcomingRides(rideSelected.getTitle(), rideSelected.getDriver());
        addToFirestore(upcomingRide);
        rideList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    private void addToFirestore(UpcomingRides upcomingRides){
        mFirestore.collection(getString(R.string.UP_COMING_RIDES_PATH)).document().set(upcomingRides)
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




}
