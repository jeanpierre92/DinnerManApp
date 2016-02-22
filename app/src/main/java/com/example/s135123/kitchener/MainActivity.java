package com.example.s135123.kitchener;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Button but1 = (Button) findViewById(R.id.foodRequest);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click ID: FoodButton");
                URL requestURL = null;
                try {
                    requestURL = new URL("http://platform.fatsecret.com/rest/server.api");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                SendRequest foodRequest = new SendRequest("POST", requestURL, "a=foo&oauth_consumer_key=64ff745f9f58447eb62cb6eb2ab906bb&oauth_nonce=abc&oauth_signature_method=HMAC-SHA1&oauth_timestamp=12345678&oauth_version=1.0&z=bar");
                while (!foodRequest.isDone) {
                    ;
                }
                JSONObject returnedJSONobject = foodRequest.getJSON();
                System.out.println(returnedJSONobject.toString());
            }
        });

        final Button but2 = (Button) findViewById(R.id.button2);
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click ID: TestButton");
            }
        });
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
