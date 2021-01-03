package com.example.mohammadghali.petsapp;


/*
Created by Mohammad El Ghali

This is the register fragment where you can register a new account.

it uses our signup service written by Mohammad El Ghali in PHP hosted on pitchkings.net/pets/Services

 */
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class register extends Fragment implements PostJson.OnPostTaskDoneListener{
    View myView;
    TextView signUpBtn;
    TextView alreadyHaveAnAccont;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    boolean alreadyLoggedIn =false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.activity_signup, container, false);

        //Check for previous logins
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String currentToken = mPreferences.getString("token", "default");
        if(!currentToken.equals("default")) {
            alreadyLoggedIn = true;
            Toast.makeText(getActivity(), "Already Logged in", Toast.LENGTH_LONG).show();
        }


        alreadyHaveAnAccont = (TextView)myView.findViewById(R.id.lin);
        alreadyHaveAnAccont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, new login()).commit();
            }
        });


        signUpBtn = (TextView)myView.findViewById(R.id.sup);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signup
                PostJson pj= new PostJson();
                pj.mListener = register.this;

                String email= ((EditText)myView.findViewById(R.id.mail)).getText().toString().trim().toLowerCase();
                String password= ((EditText)myView.findViewById(R.id.pswrdd)).getText().toString();
                String phone= ((EditText)myView.findViewById(R.id.phoneNumberTB)).getText().toString();
                String firstName= ((EditText)myView.findViewById(R.id.fname)).getText().toString();
                String lastName= ((EditText)myView.findViewById(R.id.lname)).getText().toString();

                Map<String, String> params= new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("phone_number", phone);

                String paramsAsString = pj.getParamsFromMap(params);

                if(alreadyLoggedIn)
                    pj.execute("true","http://www.pitchkings.net/pets/Services/signup.php",paramsAsString);
                else
                    pj.execute("false","http://www.pitchkings.net/pets/Services/signup.php",paramsAsString);


            }
        });
        return myView;
    }

    @Override
    public void onPostTaskDone(String responseData) {
        Toast.makeText(getActivity(), responseData, Toast.LENGTH_LONG).show();


        getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, new login()).commit();
    }

    @Override
    public void onError() {
        Toast.makeText(getActivity(), "An error occurred :(", Toast.LENGTH_LONG).show();
    }
}
