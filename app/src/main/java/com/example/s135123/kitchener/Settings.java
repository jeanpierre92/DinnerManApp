package com.example.s135123.kitchener;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

/**
 * Created by s142451 on 16-3-2016.
 */
public class Settings extends AppCompatActivity {

    CheckBox shakeActive;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);
        user = User.getInstance();

        mShakeListener();
    }

    public void mShakeListener() {
        shakeActive = (CheckBox) findViewById(R.id.shakeActive);
        shakeActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shakeActive.isChecked()) {
                    user.setShakeEnabled(true);
                } else {
                    user.setShakeEnabled(false);
                }
            }
        });
    };



    // Change standard amount of days for schedule

    // Remove all favorites

}