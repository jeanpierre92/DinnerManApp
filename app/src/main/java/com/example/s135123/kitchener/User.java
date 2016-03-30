package com.example.s135123.kitchener;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.securepreferences.SecurePreferences;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by s130604 on 16-3-2016.
 */
public class User {
    private static User user;
    private ArrayList<String> allergies = new ArrayList<>();
    boolean gender; //male=true
    private ArrayList<Integer> favoriteRecipes = new ArrayList<>();
    boolean shakeEnabled = true;
    SharedPreferences prefs;
    SharedPreferences sharedPreferences;
    private boolean didScheduleTutorial;
    private boolean didRecTutorial;

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
        prefs = new SecurePreferences(LoadingScreenActivity.applicationContext);

    }

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void removeFromFavorites(int id) {
        favoriteRecipes.remove((Integer) id);
    }

    public void setShakeEnabled(boolean shakeEnabled) {
        if(shakeEnabled) {
            prefs.edit().putString("shakeEnabled", "true").commit();
        }
        else {
            prefs.edit().putString("shakeEnabled", "false").commit();
        }
    }

    public boolean getShakeEnabled() {
        return(prefs.getString("shakeEnabled", "false").equals("true"));
    }

    public void addToFavorites(int id) {
        favoriteRecipes.add(id);
    }

    public ArrayList<Integer> getFavorites() {
        return favoriteRecipes;
    }

    public void addAllergy(String allergy) {
        allergies.add(allergy);
    }

    public void removeAllergy(String allergy) {
        allergies.remove(allergy);
    }

    public ArrayList<String> getAllergies() { return allergies; }

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
    public void setDidScheduleTutorial(boolean b) {
        if(b) {
            prefs.edit().putString("didScheduleTutorial", "true").commit();
        }
        else {
            prefs.edit().putString("didScheduleTutorial", "false").commit();
        }
    }
    public boolean getDidScheduleTutorial(){
        return(prefs.getString("didScheduleTutorial", "false").equals("true"));
    }
    public void setDidRecTutorial(boolean b) {
        if(b) {
            prefs.edit().putString("didRecTutorial", "true").commit();
        }
        else {
            prefs.edit().putString("didRecTutorial", "false").commit();
        }
    }
    public boolean getDidRecTutorial(){
        return(prefs.getString("didRecTutorial", "false").equals("true"));
    }
    public boolean getDidSearchTutorial(){
        return(prefs.getString("didSearchTutorial", "false").equals("true"));
    }
    public void setDidSearchTutorial(boolean b) {
        if(b) {
            prefs.edit().putString("didSearchTutorial", "true").commit();
        }
        else {
            prefs.edit().putString("didSearchTutorial", "false").commit();
        }
    }
}
