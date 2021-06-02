package com.example.project_v01.recipe;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_v01.interfaces.AsyncResponseRecipeInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Meal {
    private String mealId;
    ArrayList<Ingredient> IngredientArrayList = new ArrayList();
    ArrayList<String> Instructions = new ArrayList<>();
    private Context context;
    private RequestQueue requestQueue;
    String image;
     String RecipeName;
     String TimeToMake;
     String recipeServings;


    public Meal(String mealId, Context context) {
        this.mealId = mealId;
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);

    }

    public String getTimeToMake(final AsyncResponseRecipeInfo callBack) {

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/"+mealId+"/information",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    TimeToMake = response.getString("readyInMinutes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(null != callBack) {
                    callBack.getTimeToMake(TimeToMake);
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


        return TimeToMake;
    }

    public String getRecipeServings(final AsyncResponseRecipeInfo callBack) {

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/"+mealId+"/information",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    recipeServings = response.getString("servings");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(null != callBack) {
                    callBack.getRecipeServings(recipeServings);
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
                params.put("x-rapidapi-key", "d4f04f9e82msh28a66ac9f79c639p125ae1jsnf0f95c1c6c42"); // authentication key
                return params;
            }

        };

        requestQueue.add(jsonObjectRequest);


        return recipeServings;
    }

    public String getRecipeName(final AsyncResponseRecipeInfo callBack) {


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/"+mealId+"/information",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    RecipeName = response.getString("title");


                    Log.d("api", "onResponse: " + RecipeName);


                    } catch (JSONException ex) {
                    ex.printStackTrace();
                }



                if(null != callBack) {
                    callBack.getTitle(RecipeName);
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
                params.put("x-rapidapi-key", "d4f04f9e82msh28a66ac9f79c639p125ae1jsnf0f95c1c6c42"); // authentication key
                return params;
            }

        };

        requestQueue.add(jsonObjectRequest);


        return RecipeName;
    }

    public ArrayList<Ingredient> getIngredients(final AsyncResponseRecipeInfo callBack) {


        final JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/"+mealId+"/ingredientWidget.json",
                null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray ingredientsList = response.getJSONArray("ingredients");   // get the list of all ingredients and thier info



                    for(int i = 0; i<ingredientsList.length(); i++) { // iterate through ingredients and retrieve ingredient name,amount and measurement
                        JSONObject ingredient = (JSONObject) ingredientsList.get(i);

//                        Log.d("Test", "onResponse: " + ingredient);

                        String name = ingredient.getString("name");

                        JSONObject amount = ingredient.getJSONObject("amount");

//                        Log.d("Test1", "onResponse: " + amount);

                        JSONObject amountMetric = amount.getJSONObject("metric");

//                        Log.d("Test2", "onResponse: " + amountMetric);

                        double value = amountMetric.getDouble("value");

                        String measurement = amountMetric.getString("unit");

//                        Log.d("finalTest", "onResponse: " + name + ", " + value + ", " + measurement);

                        Ingredient ingredientObject = new Ingredient(name, value, measurement); // store the found values as a ingredient object

                        IngredientArrayList.add(ingredientObject); // add each ingredient object to list so it can be returned to main




                    }             } catch (JSONException e) {
                    e.printStackTrace();
                }


                if(null != callBack) {
                    Log.d("Interface", "onResponse: " + callBack);
                    callBack.getIngredientList(
                            IngredientArrayList);

                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", "onErrorResponse: "+ error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {       // headers
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");   // API version
                params.put("x-rapidapi-key", "d4f04f9e82msh28a66ac9f79c639p125ae1jsnf0f95c1c6c42"); // authentication key
                return params;
            }

        };

        requestQueue.add(jsonObjectRequest);



        return IngredientArrayList;
    }


    public ArrayList<String> getInstructions(final AsyncResponseRecipeInfo callBack) {


        final JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET,
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/"+mealId+"/analyzedInstructions",
                null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) { // get instructions text from api call and return it

                try {
                    for (int i = 0; i<response.length(); i++) {
                        JSONObject selection = response.getJSONObject(i);
                        JSONArray stepsCollection = selection.getJSONArray("steps");

                        for (int j = 0; i < stepsCollection.length(); i++) {
                            JSONObject stepIndex = stepsCollection.getJSONObject(i);
                            String stepInfo = stepIndex.getString("step");
                            Instructions.add(stepInfo);

                        }



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if(null != callBack) {
                    Log.d("Interface", "onResponse: " + callBack);
                    callBack.getInstructions(Instructions);

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", "onErrorResponse: "+ error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {       // headers
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");   // API version
                params.put("x-rapidapi-key", "d4f04f9e82msh28a66ac9f79c639p125ae1jsnf0f95c1c6c42"); // authentication key
                return params;
            }



        };

        requestQueue.add(jsonArrayRequest);

        return Instructions;
    }


    public String getImage(final AsyncResponseRecipeInfo callBack) {

        final JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/"+mealId+"/information",
                null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    image = response.getString("image");

                    Log.d("Test", "onResponse: " + image);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


                if(null != callBack) {
                    Log.d("Interface", "onResponse: " + callBack);
                    callBack.getImage(image);

                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", "onErrorResponse: "+ error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {       // headers
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");   // API version
                params.put("x-rapidapi-key", "d4f04f9e82msh28a66ac9f79c639p125ae1jsnf0f95c1c6c42"); // authentication key
                return params;
            }

        };

        requestQueue.add(jsonObjectRequest);



        return image;
    }


}
