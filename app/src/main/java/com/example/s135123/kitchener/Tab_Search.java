package com.example.s135123.kitchener;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by s136693 on 5-3-2016.
 */

public class Tab_Search extends android.support.v4.app.Fragment implements View.OnClickListener {
    feature/navigationDrawer
    ArrayList<Recipe> recipes = new ArrayList<>();
    ListView list;
    CompactBaseAdapter adapter;

    SendRequest request;

    //EditText views needed to hide them if an advanced search is necessary
    EditText editTextSearch;
    EditText editTextIncludeIngredients;
    EditText editTextExcludeIngredients;
    EditText editTextAllergens;
    //TextView views needed to hide them if an advanced search is necessary
    TextView textViewIncludeIngredients;
    TextView textViewExcludeIngredients;
    TextView textViewAllergens;

    // Buttons needed to set onClickListener()
    Button buttonSearch;
    Button buttonAdvancedOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_search,container,false);
        String recipeString1 = "{\"summary\":\"African Chicken & Sweet Potatoes could be just the gluten free and dairy free recipe you've been looking for. This recipe serves 6. One portion of this dish contains approximately 26g of protein, 32g of fat, and a total of 556 calories. For $1.36 per serving, this recipe covers 22% of your daily requirements of vitamins and minerals. Only a few people made this recipe, and 2 would say it hit the spot. This recipe is typical of African cuisine. It works well as a main course. From preparation to the plate, this recipe takes roughly 50 minutes. Head to the store and pick up pepper, canned tomatoes, creamy peanut butter, and a few other things to make it today. It is brought to you by Taste of Home. Taking all factors into account, this recipe earns a spoonacular score of 60%, which is solid. If you like this recipe, you might also like recipes such as African Groundnut Chicken and Sweet Potato Stew, African Sweet Potato Soup, and African Sweet Potato and Peanut Soup.\\n\",\"instructions\":[\"Preheat oven to 375°. \",\"Place chicken in a greased 13x9-in. baking dish; sprinkle with salt and pepper. \",\"Bake, uncovered, 30 minutes.\",\"Meanwhile, in a large skillet, heat oil over medium-high heat. \",\"Add sweet potatoes; cook and stir 10-12 minutes or until tender. In a small bowl, mix chutney and peanut butter; stir into sweet potatoes. \",\"Add tomatoes; heat through.\",\"Spoon potato mixture over chicken. \",\"Bake 10-15 minutes longer or until a thermometer inserted in chicken reads 180°.\"],\"cuisine\":\"american\",\"preparationMinutes\":10,\"fat\":32,\"image\":\"https://spoonacular.com/recipeImages/African-Chicken---Sweet-Potatoes-374273.jpg\",\"readyInMinutes\":50,\"cookingMinutes\":40,\"ingredients\":[{\"amount\":2,\"aisle\":\"Meat\",\"name\":\"bone-in chicken thighs\",\"unitShort\":\"lb\",\"originalString\":\"6 bone-in chicken thighs (about 2-1/4 pounds)\",\"unitLong\":\"pounds\"},{\"amount\":10,\"aisle\":\"Canned and Jarred\",\"name\":\"canned tomatoes\",\"unitShort\":\"oz\",\"originalString\":\"1 can (10 ounces) diced tomatoes and green chilies, undrained\",\"unitLong\":\"ounces\"},{\"amount\":2,\"aisle\":\"Oil, Vinegar, Salad Dressing\",\"name\":\"canola oil\",\"unitShort\":\"T\",\"originalString\":\"2 tablespoons canola oil\",\"unitLong\":\"tablespoons\"},{\"amount\":0.25,\"aisle\":\"Nut butters, Jams, and Honey\",\"name\":\"creamy peanut butter\",\"unitShort\":\"c\",\"originalString\":\"1/4 cup creamy peanut butter\",\"unitLong\":\"cups\"},{\"amount\":0.5,\"aisle\":\"Ethnic Foods\",\"name\":\"mango chutney\",\"unitShort\":\"c\",\"originalString\":\"1/2 cup mango chutney\",\"unitLong\":\"cups\"},{\"amount\":0.25,\"aisle\":\"Spices and Seasonings\",\"name\":\"pepper\",\"unitShort\":\"t\",\"originalString\":\"1/4 teaspoon pepper\",\"unitLong\":\"teaspoons\"},{\"amount\":0.5,\"aisle\":\"Spices and Seasonings\",\"name\":\"salt\",\"unitShort\":\"t\",\"originalString\":\"1/2 teaspoon salt\",\"unitLong\":\"teaspoons\"},{\"amount\":4,\"aisle\":\"Produce\",\"name\":\"sweet potatoes\",\"unitShort\":\"c\",\"originalString\":\"2 medium sweet potatoes, peeled and finely chopped (about 4 cups)\",\"unitLong\":\"cups\"}],\"id\":374273,\"badges\":[],\"servings\":6,\"title\":\"African Chicken & Sweet Potatoes\",\"protein\":26,\"calories\":556,\"vegetarian\":false,\"carbs\":43,\"cheap\":false},\n";
        String recipeString2 = "{\"summary\":\"If you want to add more gluten free and dairy free recipes to your recipe box, West African Peanut Chicken might be a recipe you should try. This recipe makes 4 servings with 608 calories, 58g of protein, and 36g of fat each. For $2.99 per serving, this recipe covers 36% of your daily requirements of vitamins and minerals. It works well as an African main course. This recipe from My Recipes has 8 fans. If you have peanut butter, green bell pepper, garlic, and a few other ingredients on hand, you can make it. To use up the salt you could follow this main course with the Salt River Bars as a dessert. From preparation to the plate, this recipe takes approximately 40 minutes. Overall, this recipe earns a tremendous spoonacular score of 85%. If you like this recipe, take a look at these similar recipes: West African Peanut-Chicken Stew, West African Peanut Soup, and West African Peanut Stew.\\n\",\"instructions\":[\"Heat oil over high heat for 3 to 4 minutes in a deep pot or large heavy skillet. \",\"Add chicken pieces, half at a time, and fry on both sides until nicely browned. Once all chicken pieces are cooked, set aside in a covered bowl to keep warm.  \",\"Add onions and garlic in same pot; cook until onions are soft and begin to brown, stirring occasionally. \",\"Add pureed tomatoes and 1 cup water; reduce heat to medium, bring to a simmer, and let simmer for a few minutes.  Return chicken to pot. Stir in peanut butter. \",\"Mixture will look clumpy for a minute, but will mix in nicely once it heats up. \",\"Add cayenne pepper, black pepper, and salt. Simmer over low heat 10 to 15 minutes.  Stir in diced green pepper and tomatoes; simmer for 3 to 4 more minutes.\"],\"cuisine\":\"american\",\"preparationMinutes\":0,\"fat\":36,\"image\":\"https://spoonacular.com/recipeImages/West-African-Peanut-Chicken-239068.jpg\",\"readyInMinutes\":40,\"cookingMinutes\":0,\"ingredients\":[{\"amount\":0.5,\"aisle\":\"Spices and Seasonings\",\"name\":\"black pepper\",\"unitShort\":\"t\",\"originalString\":\"1/2 teaspoon black pepper\",\"unitLong\":\"teaspoons\"},{\"amount\":3,\"aisle\":\"Baking\",\"name\":\"food dye\",\"unitShort\":\"\",\"originalString\":\"3 medium-size onions, minced or pureed in food processor\",\"unitLong\":\"\"},{\"amount\":2,\"aisle\":\"Produce\",\"name\":\"garlic\",\"unitShort\":\"cloves\",\"originalString\":\"2 cloves garlic, minced\",\"unitLong\":\"cloves\"},{\"amount\":1,\"aisle\":\"Produce\",\"name\":\"green bell pepper\",\"unitShort\":\"\",\"originalString\":\"1 green bell pepper, seeded and diced\",\"unitLong\":\"\"},{\"amount\":0.5,\"aisle\":\"Nut butters, Jams, and Honey\",\"name\":\"peanut butter\",\"unitShort\":\"c\",\"originalString\":\"1/2 cup peanut butter\",\"unitLong\":\"cups\"},{\"amount\":0.25,\"aisle\":\"Oil, Vinegar, Salad Dressing\",\"name\":\"peanut oil\",\"unitShort\":\"c\",\"originalString\":\"1/4 cup peanut oil (or any cooking oil)\",\"unitLong\":\"cups\"},{\"amount\":1,\"aisle\":\"Spices and Seasonings\",\"name\":\"salt\",\"unitShort\":\"T\",\"originalString\":\"1 tablespoon salt\",\"unitLong\":\"tablespoon\"},{\"amount\":2,\"aisle\":\"Meat\",\"name\":\"skinless boneless chicken breasts\",\"unitShort\":\"lb\",\"originalString\":\"2 pounds boneless, skinless chicken breasts or thighs, cut into bite-size pieces\",\"unitLong\":\"pounds\"},{\"amount\":2,\"aisle\":\"Produce\",\"name\":\"tomatoes\",\"unitShort\":\"\",\"originalString\":\"2 ripe tomatoes, diced\",\"unitLong\":\"\"},{\"amount\":3,\"aisle\":\"Produce\",\"name\":\"tomatoes\",\"unitShort\":\"c\",\"originalString\":\"3 cups pureed tomatoes (or 2 cups tomato sauce and 1 cup water)\",\"unitLong\":\"cups\"},{\"amount\":1,\"aisle\":\"Beverages\",\"name\":\"water\",\"unitShort\":\"c\",\"originalString\":\"1 cup water\",\"unitLong\":\"cup\"}],\"id\":239068,\"badges\":[],\"servings\":4,\"title\":\"West African Peanut Chicken\",\"protein\":58,\"calories\":608,\"vegetarian\":false,\"carbs\":15,\"cheap\":false},\n";
        String recipeString3 = "{\"summary\":\"African Tilapia might be just the main course you are searching for. This gluten free, dairy free, and pescatarian recipe serves 4 and costs $4.52 per serving. One portion of this dish contains about 46g of protein, 10g of fat, and a total of 289 calories. Only a few people really liked this African dish. This recipe is liked by 1 foodies and cooks. From preparation to the plate, this recipe takes roughly 45 minutes. Head to the store and pick up bell pepper, red pepper flakes, vinegar, and a few other things to make it today. It is brought to you by Food.com. Overall, this recipe earns a tremendous spoonacular score of 81%. If you like this recipe, take a look at these similar recipes: Tilapian in Mushroom Sauce (Tilapian en Salsa de Champiñones), Paris Village Tilapia Filet \\u2013 Located in Las Vegas the Paris makes a special Tilapia Filet, and African Curry.\\n\",\"instructions\":[],\"cuisine\":\"american\",\"preparationMinutes\":35,\"fat\":10,\"image\":\"https://spoonacular.com/recipeImages/african-tilapia-2-88823.jpg\",\"readyInMinutes\":45,\"cookingMinutes\":10,\"ingredients\":[{\"amount\":1,\"aisle\":\"Produce\",\"name\":\"bell pepper\",\"unitShort\":\"\",\"originalString\":\"1 bell pepper, chopped\",\"unitLong\":\"\"},{\"amount\":1,\"aisle\":\"Produce\",\"name\":\"juice of lemon\",\"unitShort\":\"\",\"originalString\":\"1 lemon, juice of\",\"unitLong\":\"\"},{\"amount\":1,\"aisle\":\"Produce\",\"name\":\"onion\",\"unitShort\":\"\",\"originalString\":\"1 small onion, chopped\",\"unitLong\":\"\"},{\"amount\":2,\"aisle\":\"Spices and Seasonings\",\"name\":\"red pepper flakes\",\"unitShort\":\"t\",\"originalString\":\"2 teaspoons cayenne or red pepper flakes\",\"unitLong\":\"teaspoons\"},{\"amount\":1,\"aisle\":\"Spices and Seasonings\",\"name\":\"salt\",\"unitShort\":\"t\",\"originalString\":\"1 teaspoon salt\",\"unitLong\":\"teaspoon\"},{\"amount\":2,\"aisle\":\"Seafood\",\"name\":\"tilapia fillets\",\"unitShort\":\"lb\",\"originalString\":\"2 lbs tilapia fillets\",\"unitLong\":\"pounds\"},{\"amount\":1,\"aisle\":\"Oil, Vinegar, Salad Dressing\",\"name\":\"vegetable oil\",\"unitShort\":\"c\",\"originalString\":\"1 cup vegetable oil\",\"unitLong\":\"cup\"},{\"amount\":1,\"aisle\":\"Oil, Vinegar, Salad Dressing\",\"name\":\"vinegar\",\"unitShort\":\"T\",\"originalString\":\"1 tablespoon vinegar\",\"unitLong\":\"tablespoon\"}],\"id\":88823,\"badges\":[],\"servings\":4,\"title\":\"African Tilapia\",\"protein\":46,\"calories\":289,\"vegetarian\":false,\"carbs\":5,\"cheap\":false},\n";
        String recipeString4 = "{\"summary\":\"You can never have too many main course recipes, so give Boerewors - South African Sausage a try. One portion of this dish contains roughly 21g of protein, 18g of fat, and a total of 254 calories. For $1.32 per serving, this recipe covers 11% of your daily requirements of vitamins and minerals. This recipe serves 12. This recipe is liked by 5 foodies and cooks. From preparation to the plate, this recipe takes around 13 minutes. This recipe is typical of African cuisine. If you have beef, pork ribs, ground nutmeg, and a few other ingredients on hand, you can make it. To use up the ground cloves you could follow this main course with the Libby's Famous Pumpkin Pie as a dessert. It is a good option if you're following a caveman, gluten free, dairy free, and primal diet. It is brought to you by Food.com. Overall, this recipe earns a pretty good spoonacular score of 46%. Similar recipes include South African Pudding, Hélène's South African Casserole, and South African Melktert (Milktart).\\n\",\"instructions\":[],\"cuisine\":\"american\",\"preparationMinutes\":5,\"fat\":18,\"image\":\"https://spoonacular.com/recipeImages/boerewors-south-african-sausage-2-146420.jpg\",\"readyInMinutes\":13,\"cookingMinutes\":8,\"ingredients\":[{\"amount\":1.5,\"aisle\":\"Frozen\",\"name\":\"beef\",\"unitShort\":\"lb\",\"originalString\":\"1 1/2 lbs beef, coarsely ground\",\"unitLong\":\"pounds\"},{\"amount\":0.25,\"aisle\":\"Spices and Seasonings\",\"name\":\"ground cloves\",\"unitShort\":\"t\",\"originalString\":\"1/4 teaspoon ground cloves\",\"unitLong\":\"teaspoons\"},{\"amount\":3,\"aisle\":\"Spices and Seasonings\",\"name\":\"ground coriander\",\"unitShort\":\"t\",\"originalString\":\"3 teaspoons ground coriander\",\"unitLong\":\"teaspoons\"},{\"amount\":0.25,\"aisle\":\"Spices and Seasonings\",\"name\":\"ground nutmeg\",\"unitShort\":\"t\",\"originalString\":\"1/4 teaspoon ground nutmeg\",\"unitLong\":\"teaspoons\"},{\"amount\":0.5,\"aisle\":\"Spices and Seasonings\",\"name\":\"ground pepper\",\"unitShort\":\"t\",\"originalString\":\"1/2 teaspoon fresh ground black pepper\",\"unitLong\":\"teaspoons\"},{\"amount\":1.5,\"aisle\":\"Meat\",\"name\":\"pork ribs\",\"unitShort\":\"lb\",\"originalString\":\"1 1/2 lbs boneless pork ribs, coarsely ground (must be at least 20% fat)\",\"unitLong\":\"pounds\"},{\"amount\":1,\"aisle\":\"Spices and Seasonings\",\"name\":\"salt\",\"unitShort\":\"t\",\"originalString\":\"1 teaspoon salt\",\"unitLong\":\"teaspoon\"},{\"amount\":0.6666666666666666,\"aisle\":\"Oil, Vinegar, Salad Dressing\",\"name\":\"white vinegar\",\"unitShort\":\"c\",\"originalString\":\"2/3 cup white vinegar\",\"unitLong\":\"cups\"}],\"id\":146420,\"badges\":[],\"servings\":12,\"title\":\"Boerewors - South African Sausage\",\"protein\":21,\"calories\":254,\"vegetarian\":false,\"carbs\":0,\"cheap\":false},\n";
        String recipeString5 = "{\"summary\":\"North African Cornish Hens takes around 2 hours from beginning to end. For $5.45 per serving, you get a main course that serves 4. One portion of this dish contains approximately 119g of protein, 110g of fat, and a total of 1532 calories. This recipe from cookstr.com has 1 fans. A mixture of ground ginger, cumin seed, lemons, and a handful of other ingredients are all it takes to make this recipe so flavorful. To use up the salt you could follow this main course with the Salt River Bars as a dessert. This recipe is typical of African cuisine. It is a good option if you're following a caveman, gluten free, dairy free, and primal diet. Overall, this recipe earns an amazing spoonacular score of 82%. If you like this recipe, you might also like recipes such as North African Chicken, North African Meatballs, and North African Spiced Carrots.\\n\",\"instructions\":[],\"cuisine\":\"american\",\"preparationMinutes\":0,\"fat\":110,\"image\":\"https://spoonacular.com/recipeImages/north-african-cornish-hens-2-14570.jpg\",\"readyInMinutes\":120,\"cookingMinutes\":0,\"ingredients\":[{\"amount\":6,\"aisle\":\"Meat\",\"name\":\"cornish game hens\",\"unitShort\":\"lb\",\"originalString\":\"4 (1½-lb) Cornish game hens\",\"unitLong\":\"pounds\"},{\"amount\":2,\"aisle\":\"Spices and Seasonings\",\"name\":\"cumin seed\",\"unitShort\":\"t\",\"originalString\":\"2 tsps cumin seed\",\"unitLong\":\"teaspoons\"},{\"amount\":5,\"aisle\":\"Produce\",\"name\":\"garlic cloves\",\"unitShort\":\"\",\"originalString\":\"5 garlic cloves, crushed through a garlic press\",\"unitLong\":\"\"},{\"amount\":1,\"aisle\":\"Spices and Seasonings\",\"name\":\"ground ginger\",\"unitShort\":\"t\",\"originalString\":\"1 tsp ground ginger\",\"unitLong\":\"teaspoon\"},{\"amount\":2,\"aisle\":\"Produce\",\"name\":\"lemons\",\"unitShort\":\"\",\"originalString\":\"2 lemons , halved\",\"unitLong\":\"\"},{\"amount\":0.25,\"aisle\":\"Oil, Vinegar, Salad Dressing\",\"name\":\"olive oil\",\"unitShort\":\"c\",\"originalString\":\"¼ cup olive oil\",\"unitLong\":\"cups\"},{\"amount\":1,\"aisle\":\"Produce\",\"name\":\"orange\",\"unitShort\":\"\",\"originalString\":\"1 orange , quartered\",\"unitLong\":\"\"},{\"amount\":2,\"aisle\":\"Spices and Seasonings\",\"name\":\"paprika\",\"unitShort\":\"Tbsps\",\"originalString\":\"2 Tbsps paprika , preferably hot Hungarian\",\"unitLong\":\"Tbsps\"},{\"amount\":1.5,\"aisle\":\"Spices and Seasonings\",\"name\":\"salt\",\"unitShort\":\"t\",\"originalString\":\"1½ tsps salt , preferably coarse (kosher)\",\"unitLong\":\"teaspoons\"}],\"id\":14570,\"badges\":[],\"servings\":4,\"title\":\"North African Cornish Hens\",\"protein\":119,\"calories\":1532,\"vegetarian\":false,\"carbs\":13,\"cheap\":false},\n";
        String recipeString6 = "{\"summary\":\"If you have about 30 minutes to spend in the kitchen, Harissa Shakshuka (North African Eggs in Purgatory) might be a great pescatarian recipe to try. This recipe serves 2 and costs $1.83 per serving. One serving contains 286 calories, 15g of protein, and 18g of fat. It works well as a main course. 45 people were glad they tried this recipe. It is a rather cheap recipe for fans of African food. If you have eggs, yellow onion, crusty bread, and a few other ingredients on hand, you can make it. To use up the spice mix you could follow this main course with the Spice Bars as a dessert. It is brought to you by The Wanderlust Kitchen. All things considered, we decided this recipe deserves a spoonacular score of 65%. This score is good. Similar recipes include Eggs in Purgatory: Shakshuka, Dinner Tonight: Eggs in Purgatory (Eggs Baked in Tomato Sauce), and North African Meatballs.\\n\",\"instructions\":[\"Preheat the oven to 375 degrees Fahrenheit.\",\"Heat the olive oil in a oven-safe skillet set over medium-low heat. Once the oil is shimmering, add the onions and saut until soft, about 5-7 minutes. \",\"Add the garlic and harissa; saut until fragrant, about 60 seconds.\",\"Pour in the tomatoes and season the mixture with the salt and pepper. Simmer the mixture, using a wooden spoon to help break down the tomatoes, until thickened, about 10 minutes.Arrange the goat cheese slices around the pan and turn off the heat. Crack the eggs into a small dish, then add them to the pan one at a time. Season each egg with additional salt and pepper, then bake in the preheated until the whites are set, about 7-10 minutes.\"],\"cuisine\":\"american\",\"preparationMinutes\":5,\"fat\":18,\"image\":\"https://spoonacular.com/recipeImages/harissa-shakshuka-(north-african-eggs-in-purgatory)-715038.jpg\",\"readyInMinutes\":30,\"cookingMinutes\":25,\"ingredients\":[{\"amount\":0.25,\"aisle\":\"Spices and Seasonings\",\"name\":\"black pepper\",\"unitShort\":\"t\",\"originalString\":\"¼ teaspoon freshly ground black pepper\",\"unitLong\":\"teaspoons\"},{\"amount\":14,\"aisle\":\"Canned and Jarred\",\"name\":\"canned tomatoes\",\"unitShort\":\"oz\",\"originalString\":\"1 (14-ounce) can no-salt-added diced tomatoes, with juices\",\"unitLong\":\"ounces\"},{\"amount\":2,\"aisle\":\"Bakery/Bread\",\"name\":\"crusty bread\",\"unitShort\":\"servings\",\"originalString\":\"Crusty bread for serving\",\"unitLong\":\"servings\"},{\"amount\":2,\"aisle\":\"Milk, Eggs, Other Dairy\",\"name\":\"eggs\",\"unitShort\":\"\",\"originalString\":\"2 eggs\",\"unitLong\":\"\"},{\"amount\":1,\"aisle\":\"Oil, Vinegar, Salad Dressing\",\"name\":\"extra virgin olive oil\",\"unitShort\":\"T\",\"originalString\":\"1 tablespoon extra virgin olive oil\",\"unitLong\":\"tablespoon\"},{\"amount\":2,\"aisle\":\"Produce\",\"name\":\"garlic\",\"unitShort\":\"t\",\"originalString\":\"2 teaspoons minced fresh garlic\",\"unitLong\":\"teaspoons\"},{\"amount\":2,\"aisle\":\"Cheese\",\"name\":\"goat cheese\",\"unitShort\":\"oz\",\"originalString\":\"2 ounces goat cheese, thinly sliced into coins\",\"unitLong\":\"ounces\"},{\"amount\":0.5,\"aisle\":\"Spices and Seasonings\",\"name\":\"salt\",\"unitShort\":\"t\",\"originalString\":\"½ teaspoon salt\",\"unitLong\":\"teaspoons\"},{\"amount\":1,\"aisle\":\"Spices and Seasonings\",\"name\":\"spice mix\",\"unitShort\":\"t\",\"originalString\":\"1 teaspoon harissa spice mix\",\"unitLong\":\"teaspoon\"},{\"amount\":0.25,\"aisle\":\"Produce\",\"name\":\"yellow onion\",\"unitShort\":\"c\",\"originalString\":\"¼ cup chopped yellow onion\",\"unitLong\":\"cups\"}],\"id\":715038,\"badges\":[],\"servings\":2,\"title\":\"Harissa Shakshuka (North African Eggs in Purgatory)\",\"protein\":15,\"calories\":286,\"vegetarian\":false,\"carbs\":20,\"cheap\":false},\n";

        /*recipes.add(new Recipe(recipeString1));
        recipes.add(new Recipe(recipeString2));
        recipes.add(new Recipe(recipeString3));
        recipes.add(new Recipe(recipeString4));
        recipes.add(new Recipe(recipeString5));
        recipes.add(new Recipe(recipeString6));*/
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                String result = sendGetRequest("http://appdev-gr1.win.tue.nl:8008/api/authenticate/test/test123");
                System.out.println(result);
            }
        });
        thread.start();
        list = (ListView) v.findViewById(R.id.listView_reccomendations);
        adapter = new CompactBaseAdapter(getActivity(), recipes);
        list.setAdapter(adapter);

        // EditText views found and hidden as default
        editTextSearch = (EditText) v.findViewById(R.id.editTextSearch);
        editTextIncludeIngredients = (EditText) v.findViewById(R.id.editTextIncludeIngredients);
        editTextExcludeIngredients = (EditText) v.findViewById(R.id.editTextExcludeIngredients);
        editTextAllergens = (EditText) v.findViewById(R.id.editTextAllergens);
        editTextIncludeIngredients.setVisibility(View.GONE);
        editTextExcludeIngredients.setVisibility(View.GONE);
        editTextAllergens.setVisibility(View.GONE);

        // TextView views found and hidden as default
        textViewIncludeIngredients = (TextView) v.findViewById(R.id.textViewIncludeIngredients);
        textViewExcludeIngredients = (TextView) v.findViewById(R.id.textViewExcludeIngredients);
        textViewAllergens = (TextView) v.findViewById(R.id.textViewAllergens);
        textViewIncludeIngredients.setVisibility(View.GONE);
        textViewExcludeIngredients.setVisibility(View.GONE);
        textViewAllergens.setVisibility(View.GONE);

        buttonSearch = (Button) v.findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
        buttonAdvancedOptions = (Button) v.findViewById(R.id.buttonToggleAdvancedOptions);
        buttonAdvancedOptions.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonSearch:

                search(editTextSearch.getText().toString(),
                        editTextIncludeIngredients.getText().toString(),
                        editTextExcludeIngredients.getText().toString(),
                        editTextAllergens.getText().toString());
                break;
            case R.id.buttonToggleAdvancedOptions:
                toggleAdvancedOptions();
                break;
            default:
                break;
        }

    }

    // TODO: Implement search
    // Sends a search query to the server and returns an ArrayList to display in the ListView results
    public void search(String query, String includeIngredients, String excludeIngredients, String allergens) {

        SendRequest request = new SendRequest();

//        results.setAdapter(new searchAdapter(request.getTitles(),
//                request.getDescriptions(),
//                request.getTimes(),
//                request.getImages(),
//                request.getRatings()); // TODO: Replace with correct baseAdapter and getters

    }

    // Toggles the advanced options views (
    public void toggleAdvancedOptions() {
        if(editTextIncludeIngredients.getVisibility() == View.VISIBLE) {
            editTextIncludeIngredients.setVisibility(View.GONE);
            editTextExcludeIngredients.setVisibility(View.GONE);
            editTextAllergens.setVisibility(View.GONE);

            textViewIncludeIngredients.setVisibility(View.GONE);
            textViewExcludeIngredients.setVisibility(View.GONE);
            textViewAllergens.setVisibility(View.GONE);
        } else {
            textViewIncludeIngredients.setVisibility(View.VISIBLE);
            textViewExcludeIngredients.setVisibility(View.VISIBLE);
            textViewAllergens.setVisibility(View.VISIBLE);

            editTextIncludeIngredients.setVisibility(View.VISIBLE);
            editTextExcludeIngredients.setVisibility(View.VISIBLE);
            editTextAllergens.setVisibility(View.VISIBLE);
        }
    }

}