package com.example.s135123.kitchener;

import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;

import java.util.ArrayList;

/**
 * Created by s130604 on 16-3-2016.
 */
public class User {
    private static User user;
    private ArrayList<Allergy> allergies = new ArrayList<>();
    boolean gender; //male=true
    private ArrayList<Recipe> favoriteRecipes = new ArrayList<>();
    SharedPreferences prefs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;


    public enum Allergy {
        dairy, egg, gluten, peanut, sesame, seafood, shellfish, soy, sulfite, tree, nut, wheat
    }

    private User() {
        prefs = new SecurePreferences(RegisterActivity.applicationContext);
    }

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void removeFromFavorites(Recipe recipe) {
        favoriteRecipes.remove(recipe);
    }

    public void addToFavorites(Recipe recipe) {
        favoriteRecipes.add(recipe);
    }

    public ArrayList<Recipe> getFavorites() {
        return favoriteRecipes;
    }

    public void addAllergy(Allergy allergy) {
        allergies.add(allergy);
    }

    public void removeAllergy(Allergy allergy) {
        allergies.remove(allergy);
    }

    public void setMale() {
        gender = true;
    }

    public void setFemale() {
        gender = false;
    }

    public boolean getGender() {
        return gender;
    }

    public String getUsername() {
        return prefs.getString("username", null);
    }

    public void setUsername(String username) {
        prefs.edit().putString("username", username).commit();
    }

    public String getPassword() {
        return prefs.getString("password", null);
    }

    public void setPassword(String password) {
        prefs.edit().putString("password", password).commit();
    }
}