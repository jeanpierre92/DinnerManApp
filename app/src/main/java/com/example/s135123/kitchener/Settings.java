package com.example.s135123.kitchener;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        user = User.getInstance();
        shakeActive = (CheckBox) findViewById(R.id.shakeActive);
        mShakeListener();

    }

    public void mShakeListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
    }




    // Change standard amount of days for schedule

    // Remove all favorites

}