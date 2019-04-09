package com.example.yeladrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yeladrive.Model.Kid;
import com.example.yeladrive.Model.User;
import com.google.firebase.auth.FirebaseAuth;

public class KidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid);

        final EditText name_editText = findViewById(R.id.name_kid_edittext);
        final EditText age_editText = findViewById(R.id.age_kid_edittext);

        Button save_button = findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(FirebaseAuth.getInstance());
                if (name_editText.getText().length() != 0 && age_editText.getText().length() != 0 ){


                    String name = name_editText.getText().toString();
                    Log.d("Name", name);

                    Integer age = (Integer.parseInt(age_editText.getText().toString()));

                    Log.d("Age", age.toString());

                    Kid newKid = new Kid(name, age);


                    user.addNewKid(newKid);
                    Log.i("New kid added: ", user.getListOfKids().toString());

                    finish();

                } else {
                    Toast.makeText(KidActivity.this, "please enter all fields", Toast.LENGTH_SHORT).show();
                }


                //Log.i("Kid list emptyt:", user.getListOfKids().toString());


            }
        });


    }
}