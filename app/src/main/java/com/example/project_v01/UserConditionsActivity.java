package com.example.project_v01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_v01.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserConditionsActivity extends AppCompatActivity {



    private CheckBox diabetes_checkbox;
    private CheckBox highCholesterol_checkbox;
    private CheckBox nothing_checkbox;

    private CheckBox vegetarian_checkbox;
    private CheckBox vegan_checkbox;

    private EditText caloriesInput;
    private double calories;
    private String caloriesAsString;

    private Button CreateAccount;


    private ArrayList<String> dietRequirements;
    private ArrayList<String> otherRequirements;

    private TextView DisplayMessage;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_conditions);

        DisplayMessage = findViewById(R.id.textView_select_conditions);

        String name = getIntent().getStringExtra("firstName");

        DisplayMessage.setText("Hello "+ name + " , Please select any conditions you have from the options below");



        dietRequirements = new ArrayList<>();
        otherRequirements = new ArrayList<>();



        diabetes_checkbox = findViewById(R.id.checkbox_diabetes);
        highCholesterol_checkbox = findViewById(R.id.checkbox_highCholesterol);
        nothing_checkbox = findViewById(R.id.checkbox_nothing);
        vegetarian_checkbox = findViewById(R.id.checkbox_vegetarian);
        vegan_checkbox = findViewById(R.id.checkbox_vegan);

        caloriesInput = findViewById(R.id.enter_calories);

        CreateAccount = findViewById(R.id.create_account_button);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 caloriesAsString = caloriesInput.getText().toString();
                if (caloriesAsString.equals("")) {
                    Toast.makeText(UserConditionsActivity.this, "Please input the daily calories amount", Toast.LENGTH_LONG).show();
                } else {

                     calories = Double.parseDouble(caloriesAsString);

                    if (calories < 1500 || calories > 3000) {
                        Toast.makeText(UserConditionsActivity.this, "Calories must be between 1500 and 3000", Toast.LENGTH_LONG).show();
                    } else {




                        Intent intent = new Intent(UserConditionsActivity.this, CreatingFoodPlanActivity.class);
                        intent.putExtra("SelectedDiet", dietRequirements);
                        intent.putExtra("SelectedOther", otherRequirements);
                        intent.putExtra("Calories", caloriesAsString);
                        startActivity(intent);
                    }
                }
            }
        });

        diabetes_validate();        // validation for checkboxes so checkboxes that contradict each other cannot both be pressed as well as storing chosen checkboxes in arrays to identify what the user has selected
        highCholesterol_validate();
        nothing_validate();
        vegetarian_validate();
        vegan_validate();





    }



    private void diabetes_validate() {
        diabetes_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(diabetes_checkbox.isChecked()) {
                    nothing_checkbox.setEnabled(false);


                    if(vegetarian_checkbox.isChecked()) {
                        otherRequirements.add("sugar");
                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);

                    }
                    else if (vegan_checkbox.isChecked()) {
                        dietRequirements.add("Vegan");
                        Log.d("List", "onClick: " + dietRequirements);
                    }
                    else {
                        dietRequirements.add("Paleo");
                        Log.d("List", "onClick: " + dietRequirements);
                    }

                } else {
                    if(!highCholesterol_checkbox.isChecked()) {
                        nothing_checkbox.setEnabled(true);
                    }

                    if(vegetarian_checkbox.isChecked()) {
                        otherRequirements.remove("sugar");
                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);

                    }else {

                        if(vegan_checkbox.isChecked()) {
                            otherRequirements.remove("Sugar");

                        }


                        dietRequirements.remove("Paleo");
                        Log.d("list", "onClick: " + dietRequirements);
                    }
                }
            }
        });
    }

    private void highCholesterol_validate() {

        highCholesterol_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(highCholesterol_checkbox.isChecked()) {
                    nothing_checkbox.setEnabled(false);

                    if(vegetarian_checkbox.isChecked()) {
                        otherRequirements.add("milk");
                        otherRequirements.add("yogurt");
                        otherRequirements.add("cheese");

                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);

                    }
                    else if (vegan_checkbox.isChecked()) {

                        Log.d("list", "onClick: " + dietRequirements);
                    }

                    else {

                        dietRequirements.add("Whole30");
                        Log.d("list", "onClick: " + dietRequirements);

                    }


                } else {
                    if(!diabetes_checkbox.isChecked()) {
                        nothing_checkbox.setEnabled(true);
                    }

                    if(vegetarian_checkbox.isChecked()) {
                        otherRequirements.remove("milk");
                        otherRequirements.remove("yogurt");
                        otherRequirements.remove("cheese");

                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);

                    }
                    else {


                        dietRequirements.remove("Whole30");
                        Log.d("list", "onClick: " + dietRequirements);
                    }
                }
            }
        });
    }

    private void nothing_validate() {
        nothing_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nothing_checkbox.isChecked()) {
                    highCholesterol_checkbox.setEnabled(false);
                    diabetes_checkbox.setEnabled(false);



                } else {
                    highCholesterol_checkbox.setEnabled(true);
                    diabetes_checkbox.setEnabled(true);
                }
            }
        });
    }

    private void vegetarian_validate() {

        vegetarian_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(vegetarian_checkbox.isChecked()) {
                    vegan_checkbox.setEnabled(false);

                    if(diabetes_checkbox.isChecked()) {
                        dietRequirements.remove("Paleo");
                        dietRequirements.add("Vegetarian");
                        otherRequirements.add("sugar");


                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);

                    } else if(highCholesterol_checkbox.isChecked()) {
                        dietRequirements.remove("Whole30");
                        dietRequirements.add("Vegetarian");
                        otherRequirements.add("milk");
                        otherRequirements.add("yogurt");
                        otherRequirements.add("cheese");

                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);


                    }
                    else {


                        dietRequirements.add("Vegetarian");
                        Log.d("list", "onClick: " + dietRequirements);
                    }


                } else {
                    vegan_checkbox.setEnabled(true);

                    if(diabetes_checkbox.isChecked()) {
                        dietRequirements.add("Paleo");
                        dietRequirements.remove("Vegetarian");
                        otherRequirements.remove("sugar");

                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);

                    }else if (highCholesterol_checkbox.isChecked()) {
                        dietRequirements.add("Whole30");
                        dietRequirements.remove("Vegetarian");
                        otherRequirements.remove("milk");
                        otherRequirements.remove("yogurt");
                        otherRequirements.remove("cheese");

                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);
                        Log.d("List", "onClick: " + otherRequirements);


                    }
                    else {

                        dietRequirements.remove("Vegetarian");
                        Log.d("list", "onClick: " + dietRequirements);
                    }
                }
            }
        });
    }

    private void vegan_validate() {
        vegan_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(vegan_checkbox.isChecked()) {
                    vegetarian_checkbox.setEnabled(false);

                    if(diabetes_checkbox.isChecked()) {
                        dietRequirements.remove("Paleo");
                        dietRequirements.add("Vegan");
                        otherRequirements.add("Sugar");

                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);


                    } else if(highCholesterol_checkbox.isChecked()) {
                        dietRequirements.remove("Whole30");
                        dietRequirements.add("Vegan");

                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);


                    }
                    else {

                        dietRequirements.add("vegan");

                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);

                    }

                } else {
                    vegetarian_checkbox.setEnabled(true);

                    if(diabetes_checkbox.isChecked()) {

                        dietRequirements.remove("Vegan");
                        otherRequirements.remove("Sugar");
                        dietRequirements.add("Paleo");


                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);


                    }
                    else if (highCholesterol_checkbox.isChecked()) {


                        dietRequirements.remove("Vegan");
                        dietRequirements.add("Whole30");

                        Log.d("List", "onClick: " + dietRequirements);
                        Log.d("List", "onClick: " + otherRequirements);



                    }
                    else {
                        dietRequirements.remove("vegan");
                        Log.d("list", "onClick: " + dietRequirements);

                    }
                }
            }
        });

    }


}