package com.example.yeladrive.Model;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class User {
    private  ArrayList<Kid> listOfKids;
    private FirebaseAuth userAuth;

    public User(FirebaseAuth userAuth) {
        this.userAuth = userAuth;
        this.listOfKids = new ArrayList<>();
    }

    public void addNewKid(Kid newKid){
        this.listOfKids.add(newKid);
    }

    public ArrayList<Kid> getListOfKids() {
        return listOfKids;
    }
}