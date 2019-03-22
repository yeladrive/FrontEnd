package com.example.yeladrive.Fragments;

public class UpcomingRides {
    String kid_name, pick_up_loc;

    public UpcomingRides(){

    }

    public UpcomingRides(String kid_name, String pick_up_loc){
        this.kid_name = kid_name;
        this.pick_up_loc = pick_up_loc;

    }
    public String getKid_name() {
        return kid_name;
    }

    public void setKid_name(String kid_name) {
        this.kid_name = kid_name;
    }

    public String getPick_up_loc() {
        return pick_up_loc;
    }

    public void setPick_up_loc(String pick_up_loc) {
        this.pick_up_loc = pick_up_loc;
    }
}
