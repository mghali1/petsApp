package com.example.mohammadghali.petsapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mohammadghali on 11/25/18.
 */

public class MainActivityFrag extends Fragment {
    View myView;
    Posts posts = new Posts();

    GridView gridView;
    Button addNewPostBtn;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;


    @Override
    public void onResume() {
        super.onResume();
        getJsonPets();
    }


    public void getJsonPets(){

        //New Retrofit call
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https:pitchkings.net/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        GetPosts gp = retrofit.create(GetPosts.class);
        Call<List<Post>> call= gp.posts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                try {
                    List<Post> postsData = response.body();
                    posts.setPosts(postsData);
                    GridViewAdapter gva = new GridViewAdapter(LayoutInflater.from(getActivity()), posts.getPosts());
                    ((Navigation)getActivity()).setAdapter(gva);
                    gridView.setAdapter(gva);
                }catch (Exception err){

                    Log.e("Didn't fetch correctly", "Fragment was closed before fetching");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                try {
                    Toast.makeText(getActivity(), "Fetching items failed", Toast.LENGTH_SHORT).show();
                }catch (Exception err){
                    Log.e("Couldn't fetch pets", t.getMessage().toString());
                }
            }
        });

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.activity_main, container, false);
        gridView = (GridView) myView.findViewById(R.id.gridview);
        addNewPostBtn = (Button)myView.findViewById(R.id.addNewPostBtn);
        addNewPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, new NewPostFrag()).commit();
            }
        });
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String currentToken = mPreferences.getString("token", "default");
        if(currentToken.equals("default")){//if not logged in
            addNewPostBtn.setVisibility(View.GONE);
        }


        Toast.makeText(this.getActivity(), "Fetching...", Toast.LENGTH_SHORT).show();

        getJsonPets();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Post selectedPerson = posts.GetPostsResult.get(i);

                //Toast.makeText(getActivity(), selectedPerson.getPet_name() +" " + selectedPerson.getPost_id()+ " "+selectedPerson.getPath(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), GridItem.class);
                Gson g = new Gson();
                intent.putExtra("currentPost", g.toJson(selectedPerson));
                startActivity(intent);

            }
        });
        return myView;
    }
}
