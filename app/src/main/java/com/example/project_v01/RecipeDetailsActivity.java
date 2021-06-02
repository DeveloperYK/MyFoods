package com.example.project_v01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.project_v01.interfaces.AsyncResponseRecipeInfo;
import com.example.project_v01.recipe.Ingredient;
import com.example.project_v01.recipe.Meal;
import com.example.project_v01.ui.IngredientsArrayAdapter;

import java.util.ArrayList;
import java.util.Objects;

import com.example.project_v01.ui.InstructionsArrayAdapter;
import com.squareup.picasso.Picasso;


public class RecipeDetailsActivity extends AppCompatActivity {

    private Meal meal;

    ArrayList<String> recipeIds;
    private int adapterPositon;
    private String currentRecipeId;

    private ImageView mealImage;


    private TextView RecipeName;
    private TextView RecipeServings;
    private TextView TimeToMake;

    private ListView ingrediantsListView;
    ArrayList<Ingredient> IngredientsList;

    private Context context;

    private Button instructionsButton;

    private ListView InstructionsListView;

    private Button swapMealsButton;

    private Button goBack;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        

        mealImage = findViewById(R.id.mealImageView);

        RecipeName = findViewById(R.id.RecipeTitleTextView);
        RecipeServings = findViewById(R.id.RecipeServingsTextView);
        TimeToMake = findViewById(R.id.TimeToMakeTextView);

        ingrediantsListView = findViewById(R.id.ingredientListView);

        context = getApplicationContext();

        instructionsButton = findViewById(R.id.instructions_button);

        InstructionsListView = findViewById(R.id.instructions_listView);

        swapMealsButton = findViewById(R.id.swap_meal_button);

        goBack = findViewById(R.id.back_to_plan_2);


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDetailsActivity.this, ShowFoodPlanActivity.class);
                intent.putExtra("position", currentRecipeId);
                startActivity(intent);
            }
        });










        recipeIds = new ArrayList<>();

        IngredientsList = new ArrayList<>();

        adapterPositon = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("RecyclerPosition")));

        recipeIds = getIntent().getStringArrayListExtra("RecipeIDs");

        currentRecipeId = recipeIds.get(adapterPositon);

        Log.d("idCheck", "onCreate: " + currentRecipeId);



        meal = new Meal(currentRecipeId, RecipeDetailsActivity.this);

        setMealTitleTextView(meal);

        setServingsTextView(meal);

        setTimeToMakeTextView(meal);

        setIngredientsList(meal);

        setImage(meal);

        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDetailsActivity.this, InstructionsActivity.class);
                intent.putExtra("position", currentRecipeId);
                startActivity(intent);
            }
        });

        swapMealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeDetailsActivity.this, SwapMealActivity.class);
                intent.putExtra("position", String.valueOf(adapterPositon));
                startActivity(intent);
            }
        });











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

    }   // set the servings amount  Textview

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

    }   // set the time to make recipe Textview

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
    }   // set the meal title TextView


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(RecipeDetailsActivity.this, ShowFoodPlanActivity.class);
        startActivity(intent);

    }
}