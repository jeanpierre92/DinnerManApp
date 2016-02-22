package com.example.s135123.kitchener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.*;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by s135123 on 22-2-2016.
 */
public class SendRequest implements Runnable {

    String requestType;
    URL url;
    FatSecretRequest fatSecretRequest;
    MainActivity main;

    final String USER_AGENT = "Mozilla/5.0";
    JSONObject jsonObj;

    SendRequest(MainActivity main, String requestType, URL url, FatSecretRequest fatSecretRequest) {
        this.main = main;
        this.requestType = requestType;
        this.url = url;
        this.fatSecretRequest = fatSecretRequest;
    }

    @Override
    public void run() {

        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod(requestType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        int responseCode = 0;
        // Send post request
        con.setDoOutput(true);
        try {
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + parameters);
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

        // create JSON object from the string
        try {
            jsonObj = new JSONObject(response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        main.foodRequestDone(jsonObj);
    }
}
