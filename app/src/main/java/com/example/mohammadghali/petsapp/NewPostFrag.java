package com.example.mohammadghali.petsapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Mohammad El-Ghali on 11/25/18.
 */

public class NewPostFrag extends Fragment {
    View myView;
    PostJson postJson;
    Spinner petTypes;
    Spinner petAge;
    Spinner petBreedSpinner;
    RadioGroup petGender;
    Button cancel;
    Button submit;
    ImageButton addPic;
    ImageView showImageView;
    Uri ImageUri;
    String imageFilePath;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //When the image is captured/selected, this method will save its path and loads the image into the image view

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                showImageView.setImageURI(resultUri);
                try {
                    imageFilePath = resultUri.getPath();
                    ImageUri = resultUri;
                }catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG).show();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(),error+"",Toast.LENGTH_LONG);
            }
        }
    }


    public void toggleSubmit (boolean newState){
        submit.setEnabled(newState);
    }



    private void submitPost(String...strings){
        //Attempts to submit the post -> Uses retofit's api

        try {
            String petName = ((EditText) myView.findViewById(R.id.petNameTB)).getText().toString();
            String contactPhone = ((EditText) myView.findViewById(R.id.contactPhoneTB)).getText().toString();
            String postDesc = ((EditText) myView.findViewById(R.id.postDescTB)).getText().toString();
            String petType = petTypes.getSelectedItem().toString();
            String petBreed = petBreedSpinner.getSelectedItem().toString();
            String petAgeString = petAge.getSelectedItem().toString();
            petGender = (RadioGroup)myView.findViewById(R.id.radioGender);
            int selectedId = petGender.getCheckedRadioButtonId();
            RadioButton genderBtn= (RadioButton) myView.findViewById(selectedId);
            String petGenderString = genderBtn.getText().toString();



            File originalFile = new File(imageFilePath);

            MultipartBody.Part image = MultipartBody.Part.createFormData("image", originalFile.getName(), RequestBody.create(MediaType.parse("image/*"), originalFile));

            String token = mPreferences.getString("token", "default");
            RequestBody tokenRB = RequestBody.create(
                    MediaType.parse("text/plain"),
                    token);
            RequestBody genderRB = RequestBody.create(
                    MediaType.parse("text/plain"),
                    petGenderString);
            RequestBody petNameRB = RequestBody.create(
                    MediaType.parse("text/plain"),
                    petName);
            RequestBody contactPhoneRB = RequestBody.create(
                    MediaType.parse("text/plain"),
                    contactPhone);
            RequestBody postDescRB = RequestBody.create(
                    MediaType.parse("text/plain"),
                    postDesc);
            RequestBody petTypeRB = RequestBody.create(
                    MediaType.parse("text/plain"),
                    petType);
            RequestBody petBreedRB = RequestBody.create(
                    MediaType.parse("text/plain"),
                    petBreed);
            RequestBody petAgeStringRB = RequestBody.create(
                    MediaType.parse("text/plain"),
                    petAgeString);

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https:pitchkings.net/");
            Retrofit retrofit = builder.build();
            PostPosts gp = retrofit.create(PostPosts.class);

            Call<ResponseBody> call = gp.newPost(tokenRB, genderRB, petNameRB, petAgeStringRB, petBreedRB, petTypeRB, contactPhoneRB, postDescRB, image);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, new MainActivityFrag()).commit();
                    } else {
                        Toast.makeText(getActivity(), "Failed to upload post", Toast.LENGTH_LONG).show();
                        toggleSubmit(true);
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Error in posting: "
                            , t.getMessage().toString() + " - " + t.getCause());
                    Toast.makeText(getActivity(), "Failed. Check your internet connection.", Toast.LENGTH_LONG).show();
                    toggleSubmit(true);
                }
            });
        }catch (Exception err){
            toggleSubmit(true);
            Toast.makeText(getActivity(), "Failed. Please add a photo.", Toast.LENGTH_LONG).show();
            Log.e("Error in posting ", err.getMessage().toString());
        }


    }

    //getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, new MainActivityFrag()).commit();


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_new_post, container, false);

        //Check for previous logins
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String currentToken = mPreferences.getString("token", "default");
        if(currentToken.equals("default")){//not logged in
            Toast.makeText(getActivity(), "Not Logged in", Toast.LENGTH_LONG).show();
            getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, new login()).commit();
        }

        //Set up buttons
        cancel = (Button)myView.findViewById(R.id.cancelBtn);
        submit = (Button)myView.findViewById(R.id.submitBtn);
        addPic = (ImageButton)myView.findViewById(R.id.takePicBtn);
        showImageView = (ImageView)myView.findViewById(R.id.showPicView);

        addPic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                CropImage.activity().setAspectRatio(1,1).start(getContext(), getVisibleFragment());

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction().replace(R.id.content_frame, new MainActivityFrag()).commit();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toggleSubmit(false);

                petTypes = (Spinner)myView.findViewById(R.id.petSpinner);
                petBreedSpinner = (Spinner)myView.findViewById(R.id.petBreedSpinner);
                petAge = (Spinner)myView.findViewById(R.id.petAgeSpinner);
                String petNameString = ((EditText)myView.findViewById(R.id.petNameTB)).getText().toString();
                String contactPhone = ((EditText)myView.findViewById(R.id.contactPhoneTB)).getText().toString();
                String postDesc = ((EditText)myView.findViewById(R.id.postDescTB)).getText().toString();
                String petTypeString = petTypes.getSelectedItem().toString();
                String petBreedString = petBreedSpinner.getSelectedItem().toString();
                String petAgeString = petAge.getSelectedItem().toString();
                petGender = (RadioGroup)myView.findViewById(R.id.radioGender);
                int selectedId = petGender.getCheckedRadioButtonId();
                RadioButton genderBtn= (RadioButton) myView.findViewById(selectedId);
                String petGenderString = genderBtn.getText().toString();


                    submitPost();//Submits the post
            }
        });

        /*

            Filling up the spinners are hardcoded, but we are planning to create a webservice for them that retrieves the info.
            Our webservices are hosted on https://pitchkings.net/pets/Services and are written in PHP. A copy of the webservices code
            will be provided in the submission folder.

            All of the webservices are developed by: Mohammad El-Ghali


         */

        //Fill the spinner up
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Cat");
        spinnerArray.add("Dog");
        spinnerArray.add("Bird");
        spinnerArray.add("Fish");
        spinnerArray.add("Other");

        List<String> ageArray =  new ArrayList<String>();
        ageArray.add("Less than 1 year old");
        ageArray.add("1 to 2 years old");
        ageArray.add("2 to 3 years old");
        ageArray.add("3 to 4 years old");
        ageArray.add("4 to 5 years old");
        ageArray.add("more than 5 years old");





        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner petTypes = (Spinner) myView.findViewById(R.id.petSpinner);
        petTypes.setAdapter(adapter);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, ageArray);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petAge = (Spinner) myView.findViewById(R.id.petAgeSpinner);
        //Spinner petBreedSpinner = (Spinner) findViewById(R.id.petBreedSpinner);
        petAge.setAdapter(adapter2);



        petTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = petTypes.getSelectedItem().toString();
                List<String> petBreeds = new ArrayList<>();
                if (selected.equals("Cat")) {
                    petBreeds.clear();
                    petBreeds.add("Domestic Shorthair");
                    petBreeds.add("Domestic Longhair");
                    petBreeds.add("Persian");
                    petBreeds.add("Maine Coon");
                    petBreeds.add("Siamese");
                    petBreeds.add("American Shorthair");
                    petBreeds.add("Abyssinian");
                    petBreeds.add("Exotic Shorthair");
                    petBreeds.add("Ragdoll");
                    petBreeds.add("Burmese");
                    petBreeds.add("Himalayan");
                    Collections.sort(petBreeds);
                    petBreeds.add("Other");
                }else if(selected.equals("Dog")){
                    petBreeds.clear();
                    petBreeds.add("Retrievers (Labrador)");
                    petBreeds.add("German Shepherd Dogs");
                    petBreeds.add("Retrievers (Golden)");
                    petBreeds.add("French Bulldogs");
                    petBreeds.add("Bulldogs");
                    petBreeds.add("Beagles");
                    petBreeds.add("Poodles");
                    petBreeds.add("Rottweilers");
                    petBreeds.add("Yorkshire Terriers");
                    petBreeds.add("Pointers (German Shorthaired)");
                    petBreeds.add("Boxers");
                    petBreeds.add("Siberian Huskies");
                    petBreeds.add("Dachshunds");
                    petBreeds.add("Great Danes");
                    petBreeds.add("Pembroke Welsh Corgis");
                    petBreeds.add("Doberman Pinschers");
                    petBreeds.add("Australian Shepherds");
                    petBreeds.add("Miniature Schnauzers");
                    petBreeds.add("Pug");
                    petBreeds.add("Shih Tzu");
                    petBreeds.add("Yorkie");
                    petBreeds.add("Chihuahua");
                    Collections.sort(petBreeds);
                    petBreeds.add("Other");
                }else if(selected.equals("Bird")){
                    petBreeds.clear();
                    petBreeds.add("Other");
                }else if(selected.equals("Fish")){
                    petBreeds.clear();
                    petBreeds.add("Other");

                }else{//other
                    petBreeds.clear();
                    petBreeds.add("Other");
                    //let user type

                }
                //add spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        petTypes.getContext() , android.R.layout.simple_spinner_item, petBreeds);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                final Spinner petBreedSpinner = (Spinner) myView.findViewById(R.id.petBreedSpinner);
                petBreedSpinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        return myView;
    }
}
