package com.example.s135123.kitchener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by s130604 on 29-3-2016.
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

    public RecipeInfo(Activity activity) {
        this.activity = activity;
    }
    public void updateContents(Recipe recipe){
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        if (!imageLoader.isInited()) {
            System.out.println("inited image laoder");
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
        //summaryView.setText("Forget going out to eat or ordering takeout every time you crave African food. Try making African Beef Curry at home. This recipe serves 4 and costs $1.76 per serving. This main course has 464 calories, 36g of protein, and 10g of fat per serving. Only a few people made this recipe, and 1 would say it hit the spot. It is a good option if you're following a gluten free and dairy free diet. This recipe from Taste of Home requires green bell pepper, canned tomatoes, salt, and curry powder. To use up the raisins you could follow this main course with the Chocolate Brownies With Raisins as a dessert. From preparation to the plate, this recipe takes roughly 1 hour and 45 minutes. With a spoonacular score of 85%, this dish is awesome. If you like this recipe, take a look at these similar recipes: South African Beef Curry, African Curry, and African Veg Curry.");

        instructionsView = (TextView) activity.findViewById(R.id.instructions);
        instructionsView.setText("");
        ArrayList<String> instructions = recipe.getInstructions();
        /*ArrayList<String> instructions = new ArrayList<>();
        instructions.add("first instruction");
        instructions.add("second instruction");*/
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
        //titleView.setText("Title");
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
        if (recipe.getPreparationMinutes() < 0) {
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
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientsView.append("• " + ingredients.get(i));
            if (i != ingredients.size() - 1) {
                ingredientsView.append("\n");
            }
        }

        //servingsView.setText("5 servings\n5 minutes to prepare\n5 minutes to cook\nTotal time: 5");
        //ArrayList<String> instructions =recipe.getInstructions();
    }

}
