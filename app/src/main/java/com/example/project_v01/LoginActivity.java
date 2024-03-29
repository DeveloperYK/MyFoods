package com.example.project_v01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project_v01.util.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView emailAddress;
    private EditText password;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");


    private Button loginButton;
    private Button createAccountButton_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.login_process);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.email_signin_button);
        createAccountButton_Login = findViewById(R.id.create_acc_button_login);

        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);


        createAccountButton_Login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
        }
    });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(emailAddress.getText().toString(), password.getText().toString());
            }
        });
}

    private void login(String email, String paswd) {

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(paswd)) {

            firebaseAuth.signInWithEmailAndPassword(email, paswd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "Error On Login", Toast.LENGTH_LONG)
                                        .show();
                            } else {
                                FirebaseUser user = firebaseAuth.getCurrentUser(); // current user trying to login
                                final String CurrentUserId = user.getUid();
                                collectionReference.whereEqualTo("userId", CurrentUserId)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if (error != null) {

                                                }
                                                if (!value.isEmpty()) {
                                                    // Success
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    for (QueryDocumentSnapshot snapshot : value) {
                                                        Log.d("Login", "onEvent: Success");
                                                        startActivity(new Intent(LoginActivity.this,
                                                                CreatingFoodPlanActivity.class));
                                                        User user = User.getInstance();
                                                        user.setUserId(snapshot.getString("userId"));   // save user information in User object
                                                        user.setFirstName(snapshot.getString("firstName"));
                                                        user.setLastName(snapshot.getString("lastName"));
                                                        Intent intent = new Intent(LoginActivity.this, ShowFoodPlanActivity.class);
                                                        startActivity(intent);
                                                    }

                                                }

                                            }
                                        });

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this,"Error On Login", Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

        }else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this,
                    "Please enter email and password",
                    Toast.LENGTH_LONG)
                    .show();

        }
    }
}