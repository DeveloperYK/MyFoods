package com.example.project_v01.ChatRoom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_v01.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatbotAnswerActivity extends AppCompatActivity {

    private Button goToAnswer;
    private RequestQueue requestQueue;
    private TextView answerTextView;
    private ArrayList<String> mealTitles;
    private Button MealOption1;
    private Button MealOption2;
    private Button MealOption3;
    private Button goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_answer);
        requestQueue = Volley.newRequestQueue(this);
        answerTextView = findViewById(R.id.text_chatbox_answer);
        mealTitles = new ArrayList<>();

        MealOption1 = findViewById(R.id.chatbox_meal_1);
        MealOption2 = findViewById(R.id.chatbox_meal_2);
        MealOption3 = findViewById(R.id.chatbox_meal_3);

        goBack = findViewById(R.id.back_to_chat);


        String question = getIntent().getStringExtra("question");



        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/food/converse?text="+question,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.d("chat_test", "onResponse: " + response);

                try {
                    JSONArray meals = response.getJSONArray("media");
                    if (meals.isNull(0)){
                        String answerText = response.getString("answerText");
                        Log.d("chat_test", "onResponse: " + answerText);
                        outputAnswerText(answerText);
                    }
                    else {
                        int meallength = meals.length();
                        Log.d("chat_test", "onResponse: " + meallength);

                        for(int i = 0; i < meallength; i++) {
                            JSONObject meal = meals.getJSONObject(i);
                            mealTitles.add((String) meal.get("title"));

                        }
                        Log.d("chat_test", "onResponse: " + mealTitles);
                        if (mealTitles.size() >= 3) {
                            Random random = new Random();
                            String[] randomMeals =new String[3];
                            randomMeals[0] = mealTitles.get((random.nextInt(mealTitles.size())));
                            mealTitles.remove(randomMeals[0]);
                            Log.d("chat_test", "onResponse: "+ randomMeals[0]);

                            randomMeals[1] = mealTitles.get((random.nextInt(mealTitles.size()-1)));
                            mealTitles.remove(randomMeals[1]);
                            Log.d("chat_test", "onResponse: "+ randomMeals[1]);

                            randomMeals[2] = mealTitles.get((random.nextInt(mealTitles.size()-2)));
                            mealTitles.remove(randomMeals[2]);
                            Log.d("chat_test", "onResponse: "+ randomMeals[2]);

                            AllMeals(randomMeals);

                        }

                        else if (mealTitles.size() == 2) {
                            twoMeals(mealTitles.get(0), mealTitles.get(1));
                        }
                        else {
                            oneMeal(mealTitles.get(0));

                        }

                    }

                } catch (JSONException e) {
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


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatbotAnswerActivity.this, ChatRoomActivity.class);
                ChatbotAnswerActivity.this.startActivity(intent);
            }
        });




    }

    private void oneMeal(final String meal1) {
        answerTextView.setText("Here is an option...");
        MealOption1.setText(meal1);
        MealOption1.setVisibility(View.VISIBLE);

        MealOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatbotAnswerActivity.this, ChatRoomRecipesActivity.class);
                intent.putExtra("meal", meal1);
                ChatbotAnswerActivity.this.startActivity(intent);
            }
        });
    }

    private void twoMeals(final String meal1, final String meal2) {
        answerTextView.setText("Here are two options to choose from...");
        MealOption1.setText(meal1);
        MealOption2.setText(meal2);

        MealOption1.setVisibility(View.VISIBLE);
        MealOption2.setVisibility(View.VISIBLE);

        MealOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatbotAnswerActivity.this, ChatRoomRecipesActivity.class);
                intent.putExtra("meal", meal1);
                ChatbotAnswerActivity.this.startActivity(intent);
            }
        });

        MealOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatbotAnswerActivity.this, ChatRoomRecipesActivity.class);
                intent.putExtra("meal", meal2);
                ChatbotAnswerActivity.this.startActivity(intent);
            }
        });

    }

    private void AllMeals(final String[] randomMeals) {
        answerTextView.setText("Here are some options to choose from...");

        MealOption1.setText(randomMeals[0]);
        MealOption2.setText(randomMeals[1]);
        MealOption3.setText(randomMeals[2]);

        MealOption1.setVisibility(View.VISIBLE);
        MealOption2.setVisibility(View.VISIBLE);
        MealOption3.setVisibility(View.VISIBLE);

        MealOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatbotAnswerActivity.this, ChatRoomRecipesActivity.class);
                intent.putExtra("meal", randomMeals[0]);
                ChatbotAnswerActivity.this.startActivity(intent);
            }
        });

        MealOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatbotAnswerActivity.this, ChatRoomRecipesActivity.class);
                intent.putExtra("meal", randomMeals[1]);
                ChatbotAnswerActivity.this.startActivity(intent);
            }
        });

        MealOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatbotAnswerActivity.this, ChatRoomRecipesActivity.class);
                intent.putExtra("meal", randomMeals[2]);
                ChatbotAnswerActivity.this.startActivity(intent);
            }
        });




    }

    private void outputAnswerText(String answerText) {
        answerTextView.setText(answerText);


    }
}