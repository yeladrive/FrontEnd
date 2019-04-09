package com.example.yeladrive.Model;

public class Ride {
    private int mImageRessource;
    private String title;
    private String driver;

    public Ride(int imageRessource, String title, String driver){
        this.mImageRessource = imageRessource;
        this.title = title;
        this.driver = driver;
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
}
