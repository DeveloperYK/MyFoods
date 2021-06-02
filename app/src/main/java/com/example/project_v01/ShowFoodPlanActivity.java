package com.example.project_v01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.project_v01.ChatRoom.ChatRoomActivity;
import com.example.project_v01.ui.FoodPlanRecyclerAdapter;
import com.example.project_v01.util.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowFoodPlanActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private ArrayList<String> RecipeIdList;
    private RecyclerView recyclerView;
    private FoodPlanRecyclerAdapter foodPlanRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Food Plans");

    public FloatingActionButton GoToChatButton;

    public static String diet_swap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_plan);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        RecipeIdList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        GoToChatButton = findViewById(R.id.GoToChat_button);

        GoToChatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShowFoodPlanActivity.this, ChatRoomActivity.class);
                    ShowFoodPlanActivity.this.startActivity(intent);
                }
            });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);  // show sign out button
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sign_out:  // sign out

                if(user != null && firebaseAuth != null) {
                    firebaseAuth.signOut();

                    startActivity(new Intent(ShowFoodPlanActivity.this, MainActivity.class));
                    finish();

                }



                break;



        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.whereEqualTo("UserId", User.getInstance()
                .getUserId())    // find the document that contains the users meal IDs in Food Plan collection
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot Id : queryDocumentSnapshots) {
                                for (int i = 0; i<21; i++) {
                                    String Recipeid = String.valueOf(Id.get("Meal " + i));
                                    Log.d("Extract", "onSuccess: " + Recipeid);
                                    RecipeIdList.add(Recipeid); // store meal IDs in ArrayList
                                }

                            }

                            Log.d("ExtractId", "onSuccess: " + RecipeIdList);

                            // inflate recycler

                            foodPlanRecyclerAdapter = new FoodPlanRecyclerAdapter(ShowFoodPlanActivity.this, RecipeIdList);
                            recyclerView.setAdapter(foodPlanRecyclerAdapter);
                            foodPlanRecyclerAdapter.notifyDataSetChanged();

                        }else {
                            Log.d("ExtractId", "onSuccess: Snapshots empty");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ExtractId", "onFailure: " + e.getMessage());
            }
        });

    }

    public static String get_intent() {

        return diet_swap;
    }

}