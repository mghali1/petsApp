package com.example.mohammadghali.petsapp;


/*

Created by Mohammad El-Ghali
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.util.List;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    public GridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Go to main frag (Home  Page)
        FragmentManager fragmentManager = getFragmentManager();


        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        toggleButtons();



        if(savedInstanceState != null) {

            //Check for previous logins
            mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String currentToken = mPreferences.getString("token", "default");
            if(!currentToken.equals("default")){//Loggen in already
                Toast.makeText(this, "Welcome Back :)", Toast.LENGTH_LONG).show();

            }

        }else{

            fragmentManager.beginTransaction().replace(R.id.content_frame, new MainActivityFrag(), "Main").commit();
        }
    }

    public void setAdapter(GridViewAdapter gva){
        this.adapter = gva;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        toggleButtons();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new MainActivityFrag(), "Main").commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.search_menu , menu);

        MenuItem search = menu.findItem(R.id.search_pets);
        if(search != null){
            SearchView searchView = (SearchView)search.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if(adapter!=null)
                        adapter.getFilter().filter(s);

                    return false;
                }
            });
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        toggleButtons();

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /*

        This method redirects the user to the specified fragments in the menu

         */
        toggleButtons();

        int id = item.getItemId();


        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_mainActivity) {

            fragmentManager.beginTransaction().replace(R.id.content_frame, new MainActivityFrag(), "Main").commit();
        } else if (id == R.id.nav_newPost) {
            //New Post
            fragmentManager.beginTransaction().replace(R.id.content_frame, new NewPostFrag(), "NewPost").commit();
        } else if (id == R.id.nav_login) {
            //Login
            fragmentManager.beginTransaction().replace(R.id.content_frame, new login(), "Login").commit();
        }else if (id == R.id.nav_logout) {
            //Logout
            Intent k = new Intent(Navigation.this, Logout.class);
            startActivity(k);
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MainActivityFrag(), "Main").commit();
        } else if (id == R.id.nav_settings) {
            //settings
            fragmentManager.beginTransaction().replace(R.id.content_frame, new settingsFrag(), "Settings").commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void toggleButtons(){
        /*

        This method hides and unhides the login and logout buttons in the drawer menu accordingly
         */
        String currentToken2 = mPreferences.getString("token", "default");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        if(!currentToken2.equals("default")){//Logged in already

            TextView name = (TextView)navigationView.getHeaderView(0).findViewById(R.id.nameTV);

            String first_name =mPreferences.getString("first_name", "Unknown") ;
            String last_name =mPreferences.getString("last_name", "Unknown") ;
            name.setText(first_name+" "+last_name);


            MenuItem loginBtn = (MenuItem) menu.findItem(R.id.nav_login);
            loginBtn.setVisible(false);

            MenuItem logoutBtn = (MenuItem) menu.findItem(R.id.nav_logout);
            logoutBtn.setVisible(true);


        }else {
            MenuItem loginBtn = (MenuItem) menu.findItem(R.id.nav_login);
            loginBtn.setVisible(true);

            MenuItem logoutBtn = (MenuItem) menu.findItem(R.id.nav_logout);
            logoutBtn.setVisible(false);

        }
    }


}
