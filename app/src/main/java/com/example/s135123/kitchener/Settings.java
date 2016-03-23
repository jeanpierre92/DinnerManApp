package com.example.s135123.kitchener;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
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
public class Settings extends android.support.v4.app.Fragment {

    CheckBox shakeActive;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.settings_fragment, container, false);
        shakeActive = (CheckBox) v.findViewById(R.id.shakeActive);

        shakeActive.setOnClickListener(mShakeListener);

        return v;
    }

    private View.OnClickListener mShakeListener = new View.OnClickListener() {
        User user = User.getInstance();
        public void onClick(View v) {
            if (shakeActive.isChecked()) {
                user.setShakeEnabled(true);
            } else {
                user.setShakeEnabled(false);
            }
        }
    };

    // Change standard amount of days for schedule

    // Remove all favorites

}