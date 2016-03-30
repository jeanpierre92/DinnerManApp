package com.example.s135123.kitchener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class TutorialSchedule extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_schedule);
    }

    public void onClickGotIt(View v){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("page",0);
        startActivity(i);
    }
}
