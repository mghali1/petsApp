package com.example.mohammadghali.petsapp;

/**
 * Created by mohammadghali on 9/22/18.
 *
 * This is the Post class where the pet ad is stored
 */

public class Post {

    private String post_id;
    private String first_name;
    private String last_name;
    private String user_id;
    private String pet_name;
    private String pet_age;
    private String pet_breed;
    private String pet_type;
    private String pet_gender;
    private String contact_phone;
    private String post_date;
    private String post_type;
    private String post_status;
    private String post_desc;
    private String path;


    public Post(String post_id, String first_name, String last_name, String user_id, String pet_name, String pet_age, String pet_breed, String pet_type, String pet_gender, String contact_phone, String post_date, String post_type, String post_status, String post_desc, String path) {

        this.post_id = post_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_id = user_id;
        this.pet_name = pet_name;
        this.pet_age = pet_age;
        this.pet_breed = pet_breed;
        this.pet_type = pet_type;
        this.pet_gender = pet_gender;
        this.contact_phone = contact_phone;
        this.post_date = post_date;
        this.post_type = post_type;
        this.post_status = post_status;
        this.post_desc = post_desc;
        this.path = path;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPet_age() {
        return pet_age;
    }

    public void setPet_age(String pet_age) {
        this.pet_age = pet_age;
    }

    public String getPet_breed() {
        return pet_breed;
    }

    public void setPet_breed(String pet_breed) {
        this.pet_breed = pet_breed;
    }

    public String getPet_type() {
        return pet_type;
    }

    public void setPet_type(String pet_type) {
        this.pet_type = pet_type;
    }

    public String getPet_gender() {
        return pet_gender;
    }

    public void setPet_gender(String pet_gender) {
        this.pet_gender = pet_gender;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getPost_status() {
        return post_status;
    }

    public void setPost_status(String post_status) {
        this.post_status = post_status;
    }

    public String getPost_desc() {
        return post_desc;
    }

    public void setPost_desc(String post_desc) {
        this.post_desc = post_desc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String image_path) {
        this.path = image_path;
    }

    @Override
    public String toString() {
        return post_id +" "+ pet_name+" "+pet_type+" "+pet_gender+" "+pet_age+" "+pet_breed;
    }
}
