package com.example.s135123.kitchener;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by s142451 on 16-3-2016.
 */
public class Allergens extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergens);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAllergens);
        setSupportActionBar(toolbar);
        user = User.getInstance();


    }

    // List allergens

    // Add allergens

    // Remove allergens
}