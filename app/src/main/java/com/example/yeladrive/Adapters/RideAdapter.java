package com.example.yeladrive.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yeladrive.Model.Ride;
import com.example.yeladrive.R;

import java.util.ArrayList;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {
    private ArrayList<Ride> rideList;
    private int mExpandedPosition = -1;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onCheckClick(int i);
        void onClearClick(int i);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class RideViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView titleTextView, driverTextView, pickupTextView, dropoffTextView, fromTextView, toTextView;
        public ImageView mCheckImage;
        public ImageView mClearImage;

        public RideViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleRideTextView);
            driverTextView = itemView.findViewById(R.id.driverRideTextView);
            pickupTextView = itemView.findViewById(R.id.pickText);
            dropoffTextView = itemView.findViewById(R.id.dropText);
            fromTextView = itemView.findViewById(R.id.fromTextView);
            toTextView = itemView.findViewById(R.id.toTextView);
            mCheckImage = itemView.findViewById(R.id.checkImageView);
            mClearImage = itemView.findViewById(R.id.clearImageView);

            mCheckImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int i = getAdapterPosition();
                        if(i!=RecyclerView.NO_POSITION){
                            listener.onCheckClick(i);
                        }
                    }
                }
            });

            mClearImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int i = getAdapterPosition();
                        if(i!=RecyclerView.NO_POSITION){
                            listener.onClearClick(i);
                        }
                    }
                }
            });


        }
    }

    public RideAdapter(ArrayList<Ride> rideList){
        this.rideList = rideList;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ride_cardview, viewGroup, false);
        RideViewHolder rvh = new RideViewHolder(v, mListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder rideViewHolder, final int i) {
        Ride currentRide = rideList.get(i);

        rideViewHolder.mImageView.setImageResource(currentRide.getImageRessource());
        rideViewHolder.titleTextView.setText(currentRide.getTitle());
        rideViewHolder.driverTextView.setText(currentRide.getDriver() + " is driving");
        rideViewHolder.dropoffTextView.setText(currentRide.getDropoff_location());
        rideViewHolder.pickupTextView.setText(currentRide.getPickup_location());


        final boolean isExpanded = i==mExpandedPosition;
        rideViewHolder.dropoffTextView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        rideViewHolder.pickupTextView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        rideViewHolder.fromTextView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        rideViewHolder.toTextView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        rideViewHolder.itemView.setActivated(isExpanded);
        rideViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:i;
                notifyItemChanged(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rideList.size();
    }
}
