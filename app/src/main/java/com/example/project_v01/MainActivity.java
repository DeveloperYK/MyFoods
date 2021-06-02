package com.example.project_v01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button getStartedButton;
    Animation topAnimation;
    Animation bottomAnimation;
    ImageView imageView;
    Button begin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow() .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getStartedButton = findViewById(R.id.startButton);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // go to login/sign up screen
                startActivity(new Intent(MainActivity.this,
                        LoginActivity.class));
            }
        });

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        imageView = findViewById(R.id.Logo);

        imageView.setAnimation(topAnimation);
        imageView.setAnimation(bottomAnimation);

        getStartedButton.setAnimation(bottomAnimation);
        getStartedButton.setAnimation(topAnimation);


    }
}