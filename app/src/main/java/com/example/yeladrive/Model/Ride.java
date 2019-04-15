package com.example.yeladrive.Model;

public class Ride {
    private int mImageRessource;
    private String title, driver, pickup_location, dropoff_location;

    public Ride(int imageRessource, String title, String driver, String pickup_location, String dropoff_location){
        this.mImageRessource = imageRessource;
        this.title = title;
        this.driver = driver;
        this.pickup_location = pickup_location;
        this.dropoff_location = dropoff_location;
    }

    public int getImageRessource() {
        return mImageRessource;
    }

    public String getTitle() {
        return title;
    }

    public String getDriver() {
        return driver;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public String getDropoff_location() {
        return dropoff_location;
    }
}
