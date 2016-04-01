package com.example.s135123.kitchener;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class MainActivity extends AppCompatActivity {
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    RelativeLayout relativeMainLayout;
    CharSequence Titles[]={"Schedule","Recommend", "Search"};
    int numboftabs =3;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;
    ImageView hamburger;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getResources().getBoolean(R.bool.isPhone)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        user = User.getInstance();
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        User user = User.getInstance();
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        hamburger = (ImageView) findViewById(R.id.hamburger_icon);
        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked hamburger");
                mDrawer.openDrawer(Gravity.LEFT);
            }
        });
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        if(getIntent().getSerializableExtra("page")!=null) {
            int page = (Integer) getIntent().getSerializableExtra("page");
            pager.setCurrentItem(page);
        }
        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        relativeMainLayout = (RelativeLayout) findViewById(R.id.relative_main_layout);
        boolean isPhone = getResources().getBoolean(R.bool.isPhone);
        if(isPhone){
            ViewGroup.LayoutParams paramsLinear = relativeMainLayout.getLayoutParams();
            paramsLinear.width= ViewGroup.LayoutParams.MATCH_PARENT;
            ViewGroup.LayoutParams paramsSlide = tabs.getLayoutParams();
            paramsSlide.width= ViewGroup.LayoutParams.MATCH_PARENT;
            ViewGroup.LayoutParams paramsPager = pager.getLayoutParams();
            paramsPager.width= ViewGroup.LayoutParams.MATCH_PARENT;
        }
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
    @Override
     public void onBackPressed() {
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass=null;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                Intent intentTutorial = new Intent(MainActivity.this, TutorialGeneral.class);
                startActivity(intentTutorial);

                break;
            case R.id.nav_second_fragment:
                Intent intentFavorites = new Intent(MainActivity.this, Favorites.class);
                startActivity(intentFavorites);
                break;
            case R.id.nav_third_fragment:
                Intent intentAllergens = new Intent(MainActivity.this, Allergens.class);
                startActivity(intentAllergens);
                break;
            case R.id.nav_fourth_fragment:
                Intent intentSettings = new Intent(MainActivity.this, Settings.class);
                startActivity(intentSettings);
                break;
            case R.id.nav_fifth_fragment:
                User user =User.getInstance();
                user.setPassword(null);
                user.setUsername(null);
                Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogin);
            default:
                fragmentClass = Tab_Schedule.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(fragment!=null&&fragmentClass!=null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
        // Highlight the selected item, update the title, and close the drawer
        // Highlight the selected item has been done by NavigationView
        // menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public void onPause(){
        super.onPause();
            sensorManager.unregisterListener(shakeDetector);
    }
    @Override
    public void onResume(){
        super.onResume();
            sensorManager.registerListener(shakeDetector,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
    }
}
