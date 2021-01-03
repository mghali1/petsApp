package com.example.mohammadghali.petsapp;


/*
Created By Mohammad El-Ghali

This is the login fragment where you can login to our database. It uses our webservices written in PHP hosted on https://www.pitchkings.net/pets/Services


 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.mohammadghali.petsapp.Navigation.*;

public class login extends Fragment implements PostJson.OnPostTaskDoneListener{


    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    boolean alreadyLoggedIn = false;
    TextView login;
    TextView signUp;
    View myView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.activity_login, container, false);
        login = (TextView)myView.findViewById(R.id.lin);
        signUp = (TextView)myView.findViewById(R.id.sup);

        //Check for previous logins
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String currentToken = mPreferences.getString("token", "default");
        if(!currentToken.equals("default")){
            alreadyLoggedIn = true;
            Toast.makeText(getActivity(), "Already Logged in", Toast.LENGTH_LONG).show();
            getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, new MainActivityFrag()).commit();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= ((EditText)myView.findViewById(R.id.usrusr)).getText().toString().trim().toLowerCase();
                String password= ((EditText)myView.findViewById(R.id.pswrdd)).getText().toString();


                //I have added some methods to the PostJson class in which it can get a Map as a parameter. This map holds the post variables.
                PostJson pj= new PostJson();
                pj.mListener = login.this;

                Map<String, String> params= new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                String paramsAsString = pj.getParamsFromMap(params);
                if(alreadyLoggedIn)
                    pj.execute("true","http://www.pitchkings.net/pets/Services/signin.php",paramsAsString);
                else
                    pj.execute("false","http://www.pitchkings.net/pets/Services/signin.php",paramsAsString);

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, new register()).commit();

            }
        });


        return myView;
    }


    @Override
    public void onPostTaskDone(String responseData) {

        //Accept login -> Save credentials

        if(responseData.toLowerCase().contains("token")){
            //Save Token
            String inputJSONString = ""; // Your string JSON here

            Map<String, String> loginInfo = new HashMap<String, String>();
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(responseData);
                Iterator<String> keys = jObject.keys();

                while( keys.hasNext() ) {
                    String key = keys.next();
                    String value = jObject.getString(key);
                    loginInfo.put(key, value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("Token: ",loginInfo.get("token"));
            Log.i("Token: ",loginInfo.get("first_name"));
            Log.i("Token: ",loginInfo.get("last_name"));

            mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mEditor = mPreferences.edit();
            mEditor.putString("token", loginInfo.get("token"));
            mEditor.putString("first_name", loginInfo.get("first_name"));
            mEditor.putString("last_name", loginInfo.get("last_name"));
            mEditor.commit();

            Toast.makeText(getActivity(),"Login Successful",Toast.LENGTH_LONG).show();

            ((Navigation)getActivity()).toggleButtons();
            getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, new MainActivityFrag()).commit();
        }else{
            Toast.makeText(getActivity(), responseData, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError() {
        Toast.makeText(getActivity(), "Failed :(", Toast.LENGTH_LONG).show();
    }
}
