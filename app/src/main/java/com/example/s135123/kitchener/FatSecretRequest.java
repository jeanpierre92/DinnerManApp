package com.example.s135123.kitchener;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by s135123 on 22-2-2016.
 */
public class FatSecretRequest {

    String consumerKey;
    int timeStamp;
    String[] arguments;

    String signatureMethod;
    String nonce;
    String version;
    String returnString;

    FatSecretRequest(String consumerKey, int timeStamp, String[] arguments) {
        this.consumerKey = consumerKey;
        this.timeStamp = timeStamp;
        this.arguments = arguments;

        signatureMethod = "HMAC-SHA1";
        makeNonce();
        version = "1.0";
        makeReturnString();
    }

    private void makeReturnString() {

    }

    private void makeNonce() {
        // Create a random string that the fatSecretAPI needs
        SecureRandom random = new SecureRandom();
        nonce = new BigInteger(130, random).toString(32);
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getArguments() {
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
