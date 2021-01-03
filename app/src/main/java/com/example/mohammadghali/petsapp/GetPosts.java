package com.example.mohammadghali.petsapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by mohammadghali on 9/26/18.
 *
 * This is an interface used for retrofit to fetch the pets
 */

public interface GetPosts {
    @GET("/pets/Services/fetch_pets.php?")
    Call<List<Post>> posts();

}
