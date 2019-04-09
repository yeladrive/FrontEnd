package com.example.yeladrive.Model;

public class UpcomingRides {
    String kid_name, pickup_loc;

    public UpcomingRides(){

    }

    public UpcomingRides(String kid_name, String pickup_loc){
        this.kid_name = kid_name;
        this.pickup_loc = pickup_loc;

    }
    public String getKid_name() {
        return kid_name;
    }

    public void setKid_name(String kid_name) {
        this.kid_name = kid_name;
    }

    public String getPickup_loc() {
        return pickup_loc;
    }

    public void setPickup_loc(String pickup_loc) {
        this.pickup_loc = pickup_loc;
    }
}
