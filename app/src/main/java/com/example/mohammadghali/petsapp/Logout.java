package com.example.mohammadghali.petsapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohammadghali on 12/2/18.
 *
 *
 * This is the logout activity where it logs you out and deletes the
 */

public class Logout extends AppCompatActivity implements PostJson.OnPostTaskDoneListener {

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Logout ();

    }

    public void Logout (){
        //Check for previous logins
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentToken = mPreferences.getString("token", "default");
        if(currentToken.equals("default")){
            Toast.makeText(this, "Not Logged in", Toast.LENGTH_LONG).show();
            Intent k = new Intent(Logout.this, Navigation.class);
            startActivity(k);

        }else{
            //Call the logout webservice
            //Toast.makeText(this, "Attempting to logout", Toast.LENGTH_LONG).show();
            PostJson pj= new PostJson();
            pj.mListener = Logout.this;
            pj.execute("false", "http://www.pitchkings.net/pets/Services/signout.php");
        }
    }
    @Override
    public void onPostTaskDone(String responseData) {

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        mEditor.putString("token", "default");
        mEditor.putString("first_name", "Unknown");
        mEditor.putString("last_name", "Unknown");
        mEditor.commit();
        String currentToken = mPreferences.getString("token", "default");
        if(currentToken.equals("default")){
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_LONG).show();

        }
        Intent k = new Intent(Logout.this, Navigation.class);
        startActivity(k);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Failed to logout", Toast.LENGTH_LONG).show();
    }
}
