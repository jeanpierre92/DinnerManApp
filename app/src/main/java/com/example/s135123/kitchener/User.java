package com.example.s135123.kitchener;

import java.util.ArrayList;

/**
 * Created by s130604 on 16-3-2016.
 */
public class User {
    private static User user;
    private String userName;
    private ArrayList<Allergy> allergies;
    boolean gender; //male=true
    private ArrayList<Recipe> favoriteRecipes;

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

    private User(){

    }
    public User getInstance(){
        if(user==null){
            user = new User();
            allergies = new ArrayList<>();
            favoriteRecipes = new ArrayList<>();
        }
        return user;
    }
    public void removeFromFavorites(Recipe recipe){
        favoriteRecipes.remove(recipe);
    }
    public void addToFavorites(Recipe recipe){
        favoriteRecipes.add(recipe);
    }
    public ArrayList<Recipe> getFavorites(){
        return favoriteRecipes;
    }
    public void addAllergy(Allergy allergy){
        allergies.add(allergy);
    }
    public void removeAllergy(Allergy allergy){
        allergies.remove(allergy);
    }
    public void setMale(){
        gender=true;
    }
    public void setFemale(){
        gender=false;
    }
    public boolean getGender(){
        return gender;
    }
    public void setUserName(String name){
        userName=name;
    }
    public String getUserName(){
        return userName;
    }
}
