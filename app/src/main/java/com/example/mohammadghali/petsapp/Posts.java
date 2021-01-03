package com.example.mohammadghali.petsapp;

import java.util.ArrayList;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohammadghali on 9/22/18.
 *
 * This class is used for fetching pets and storing them in lists
 */


public class Posts {

    @SerializedName("posts")
    @Expose
    protected List<Post> GetPostsResult = new ArrayList<Post>();

    public List<Post> getPosts() {
        return GetPostsResult;
    }

    public void setPosts(List<Post> posts) {
        this.GetPostsResult = posts;
    }

    public List<Post> getGetPostsResult() {
        return GetPostsResult;
    }

    public void setGetPostsResult(List<Post> getPostsResult) {
        GetPostsResult = getPostsResult;
    }

    @Override
    public String toString() {
        return "" + GetPostsResult.size();
    }

}
