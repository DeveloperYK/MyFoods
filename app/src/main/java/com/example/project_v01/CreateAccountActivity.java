package com.example.project_v01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project_v01.util.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

    private Button createAccountButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // FireStore Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Users");

    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;
    private EditText firstNameEditText;
    private EditText lastNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firebaseAuth = FirebaseAuth.getInstance();


        firstNameEditText = findViewById(R.id.FirstName);
        lastNameEditText = findViewById(R.id.LastName);

        createAccountButton = findViewById(R.id.create_acc_button);
        progressBar = findViewById(R.id.create_acct_progress);
        emailEditText = findViewById(R.id.emailAccount);
        passwordEditText = findViewById(R.id.passwordAccount);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser(); // check to see user is the same as in onstart();

                if(currentUser != null) {
                    // user is already logged in
                }
                else {
                    // no user is logged in yet
                }
            }
        };

        createAccountButton.setOnClickListener(new View.OnClickListener() { // when this button is pressed user wants to create a new account
            @Override
            public void onClick(View view) {


                if(!TextUtils.isEmpty(emailEditText.getText())
                        && !TextUtils.isEmpty(passwordEditText.getText())
                        && !TextUtils.isEmpty(firstNameEditText.getText())
                        && !TextUtils.isEmpty(lastNameEditText.getText())) {

                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String firstName = firstNameEditText.getText().toString();
                    String lastName = lastNameEditText.getText().toString();


                    createUser(email, password, firstName, lastName);   // method to create a new account

                }

                else {
                    Toast.makeText(CreateAccountActivity.this, "Empty Fields Not Allowed", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }


    private void createUser(final String email, String password, final String firstName, final String lastName) {
        if(!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(firstName)
                && !TextUtils.isEmpty(lastName)) {  // if user has inputted all required information to create an account then...

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {       // Account has been successfully made
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                final String currentUserId = currentUser.getUid(); // we store all the user information in firestore cloud database, giving them a unique id too

                                Map<String, String> userObj = new HashMap<>();  // save to  firestore database
                                userObj.put("userId", currentUserId);
                                userObj.put("email", email);
                                userObj.put("firstName", firstName);
                                userObj.put("lastName", lastName);
                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (Objects.requireNonNull(task.getResult()).exists()) {

                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    String FirstName = task.getResult()
                                                                            .getString("firstName");

                                                                    User user = User.getInstance();  // store the data of the user which is logging in, into a user object
                                                                    user.setUserId(currentUserId);
                                                                    user.setFirstName(firstName);
                                                                    user.setLastName(lastName);

                                                                    Intent intent = new Intent(CreateAccountActivity.this, UserConditionsActivity.class);
                                                                    intent.putExtra("userId", currentUserId);
                                                                    intent.putExtra("email", email);
                                                                    intent.putExtra("firstName", FirstName);
                                                                    intent.putExtra("lastName", lastName);

                                                                    startActivity(intent);
                                                                }
                                                                else {
                                                                    progressBar.setVisibility(View.INVISIBLE);


                                                                }
                                                            }
                                                        });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                            }
                            else {
                                Log.d("NotWorking", "onComplete: Wrong" + task.getException());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // something went wrong
                        }
                    });
        }

        else {

        }
    }




    @Override
    protected void onStart() {  // before everything is on the screen we want the firebase stuff done
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);   // create a statelistener listening to changes in firebase authentication
    }
}