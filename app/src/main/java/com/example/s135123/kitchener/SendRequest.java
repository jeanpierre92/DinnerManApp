package com.example.s135123.kitchener;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by s135123 on 22-2-2016.
 */
public class SendRequest {

    public String sendGetRequest(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }

            /*HttpPost post = new HttpPost("http://appdev-gr1.win.tue.nl:8008/api/user/test/lfbHKQLmh0o5khy2/addFavorites");
            HttpEntity entity = new ByteArrayEntity("adsfdfs".getBytes("UTF-8"));
            post.setEntity(entity);
            HttpResponse response = httpclient.execute(post);
            System.out.println(EntityUtils.toString(response.getEntity()));*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //returns the status code
    public int sendPostRequest(String url, String post) {

        // create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // Create the POST object and add the parameters
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(post, HTTP.CONTENT_TYPE);
        httpPost.setEntity(entity);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.getStatusLine().getStatusCode();
    }

    //returns the status code
    public int sendDeleteRequest(String stringUrl, String delete) {
        int statuscode = 0;
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //regular delete request with payload gives an exception on kitkat...
            httpURLConnection.setRequestProperty("X-HTTP-Method-Override", "DELETE");
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(delete);
            wr.flush();
            statuscode = httpURLConnection.getResponseCode();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return statuscode;
    }

    // convert inputstream to String
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        inputStream.close();
        return result;
    }
}
