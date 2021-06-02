package com.example.project_v01.interfaces;

import com.example.project_v01.recipe.Ingredient;

import java.util.ArrayList;

public interface AsyncResponseRecipeInfo {

    void getIngredientList(ArrayList<Ingredient> questionArrayList);

    void getInstructions(ArrayList<String> Instructions);

    void getTitle(String title);

    void getImage(String image);

    void getTimeToMake(String timeToMake);

    void getRecipeServings(String recipeServings);
}
