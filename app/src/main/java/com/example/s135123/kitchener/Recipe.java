package com.example.s135123.kitchener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by s130604 on 5-3-2016.
 */
public class Recipe {


    //Deze waardes kunnen leeg zijn, afhankelijk van wat de API geeft
    int id;
    String title;
    String image;

    int readyInMinutes;
    int cookingMinutes;
    int preparationMinutes;

    String summary;
    String instructions;
    boolean cheap = false;
    ArrayList<String> badges;   //properties of the recipe
    ArrayList<Ingredient> ingredients;

    String cuisine;

    int fat;
    int calories;
    int protein;
    int carbs;

    int servings; //number of servings I assume

    /*
    example of complexSearchResultJson:
    {
         "missedIngredientCount":7,
         "id":311403,
         "title":"Italian Hamburger Slices",
         "imageType":"jpeg",
         "protein":"41g",
         "fat":"53g",
         "likes":0,
         "usedIngredientCount":3,
         "calories":907,
         "image":"https://spoonacular.com/recipeImages/Italian-Hamburger-Slices-311403.jpeg",
         "carbs":"68g"
      }
     */

    //these are all json strings
    public Recipe(String cuisineJson, String summaryJson, String informationJson, String extractedByUrlJson, String complexSearchResultJson) {
        JSONObject cuisineObject = null;
        try {
            cuisineObject = new JSONObject(cuisineJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cuisine = getJsonInfo(cuisineObject, "cuisine");

        JSONObject summaryObject = null;
        try {
            summaryObject = new JSONObject(summaryJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        summary = getJsonInfo(summaryObject, "summary");

        JSONObject extractedByUrlObject = null;
        try {
            extractedByUrlObject = new JSONObject(extractedByUrlJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        instructions = getJsonInfo(extractedByUrlObject, "instructions");

        JSONObject informationObject = null;
        try {
            informationObject = new JSONObject(informationJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        readyInMinutes = Integer.parseInt(getJsonInfo(informationObject, "readyInMinutes"));
        preparationMinutes = Integer.parseInt(getJsonInfo(informationObject, "preparationMinutes"));
        cookingMinutes = Integer.parseInt(getJsonInfo(informationObject, "cookingMinutes"));
        servings = Integer.parseInt(getJsonInfo(informationObject, "servings"));
        id = Integer.parseInt(getJsonInfo(informationObject, "id"));
        title = getJsonInfo(informationObject, "title");
        image = getJsonInfo(informationObject, "image");
        if (getJsonInfo(informationObject, "cheap").equals("true")) {
            cheap = true;
        }
        JSONArray ingredientJSONArray = null;
        try {
            ingredientJSONArray = informationObject.getJSONArray("extendedIngredients");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < ingredientJSONArray.length(); i++) {
            try {
                JSONObject ingredientJSON = ingredientJSONArray.getJSONObject(i);
                Ingredient ingredient = new Ingredient(ingredientJSON);
                ingredients.add(ingredient);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONArray badgesJSONArray = null;
        try {
            badgesJSONArray = informationObject.getJSONArray("badges");
            for (int i = 0; i < badgesJSONArray.length(); i++) {
                badges.add(badgesJSONArray.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject searchObject = null;
        try {
            searchObject = new JSONObject(complexSearchResultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        protein = Integer.parseInt(getJsonInfo(searchObject, "protein"));
        fat = Integer.parseInt(getJsonInfo(searchObject, "fat"));
        carbs = Integer.parseInt(getJsonInfo(searchObject, "carbs"));
        calories = Integer.parseInt(getJsonInfo(searchObject, "calories"));
    }

    public String getJsonInfo(JSONObject o, String info) {
        try {
            return o.getJSONObject(info).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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

    public String getInstructions() {
        return instructions;
    }

    public boolean isCheap() {
        return cheap;
    }

    public ArrayList<String> getBadges() {
        return badges;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getCuisine() {
        return cuisine;
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
