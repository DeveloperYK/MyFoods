package com.example.project_v01.ChatRoom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_v01.InstructionsActivity;
import com.example.project_v01.R;
import com.example.project_v01.RecipeDetailsActivity;
import com.example.project_v01.interfaces.AsyncResponseRecipeInfo;
import com.example.project_v01.recipe.Ingredient;
import com.example.project_v01.recipe.Meal;
import com.example.project_v01.ui.IngredientsArrayAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.ClosedFileSystemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatRoomRecipesActivity extends AppCompatActivity {

    private Meal meal;

    private ImageView mealImage;


    private TextView RecipeName;
    private TextView RecipeServings;
    private TextView TimeToMake;

    private ListView ingrediantsListView;
    ArrayList<Ingredient> IngredientsList;

    private Context context;

    private Button instructionsButton;

    private ListView InstructionsListView;

    private String mealId;

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_recipes);

        mealImage = findViewById(R.id.mealImageView);

        RecipeName = findViewById(R.id.RecipeTitleTextView);
        RecipeServings = findViewById(R.id.RecipeServingsTextView);
        TimeToMake = findViewById(R.id.TimeToMakeTextView);

        ingrediantsListView = findViewById(R.id.ingredientListView);

        context = getApplicationContext();

        instructionsButton = findViewById(R.id.instructions_button);

        InstructionsListView = findViewById(R.id.instructions_listView);

        requestQueue = Volley.newRequestQueue(this);

        String mealName = getIntent().getStringExtra("meal");

        mealName = mealName.replace(" ", "%20");


        final JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/autocomplete?query="+mealName,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Log.d("chat_test", "onResponse: " + response);

                try {
                    JSONObject temp= response.getJSONObject(0);
                    mealId = temp.getString("id");
                    Log.d("chatbot_test", "onResponse: "+ mealId);


                    meal = new Meal(mealId, ChatRoomRecipesActivity.this);

                    setMealTitleTextView(meal);

                    setServingsTextView(meal);

                    setTimeToMakeTextView(meal);

                    setIngredientsList(meal);

                    setImage(meal);

                    instructionsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ChatRoomRecipesActivity.this, InstructionsActivity.class);
                            intent.putExtra("position", mealId);
                            startActivity(intent);
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("chatbot_test", "onResponse: ERROR" );
                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", "onErrorResponse: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {       // headers
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");   // API version
                params.put("x-rapidapi-key", "AUTHENTICATION KEY HERE"); // authentication key
                return params;
            }

        };

        requestQueue.add(jsonObjectRequest);



    }

    private void setImage(Meal meal) {
        this.meal = meal;

        meal.getImage(new AsyncResponseRecipeInfo() {
            @Override
            public void getIngredientList(ArrayList<Ingredient> questionArrayList) {

            }

            @Override
            public void getInstructions(ArrayList<String> Instructions) {

            }

            @Override
            public void getTitle(String title) {

            }

            @Override
            public void getImage(String image) {
                Picasso.get().load(image).into(mealImage);

            }

            @Override
            public void getTimeToMake(String timeToMake) {

            }

            @Override
            public void getRecipeServings(String recipeServings) {

            }
        });

    }

    private void setIngredientsList(Meal meal) {
        meal.getIngredients(new AsyncResponseRecipeInfo() {
            @Override
            public void getIngredientList(ArrayList<Ingredient> questionArrayList) {


                IngredientsList = questionArrayList;


                IngredientsArrayAdapter adapter = new IngredientsArrayAdapter(context,
                        R.layout.ingredients_layout, questionArrayList);

                ingrediantsListView.setAdapter(adapter);



            }

            @Override
            public void getInstructions(ArrayList<String> Instructions) {
            }

            @Override
            public void getTitle(String title) {

            }

            @Override
            public void getImage(String image) {

            }

            @Override
            public void getTimeToMake(String timeToMake) {

            }

            @Override
            public void getRecipeServings(String recipeServings) {

            }
        });

    }

    private void setTimeToMakeTextView(Meal meal) {
        meal.getTimeToMake(new AsyncResponseRecipeInfo() {
            @Override
            public void getIngredientList(ArrayList<Ingredient> questionArrayList) {

            }

            @Override
            public void getInstructions(ArrayList<String> Instructions) {

            }

            @Override
            public void getTitle(String title) {
            }

            @Override
            public void getImage(String image) {

            }

            @Override
            public void getTimeToMake(String timeToMake) {

                TimeToMake.setText(timeToMake + " Mins");

            }

            @Override
            public void getRecipeServings(String recipeServings) {

            }
        });

    }

    private void setServingsTextView(Meal meal) {
        meal.getRecipeServings(new AsyncResponseRecipeInfo() {
            @Override
            public void getIngredientList(ArrayList<Ingredient> questionArrayList) {

            }

            @Override
            public void getInstructions(ArrayList<String> Instructions) {

            }


            @Override
            public void getTitle(String title) {

            }

            @Override
            public void getImage(String image) {

            }

            @Override
            public void getTimeToMake(String timeToMake) {

            }

            @Override
            public void getRecipeServings(String recipeServings) {
                RecipeServings.setText("Serves: "+ recipeServings);
            }
        });


    }

    private void setMealTitleTextView(Meal meal) {
        meal.getRecipeName(new AsyncResponseRecipeInfo() {
            @Override
            public void getIngredientList(ArrayList<Ingredient> questionArrayList) {

            }

            @Override
            public void getInstructions(ArrayList<String> Instructions) {

            }

            @Override
            public void getTitle(String title) {
                RecipeName.setText(title);

            }

            @Override
            public void getImage(String image) {

            }

            @Override
            public void getTimeToMake(String recipeName) {

            }

            @Override
            public void getRecipeServings(String recipeServings) {

            }
        });


    }
}