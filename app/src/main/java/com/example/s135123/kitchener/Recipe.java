package com.example.s135123.kitchener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by s130604 on 5-3-2016.
 */
public class Recipe implements Serializable{

    int id;
    String title;
    String image;

    int readyInMinutes;
    int cookingMinutes;
    int preparationMinutes;

    public String getCuisine() {
        return cuisine;
    }

    String summary;
    ArrayList<String> instructions = new ArrayList<>();
    boolean cheap = false;
    ArrayList<String> ingredients = new ArrayList<>();

    int fat;
    int calories;
    int protein;
    int carbs;

    int servings;

    String cuisine;

    //constructor for gson.fromjson to use
    public Recipe(){}

    //these are all json strings
    public Recipe(String json) {
        JSONObject o = null;
        try {
            o = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (o != null) {
            try {
                id = o.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                title = o.getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                cuisine = o.getString("cuisine");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                image = o.getString("image");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                summary = o.getString("summary");
                summary = Jsoup.parse(summary).text();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String instructionString = null;
            try {
                instructionString = o.getString("instructions");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Pattern regex = Pattern.compile("<li>(.+?)</li>");
            Matcher matcher = regex.matcher(instructionString);
            while (matcher.find()) {
                instructions.add(matcher.group(1));
            }
            String ingredientString = "";
            try {
                ingredientString = o.getString("ingredients");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(String ingredient:ingredientString.split("\n")){
                ingredients.add(ingredient);
            }
            String fatString="";
            try{
                fatString = o.getString("fat");
                fat = Integer.parseInt(fatString.replaceAll("[^\\d]", ""));
            }catch(Exception e){
                e.printStackTrace();
            }
            String proteinString="";
            try{
                proteinString = o.getString("protein");
                protein = Integer.parseInt(proteinString.replaceAll("[^\\d]", ""));
            }catch(Exception e){
                e.printStackTrace();
            }
            String carbsString="";
            try{
                carbsString = o.getString("carbs");
                carbs = Integer.parseInt(carbsString.replaceAll( "[^\\d]", ""));
            }catch(Exception e){
                e.printStackTrace();
            }
            try {
                calories = o.getInt("calories");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                servings = o.getInt("servings");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                readyInMinutes = o.getInt("readyInMinutes");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                cookingMinutes = o.getInt("cookingMinutes");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                preparationMinutes = o.getInt("preparationMinutes");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public int getCookingMinutes() {
        return cookingMinutes;
    }

    public int getPreparationMinutes() {
        return preparationMinutes;
    }

    public String getSummary() {
        return summary;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public int getFat() {
        return fat;
    }

    public int getCalories() {
        return calories;
    }

    public int getProtein() {
        return protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getServings() {
        return servings;
    }
}
