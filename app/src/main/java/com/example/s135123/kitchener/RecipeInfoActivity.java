package com.example.s135123.kitchener;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeInfoActivity extends AppCompatActivity {
    Recipe recipe;
    ImageView imageView;
    TextView summaryView;
    TextView instructionsView;
    TextView titleView;
    TextView nutritionView;
    TextView servingsView;
    TextView ingredientsView;
    TextView cuisineView;
    User user = User.getInstance();

    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        if(getResources().getBoolean(R.bool.isPhone)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this);

        recipe = (Recipe) getIntent().getSerializableExtra("Recipe");
        new RecipeInfo(this).updateContents(recipe);
        /*ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        if (!imageLoader.isInited()) {
            System.out.println("inited image laoder");
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        }
        imageView = (ImageView) findViewById(R.id.imageView);
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        imageLoader.displayImage(recipe.getImage(), imageView, new SimpleImageLoadingListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                int width = loadedImage.getWidth();
                layoutParams.width = width;
                int height = loadedImage.getHeight();
                layoutParams.height = metrics.widthPixels * height / width;
                imageView.setLayoutParams(layoutParams);
            }
        });
        summaryView = (TextView) findViewById(R.id.summary);
        summaryView.setText(recipe.getSummary());

        instructionsView = (TextView) findViewById(R.id.instructions);
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
        titleView = (TextView) findViewById(R.id.title);
        //titleView.setText("Title");
        titleView.setText(recipe.getTitle());
        cuisineView = (TextView) findViewById(R.id.cuisine_textview);
        if (recipe.getCuisine().matches("[aeiou].*$")) {
            cuisineView.setText("An " + recipe.getCuisine() + " recipe");
        } else {
            cuisineView.setText("A " + recipe.getCuisine() + " recipe");
        }
        nutritionView = (TextView) findViewById(R.id.nutrition);
        nutritionView.setText("Calories: " + recipe.getCalories() + "\nFat: " + recipe.getFat() + "g\nProtein: " + recipe.getProtein() + "g\nCarbs: " + recipe.getCarbs() + "g");
        //nutritionView.setText("Calories: 55g\nFat: 5g\nProtein: 5g\nCarbs: 5g");
        servingsView = (TextView) findViewById((R.id.servingsAndMinutes));
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

        ingredientsView = (TextView) findViewById(R.id.ingredients);
        ArrayList<String> ingredients = recipe.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientsView.append("• " + ingredients.get(i));
            if (i != ingredients.size() - 1) {
                ingredientsView.append("\n");
            }
        }*/

        //servingsView.setText("5 servings\n5 minutes to prepare\n5 minutes to cook\nTotal time: 5");
        //ArrayList<String> instructions =recipe.getInstructions();

    }// Method to check if there is a network available

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onPause(){
        super.onPause();
        if (user.getShakeEnabled()) {
            sensorManager.unregisterListener(shakeDetector);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if (user.getShakeEnabled()) {
            sensorManager.registerListener(shakeDetector,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

}
