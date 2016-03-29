package com.example.s135123.kitchener;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by s142451 on 16-3-2016.
 */
public class Settings extends AppCompatActivity {

    CheckBox shakeActive;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);
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