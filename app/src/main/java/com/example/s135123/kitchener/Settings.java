package com.example.s135123.kitchener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

/**
 * Created by s142451 on 16-3-2016.
 */
public class Settings extends android.support.v4.app.Fragment {
    CheckBox ch1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ch1=(CheckBox)getView().findViewById(R.id.measure);
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    // Change standard amount of days for schedule

    // Remove all favorites

}