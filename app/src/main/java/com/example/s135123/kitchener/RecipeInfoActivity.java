package com.example.s135123.kitchener;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class RecipeInfoActivity extends AppCompatActivity {
    Recipe recipe;
    ImageView imageView;
    TextView summaryView;
    TextView titleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Intent i = getIntent();
        recipe = (Recipe) getIntent().getSerializableExtra("Recipe");
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        imageView = (ImageView) findViewById(R.id.imageView);
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        imageLoader.displayImage("https://spoonacular.com/recipeImages/African-Beef-Curry-384740.jpg", imageView, new SimpleImageLoadingListener(){
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
        //summaryView.setText(recipe.getSummary());
        summaryView.setText("Forget going out to eat or ordering takeout every time you crave African food. Try making African Beef Curry at home. This recipe serves 4 and costs $1.76 per serving. This main course has 464 calories, 36g of protein, and 10g of fat per serving. Only a few people made this recipe, and 1 would say it hit the spot. It is a good option if you're following a gluten free and dairy free diet. This recipe from Taste of Home requires green bell pepper, canned tomatoes, salt, and curry powder. To use up the raisins you could follow this main course with the Chocolate Brownies With Raisins as a dessert. From preparation to the plate, this recipe takes roughly 1 hour and 45 minutes. With a spoonacular score of 85%, this dish is awesome. If you like this recipe, take a look at these similar recipes: South African Beef Curry, African Curry, and African Veg Curry.");

        titleView = (TextView) findViewById(R.id.title);
        titleView.setText("Title");
    }

}
