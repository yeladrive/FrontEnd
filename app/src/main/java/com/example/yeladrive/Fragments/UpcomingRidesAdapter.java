package com.example.yeladrive.Fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yeladrive.R;

import java.util.List;

public class UpcomingRidesAdapter extends RecyclerView.Adapter<UpcomingRidesAdapter.ViewHolder> {

    public List<UpcomingRides> upcomingRidesList;

    public UpcomingRidesAdapter(List<UpcomingRides> upcomingRidesList){

        this.upcomingRidesList = upcomingRidesList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.kid_name_text.setText(upcomingRidesList.get(i).getKid_name());
        viewHolder.pick_up_loc_text.setText(upcomingRidesList.get(i).getPick_up_loc());

    }

    @Override
    public int getItemCount() {
        return upcomingRidesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public TextView kid_name_text;
        public TextView pick_up_loc_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            kid_name_text = (TextView) mView.findViewById(R.id.kid_name_text);
            pick_up_loc_text = (TextView) mView.findViewById(R.id.pick_up_loc_text);
        }
    }



}
