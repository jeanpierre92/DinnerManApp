package com.example.s135123.kitchener;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by s135123 on 22-2-2016.
 */
public class FatSecretRequest {

    String consumerKey;
    String[] arguments;

    String signatureMethod;
    String nonce;
    String version;
    String returnString;
    String fatSecretURL;
    String httpMethod;
    String format;
    String timeStamp;

    FatSecretRequest(String[] arguments) {
        this.arguments = arguments;

        signatureMethod = "HMAC-SHA1";
        version = "1.0";
        fatSecretURL = "http://platform.fatsecret.com/rest/server.api";
        httpMethod = "POST";
        format = "json";
        makeNonce();
        makeTimeStamp();
        makeReturnString();
    }

    private void makeReturnString() {
        //first sort the array
        List<String> sortedArray = Arrays.asList(arguments);
        sortedArray.add(consumerKey);
        sortedArray.add(signatureMethod);
        sortedArray.add(version);
        sortedArray.add(nonce);
        sortedArray.add(timeStamp);
        Arrays.sort(sortedArray.toArray());;
        URLEncoder test = URLEncoder.encode(sortedArray.toString(), "US-ASCII");
    }

    private void makeNonce() {
        // Create a random string that the fatSecretAPI needs
        SecureRandom random = new SecureRandom();
        nonce = new BigInteger(130, random).toString(32);
    }

    private void makeTimeStamp() {
        // Get the current
        Long timeStampLong = System.currentTimeMillis()/1000;
        timeStamp = timeStampLong.intValue() + "";
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getArgumentsSortedSigned() {
        return returnString;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public String getNonce() {
        return nonce;
    }

    public String getVersion() {
        return version;
    }
}
