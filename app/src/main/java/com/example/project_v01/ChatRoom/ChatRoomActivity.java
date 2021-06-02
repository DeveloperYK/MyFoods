package com.example.project_v01.ChatRoom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_v01.R;
import com.example.project_v01.ShowFoodPlanActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {


    private Button goToAnswer;
    private EditText questionTextView;
    private Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        goToAnswer = findViewById(R.id.ask_chatbot_button);
        questionTextView = findViewById(R.id.chatbox_question);
        goBack = findViewById(R.id.back_to_plan);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatRoomActivity.this, ShowFoodPlanActivity.class);
                ChatRoomActivity.this.startActivity(intent);
            }
        });



        goToAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionTextView.getText() != null) {
                    String question = String.valueOf(questionTextView.getText());
                    question = question.replace(" ", "%20");
                    Log.d("chat_test", "onClick: "+ question);


                    Intent intent = new Intent(ChatRoomActivity.this, ChatbotAnswerActivity.class);
                    intent.putExtra("question", question);
                    ChatRoomActivity.this.startActivity(intent);
                }



            }
        });


    }
}