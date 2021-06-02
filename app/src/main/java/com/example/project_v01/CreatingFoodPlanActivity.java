package com.example.project_v01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_v01.interfaces.StoringRecipes;
import com.example.project_v01.util.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreatingFoodPlanActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    // for storing id of each recipe

    ArrayList<String> IdList = new ArrayList<>();

    // for api
    private RequestQueue requestQueue;

    private String currentUserId;
    private String currentUserFirstName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    public static String diet_swap;

    // connection to firestore

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference; // directiory to images

    private CollectionReference collectionReference = db.collection("Food Plans");

    private ArrayList<String> dietRequirements;

    private ArrayList<String> otherRequirements;

    private String calories;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);

        ArrayList<String> diet_swapList = getIntent().getStringArrayListExtra("SelectedDiet");
        diet_swap = diet_swapList.get(0);

        dietRequirements = new ArrayList<>();

        otherRequirements = new ArrayList<>();


        firebaseAuth = FirebaseAuth.getInstance();

        // set up progress bar


        // use spoonacular API to create the foodplan

        requestQueue = Volley.newRequestQueue(CreatingFoodPlanActivity.this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }

            }
        };


        createFoodPlan(new StoringRecipes() {
            @Override
            public void IDsStored(ArrayList<String> IdList) {
                saveFoodPlanToFireStore(IdList);
            }
        });





    }




    private void createFoodPlan(final StoringRecipes callBack) {
        calories = getIntent().getStringExtra("Calories");
        Log.d("calories", "createFoodPlan: " + calories);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/mealplans/generate?timeFrame=week&targetCalories="+calories+"&diet="+getAllDietRequirements()+"&exclude="+getAllOtherRequirements()+"&tags=main%20course",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try { // store the id of all the meals in the food plan
                    JSONArray mealPlan = response.getJSONArray("items");

                    Log.d("API", "onResponse: " + mealPlan);

                    for(int i = 0 ;i < 21; i++) {

                        JSONObject day = (JSONObject) mealPlan.get(i);

                        String value = day.getString("value");

                        String id = value.substring((value.indexOf(":") + 1), value.indexOf(","));  // couldn't convert id to json due to a weird error so used substring to extract id from the response instead.


                        Log.d("Testing", "onResponse: Success");

                        IdList.add(id);     // store id of each recipe in this list, we will later use this list to store each id in a document in firestore

                        // get all id's of all meals in the foodplan
                        // save them in a new collection in the firestore database along with the id of the user who the meal plan is for. So we can reference it

                    }

                    Log.d("API", "onResponse: " + IdList);





                } catch (JSONException e) {
                    Log.d("API", "onError: " + e.getMessage());
                }

                if(null != callBack) {
                    Log.d("Interface", "onResponse: " + callBack);
                    callBack.IDsStored(IdList);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "onErrorResponse: " + error.getMessage());
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

    public static String get_intent() {

        return diet_swap;
    }

    private String getAllOtherRequirements() {
        otherRequirements = getIntent().getStringArrayListExtra("SelectedOther"); // get the selected requirements from UserConditionsActivity

        Log.d("retrieve", "getAllOtherRequirements: " + otherRequirements);


        for (int i = 0; i < otherRequirements.size(); i++) {
            return otherRequirements.get(i);

        }
        return null;
    }

    private String getAllDietRequirements() {
        dietRequirements = getIntent().getStringArrayListExtra("SelectedDiet"); // get the selected requirements from UserConditionsActivity


        for (int i = 0; i < dietRequirements.size(); i++) {
            return dietRequirements.get(i);

        }
        return null;
    }


    private void saveFoodPlanToFireStore(ArrayList<String> IdList) {

            if(User.getInstance() != null) {

                currentUserId = User.getInstance().getUserId();

            }
            else {

                Log.d("Save", "OnSaveError: User object does not exist" );
            }

        Map<String, String> storeFoodPlan = new HashMap<>();

        storeFoodPlan.put("UserId" , currentUserId);

        for(int i = 0; i < IdList.size(); i++) {

            storeFoodPlan.put("Meal "+i, IdList.get(i));

        }

        collectionReference.add(storeFoodPlan)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d("Store", "onSuccess: Stored!" );
                        startActivity(new Intent(CreatingFoodPlanActivity.this,
                                ShowFoodPlanActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Store", "onFailure: " + e.getMessage());
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);

        }
    }

    public static class FoodPlanListActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_food_plan_list);
        }
    }
}