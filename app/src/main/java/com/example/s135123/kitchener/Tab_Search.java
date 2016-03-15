package com.example.s135123.kitchener;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by s136693 on 5-3-2016.
 */
public class Tab_Search extends Fragment {
    ArrayList<Recipe> recipes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_search,container,false);

        final Button but1 = (Button) v.findViewById(R.id.foodRequest);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click ID: FoodButton");
                sendPostRequest();
            }
        });

        final Button but2 = (Button) v.findViewById(R.id.button2);
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click ID: TestButton");
            }
        });

        return v;
    }


    private void sendPostRequest() {

        final String USER_AGENT = "Mozilla/5.0";
        String url = "https://platform.fatsecret.com/rest/server.api";

        URL obj = null;
        HttpsURLConnection con = null;
        try {
            obj = new URL(url);
            con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
        } catch (IOException e) {
            e.printStackTrace();
        }

        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "a=foo&oauth_consumer_key=demo&oauth_nonce=abc&oauth_signature_method=HMAC-SHA1&oauth_timestamp=12345678&oauth_version=1.0&z=bar";

        int responseCode = 0;
        // Send post request
        con.setDoOutput(true);
        try {
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        StringBuffer response = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //print result
        System.out.println(response.toString());
    }
}