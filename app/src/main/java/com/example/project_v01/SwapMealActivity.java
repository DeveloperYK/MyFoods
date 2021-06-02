package com.example.project_v01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_v01.ui.FoodPlanRecyclerAdapter;
import com.example.project_v01.util.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwapMealActivity extends AppCompatActivity {
    private Context context;
    private RequestQueue requestQueue;
    private ArrayList<String> potentialMealsIDs;

    private Button potential_meal_1;
    private Button potential_meal_2;
    private Button potential_meal_3;
    private Button potential_meal_4;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Food Plans");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_meal);
        context = getApplicationContext();
        requestQueue = Volley.newRequestQueue(context);

        potentialMealsIDs = new ArrayList<>();

        potential_meal_1 = findViewById(R.id.potential_meal_1);
        potential_meal_2 = findViewById(R.id.potential_meal_2);
        potential_meal_3 = findViewById(R.id.potential_meal_3);
        potential_meal_4 = findViewById(R.id.potential_meal_4);



        String diet = CreatingFoodPlanActivity.get_intent();
        Log.d("SWAP", "onCreate: " + diet);


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/random?number=4&tags=vegetarian%2Cbreakfast",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.d("api", "onResponse: " + response);

                try {
                    JSONArray meals = response.getJSONArray("recipes");
                    Log.d("api", "onResponse: " + meals);

                    for(int i = 0 ; i < 4; i++) {

                        JSONObject meal = (JSONObject) meals.get(i);
                        String id = meal.getString("id");
                        Log.d("api", "onResponse: " + id);

                        potentialMealsIDs.add(id);

                    }


                    for (int i = 0; i < potentialMealsIDs.size(); i++) {

                        Log.d("finish", "onCreate: " + potentialMealsIDs.get(i));
                    }

                    displayMeals(potentialMealsIDs);


                    potential_meal_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("Checks", "onClick: number 1");
                            String swapID = getIntent().getStringExtra("position");

                            Log.d("checker", "onClick: " + swapID);
                            changeMeal(swapID, potentialMealsIDs.get(0));



                        }
                    });

                    potential_meal_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("Checks", "onClick: number 2");
                            String swapID = getIntent().getStringExtra("position");

                            Log.d("checker", "onClick: " + swapID);
                            changeMeal(swapID, potentialMealsIDs.get(1));
                        }
                    });

                    potential_meal_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("Checks", "onClick: number 3");
                            String swapID = getIntent().getStringExtra("position");

                            Log.d("checker", "onClick: " + swapID);
                            changeMeal(swapID, potentialMealsIDs.get(2));
                        }
                    });

                    potential_meal_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("Checks", "onClick: number 4");

                            String swapID = getIntent().getStringExtra("position");

                            Log.d("checker", "onClick: " + swapID);
                            changeMeal(swapID, potentialMealsIDs.get(3));
                        }
                    });


                } catch (JSONException e) {
                    Log.d("api", "onResponse: ERROR");
                    e.printStackTrace();
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

    private void changeMeal(final String swapID, final String newMealID) {



        collectionReference.whereEqualTo("UserId", User.getInstance()
                .getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot Id : queryDocumentSnapshots) {
                                    String doc = String.valueOf((queryDocumentSnapshots.getDocuments()));
                                    String[] docArray = doc.split(",");


                                    doc = docArray[0];
                                    doc = doc.substring(doc.indexOf('/')+1);
                                    Log.d("Checker", "onSuccess: " + doc);

                                    collectionReference.document(doc).update("Meal " + swapID, newMealID).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Checker", "onSuccess: Document UPDATED!");
                                            Intent intent = new Intent(SwapMealActivity.this, ShowFoodPlanActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            }


                        }else {
                            Log.d("ExtractId", "onSuccess: Snapshots empty");
                        }
                    }
                });






    }


    private void displayMeals(ArrayList<String> potentialMealsIDs) {

        for (int i = 1; i < 5; i++) {
            String mealId = potentialMealsIDs.get(i-1);

            final int finalI = i;
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" + mealId + "/information",
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        String RecipeName = response.getString("title");

                        if (finalI == 1) {
                            potential_meal_1.setText(RecipeName);
                        }
                        else if(finalI ==2) {
                            potential_meal_2.setText(RecipeName);
                        }
                        else if(finalI ==3) {
                            potential_meal_3.setText(RecipeName);
                        }
                        else if(finalI ==4) {
                            potential_meal_4.setText(RecipeName);
                        }

                    } catch (JSONException ex) {
                        ex.printStackTrace();
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
        }



    }




}