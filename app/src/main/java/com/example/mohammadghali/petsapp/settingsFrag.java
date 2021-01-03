package com.example.mohammadghali.petsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mohammadghali on 12/10/18.
 */

public class settingsFrag extends Fragment {

    /*

        SETTINGS FUNCTIONALITY WERE NOT ADDED FOR THE SCOPE OF THIS PROJECT - WE WILL ADD THEM LATER WHEN THIS APP HITS THE PUBLIC

     */
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.activity_settings, container, false);
        return myView;
    }
}
