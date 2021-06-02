package com.example.project_v01.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_v01.ChatRoom.ChatRoomActivity;
import com.example.project_v01.R;
import com.example.project_v01.RecipeDetailsActivity;
import com.example.project_v01.interfaces.AsyncResponseRecipeInfo;
import com.example.project_v01.recipe.Ingredient;
import com.example.project_v01.recipe.Meal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodPlanRecyclerAdapter extends RecyclerView.Adapter<FoodPlanRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> recipeIds;
    private RequestQueue requestQueue;

    public FoodPlanRecyclerAdapter(Context context, ArrayList recipeIds) {
        this.context = context;
        this.recipeIds = recipeIds;
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public FoodPlanRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.meal_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodPlanRecyclerAdapter.ViewHolder holder, final int position) {    // get the names of each recipe

        if(position <= 2) {
            holder.recipeDay.setText("Monday");
        } else if (position <= 5 ) {
            holder.recipeDay.setText("Tuesday");
        } else if (position <= 8 ) {
            holder.recipeDay.setText("Wednesday");
        } else if (position <= 11 ) {
            holder.recipeDay.setText("Thursday");
        } else if (position <= 14 ) {
            holder.recipeDay.setText("Friday");
        } else if (position <= 17 ) {
            holder.recipeDay.setText("Saturday");
        } else if (position <= 20 ) {
            holder.recipeDay.setText("Sunday");
        }

        String mealId = recipeIds.get(position);



        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/"+mealId+"/information",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                   String RecipeName = response.getString("title");



                    holder.ViewRecipeButton.setText(RecipeName);


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
                params.put("x-rapidapi-key", "AUTHENTICATION KEY HERE"); // authentication key
                return params;
            }

        };

        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public int getItemCount() {
        return recipeIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public Button ViewRecipeButton;
        public TextView recipeDay;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            ViewRecipeButton = itemView.findViewById(R.id.ViewRecipeButton);

            recipeDay = itemView.findViewById(R.id.RecipeDay);



            ViewRecipeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("onClick", "onClick: " + recipeIds.get(getAdapterPosition()));
                    Intent intent = new Intent(context, RecipeDetailsActivity.class);

                    intent.putExtra("RecyclerPosition", String.valueOf(getAdapterPosition()));
                    intent.putExtra("RecipeIDs", recipeIds);

                    context.startActivity(intent);
                }
            });


        }
    }
}
