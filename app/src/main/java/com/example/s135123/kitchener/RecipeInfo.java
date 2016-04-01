package com.example.s135123.kitchener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Shows the recipe information in the activity that was passed
 */
public class RecipeInfo {
    Activity activity;
    ImageView imageView;
    TextView summaryView;
    TextView instructionsView;
    TextView titleView;
    TextView nutritionView;
    TextView servingsView;
    TextView ingredientsView;
    TextView cuisineView;
    TextView noContentText;
    RelativeLayout recipeInfoContentLayout;

    public RecipeInfo(Activity activity) {
        this.activity = activity;
    }

    public void updateContents(Recipe recipe){
        noContentText = (TextView) activity.findViewById(R.id.recipe_info_no_content);
        noContentText.setVisibility(View.GONE);
        recipeInfoContentLayout = (RelativeLayout) activity.findViewById(R.id.content_recipe_info_layout);
        recipeInfoContentLayout.setVisibility(View.VISIBLE);
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        //only init if this was not done before
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        }
        imageView = (ImageView) activity.findViewById(R.id.imageView);
        final DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        imageLoader.displayImage(recipe.getImage(), imageView, new SimpleImageLoadingListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                int finalWidth = metrics.widthPixels;
                //if the info is displayed on a tablet, the width of the image is width of screen - width of
                //the SlidingTablLayout (on a phone this is opened in a new activity instead)
                if(!activity.getResources().getBoolean(R.bool.isPhone)){
                    finalWidth -=  (int) activity.getResources().getDimension(R.dimen.list_width);;
                }
                int width = loadedImage.getWidth();
                layoutParams.width = finalWidth;
                int height = loadedImage.getHeight();
                layoutParams.height =  finalWidth* height / width;
                imageView.setLayoutParams(layoutParams);
            }
        });
        summaryView = (TextView) activity.findViewById(R.id.summary);
        summaryView.setText(recipe.getSummary());
        instructionsView = (TextView) activity.findViewById(R.id.instructions);
        instructionsView.setText("");
        ArrayList<String> instructions = recipe.getInstructions();
        int num = 1;
        for (int i = 0; i < instructions.size(); i++) {
            String instruction = instructions.get(i);
            if(instruction.length()>1) {
                instructionsView.append(num + ". " + instruction);
                if (i != instructions.size() - 1) {
                    instructionsView.append("\n");
                }
                num++;
            }
        }
        titleView = (TextView) activity.findViewById(R.id.title);
        titleView.setText(recipe.getTitle());
        cuisineView = (TextView) activity.findViewById(R.id.cuisine_textview);
        if (recipe.getCuisine().matches("[aeiou].*$")) {
            cuisineView.setText("An " + recipe.getCuisine() + " recipe");
        } else {
            cuisineView.setText("A " + recipe.getCuisine() + " recipe");
        }
        nutritionView = (TextView) activity.findViewById(R.id.nutrition);
        nutritionView.setText("Calories: " + recipe.getCalories() + "\nFat: " + recipe.getFat() + "g\nProtein: " + recipe.getProtein() + "g\nCarbs: " + recipe.getCarbs() + "g");
        //nutritionView.setText("Calories: 55g\nFat: 5g\nProtein: 5g\nCarbs: 5g");
        servingsView = (TextView) activity.findViewById((R.id.servingsAndMinutes));
        String servingsString = recipe.getServings() + " serving";
        if(recipe.getServings()>1){
            servingsString+="s";
        }
        servingsString+="\n";
        if (recipe.getPreparationMinutes() < 0) {       //time is invalid
            servingsString += "Time to prepare: unknown\n";
        } else {
            servingsString += "Time to prepare: " + recipe.getPreparationMinutes() + " minutes\n";
        }
        if (recipe.getCookingMinutes() < 0) {
            servingsString += "Time to cook: unknown\n";
        } else {
            servingsString += "Time to cook: " + recipe.getCookingMinutes() + " minutes\n";
        }
        if (recipe.getReadyInMinutes() < 0) {
            servingsString += "Total time: unknown";
        } else {
            servingsString += "Total time: " + recipe.getReadyInMinutes() + " minutes";
        }
        servingsView.setText(servingsString);

        ingredientsView = (TextView) activity.findViewById(R.id.ingredients);
        ArrayList<String> ingredients = recipe.getIngredients();
        String ingredientsString="";
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientsString+="• " + ingredients.get(i);
            if (i != ingredients.size() - 1) {
                ingredientsString+="\n";
            }
        }
        ingredientsView.setText(ingredientsString);

    }

}
