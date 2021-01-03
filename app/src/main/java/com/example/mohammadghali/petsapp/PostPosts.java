package com.example.mohammadghali.petsapp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by mohammadghali on 9/26/18.\
 *
 *
 * This interface is used for the newpsot call for the retrofit api
 */
public interface PostPosts {
    @Multipart
    @POST("/pets/Services/post.php")
    Call<ResponseBody> newPost(
            @Part("token") RequestBody token,
            @Part("pet_gender") RequestBody pet_gender,
            @Part("pet_name") RequestBody pet_name,
            @Part("pet_age") RequestBody pet_age,
            @Part("pet_breed") RequestBody pet_breed,
            @Part("pet_type") RequestBody pet_type,
            @Part("contact_phone") RequestBody contact_phone,
            @Part("post_desc") RequestBody post_desc,
            @Part MultipartBody.Part image
    );
}
