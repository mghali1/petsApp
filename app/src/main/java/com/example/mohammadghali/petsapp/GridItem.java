package com.example.mohammadghali.petsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

/**
 * Created by mohammadghali on 11/20/18.
 *
 *
 * This is the Activity opens when a pet is clicked
 */


public class GridItem extends AppCompatActivity {
    Post current;
    TextView pet_name;
    TextView pet_desc;
    ImageView pet_image;
    Button callBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_item);

        pet_name = (TextView) findViewById(R.id.pet_name_tv);
        pet_image = (ImageView) findViewById(R.id.pet_image_iv);
        pet_desc = (TextView) findViewById(R.id.petDesc_tv);
        callBtn = (Button) findViewById(R.id.callBtn);


        //In the intent, I have put the Post class as a serialized JSON, and now we deserialize it
        Intent intent = getIntent();
        Gson g = new Gson();
        final String currentPostJSON = intent.getStringExtra("currentPost");
        current = g.fromJson(currentPostJSON, Post.class);

        pet_name.setText(current.getPet_name());
        pet_desc.setText(current.getPost_desc() + "\nAge: " + current.getPet_age() + "\nType: " + current.getPet_type() + "\nBreed: " + current.getPet_breed() + "\nGender: " + current.getPet_gender() + "\nTo contact, call: " + current.getContact_phone() + "\n\n\nOwner: " + current.getFirst_name() + " " + current.getLast_name());


        //Glide is an image downloader library
        if (current.getPath() != null) {
            if (current.getPath().trim() != "" && !current.getPath().trim().equals("null")) {

                Glide.with(this)
                        .load(current.getPath())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.defaultimage))
                        .into(pet_image);


            }
        }

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current.getContact_phone().matches("[0-9-/]+")) {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + current.getContact_phone()));
                        startActivity(callIntent);
                    }catch (Exception err){
                        Log.e("Call failed", err.getMessage().toString());
                    }

                }else{
                    Toast.makeText(GridItem.this, "Phone number not provided", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
