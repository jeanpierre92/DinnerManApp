package com.example.s135123.kitchener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by s130604 on 5-3-2016.
 */
public class Recipe implements Serializable{


    //Deze waardes kunnen leeg zijn, afhankelijk van wat de API geeft
    int id;
    String title;
    String image;

    int readyInMinutes;
    int cookingMinutes;
    int preparationMinutes;

    String summary;
    ArrayList<String> instructions = new ArrayList<>();
    boolean cheap = false;
    ArrayList<String> badges;   //properties of the recipe
    ArrayList<Ingredient> ingredients;

    int fat;
    int calories;
    int protein;
    int carbs;

    int servings; //number of servings I assume

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
                image = o.getString("image");
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

            try {
                summary = o.getString("summary");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray instructionsJsonArray = null;
            try {
                instructionsJsonArray = o.getJSONArray("instructions");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < instructionsJsonArray.length(); i++) {
                    instructions.add(instructionsJsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                cheap = o.getBoolean("cheap");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray badgesJsonArray = null;
            try {
                badgesJsonArray = o.getJSONArray("badges");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < badgesJsonArray.length(); i++) {
                    badges.add(badgesJsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray ingredientsJsonArray = null;
            try {
                ingredientsJsonArray = o.getJSONArray("ingredients");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < ingredientsJsonArray.length(); i++) {
                    ingredients.add(new Ingredient(ingredientsJsonArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                fat = o.getInt("fat");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                calories = o.getInt("calories");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                protein = o.getInt("protein");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                carbs = o.getInt("carbs");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                servings = o.getInt("servings");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    public ArrayList<String> getInstructions() {
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
