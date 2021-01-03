package com.example.mohammadghali.petsapp;

/**
 * Created by mohammadghali on 9/22/18.
 *
 * THIS IS A MODIFIED PostJson of the one provided in class
 *
 *
 * Modification: Ability to send a key:value map that contains all the required parameters to send
 */

import android.content.SharedPreferences;
import android.icu.text.MessageFormat;
import android.os.AsyncTask;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;


public class PostJson extends AsyncTask<String , Void ,String> {
    String server_response;


    public OnPostTaskDoneListener mListener;
    URL url;
    static final String COOKIES_HEADER = "Set-Cookie";

    boolean alreadyLoggedIn = false;

    @Override
    protected String doInBackground(String... strings) {

        try{

            if(strings[0].toLowerCase().equals("true")){
                alreadyLoggedIn = true;
            }
        }catch (Exception err){
            return null;
        }
        this.alreadyLoggedIn = alreadyLoggedIn;


        Log.i("ALREADY LOGGED?: ", alreadyLoggedIn+"");


        HttpURLConnection urlConnection = null;

        try {
            url = new URL(strings[1]);

            Log.i("URL POST: ",  strings[1]);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);


            if(strings.length>2)
            AddParams(urlConnection, getParamsFromString(strings[2]));








            int responseCode = urlConnection.getResponseCode();
            Log.i("RESPONSE CODE POST: ",  responseCode+"");
            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient", server_response);

            }else{
                Log.e("failed", "FAILED! Error code: "+responseCode);
                return "FAILED! Error code: "+responseCode;
            }




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return server_response;


        //return "This is the response";
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("POST: ",  "EXECUTING ON POST EXEC: "+s);
        super.onPostExecute(s);

        Log.e("Response", "" + server_response);

        mListener.onPostTaskDone(s);

    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public String getParamsFromMap(Map<String, String> params) {
        String paramsAsString = "";
        for(String key: params.keySet()){
            paramsAsString+=key+"@:@"+params.get(key)+"@-@";//This will be used to separate the stings later so it has to be somehow unique

        }
        Log.i("Params", paramsAsString);
        return paramsAsString;
    }

    public Map getParamsFromString(String paramsAsString){
        Map<String, String> map = new HashMap<String, String>();
        try{
            for(String parameter : paramsAsString.split("@-@")){
                map.put(parameter.split("@:@")[0], parameter.split("@:@")[1]);
            }
            return map;

        }catch (Exception err){
            Log.e("Failed", "Failed to convert params: "+err.getMessage());
            return null;
        }
    }

    private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    protected HttpURLConnection AddParams(HttpURLConnection urlConnection, Map<String, String> params) throws IOException {

        OutputStream os = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(params));

        writer.flush();
        writer.close();
        os.close();
        return urlConnection;
    }




    public interface OnPostTaskDoneListener {
        void onPostTaskDone(String responseData);

        void onError();
    }

}