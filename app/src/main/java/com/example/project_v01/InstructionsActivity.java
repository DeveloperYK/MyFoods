package com.example.project_v01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.project_v01.interfaces.AsyncResponseRecipeInfo;
import com.example.project_v01.recipe.Ingredient;
import com.example.project_v01.recipe.Meal;
import com.example.project_v01.ui.IngredientsArrayAdapter;
import com.example.project_v01.ui.InstructionsArrayAdapter;

import java.util.ArrayList;

public class InstructionsActivity extends AppCompatActivity {

    private ArrayList<String> instructionsList;

    private ListView InstructionsListView;
    private Meal meal;

    private String currentRecipeId;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        instructionsList = new ArrayList<>();

        context = getApplicationContext();

        InstructionsListView = findViewById(R.id.instructions_listView);

        currentRecipeId = getIntent().getStringExtra("position");

        meal = new Meal(currentRecipeId, this);


        meal.getInstructions(new AsyncResponseRecipeInfo() {
            @Override
            public void getIngredientList(ArrayList<Ingredient> questionArrayList) {

            }

            @Override
            public void getInstructions(ArrayList<String> Instructions) {



                InstructionsArrayAdapter adapter = new InstructionsArrayAdapter(context,
                        R.layout.ingredients_layout, Instructions);

                InstructionsListView.setAdapter(adapter);


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
}