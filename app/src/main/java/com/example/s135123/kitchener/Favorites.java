package com.example.s135123.kitchener;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


/**
 * Created by s142451 on 16-3-2016.
 */
public class Favorites extends android.support.v4.app.Fragment {
    private TextView textView;
    HashMap<String, String> apiParams = new HashMap<String, String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.favorites_fragment, container, false);
    }


    // Make server request
    apiParams.put("abc", "123");


    new CallWebService("GET", "http://appdev-gr1.win.tue.nl", apiParams) {
        @Override
        public void OnStartingService() {
            super.OnStartingService();
            // What you want to do
        }

        @Override
        public void OnGettingResult(JSONObject jsonObject) throws JSONException, MethodNotDefinedException {
            // Json object in return from webservice
            super.OnGettingResult(jsonObject);
        }
    }

    // List favorite meals

}
