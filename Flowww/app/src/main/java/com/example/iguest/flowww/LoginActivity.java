package com.example.iguest.flowww;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity {

    public final String TAG = "LOGIN ACTIVITY";
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://flowww.firebaseio.com/");

        getSupportActionBar().hide();

        // IF NOT LOGGED IN OR SIGNED AND JUST WANTS TO GO TO MAP
        if(ref.getAuth() != null) {
            loadMap();
        }

        setContentView(R.layout.activity_login);

        Button login = (Button) findViewById(R.id.loginButton); // login button
        Button signUp = (Button) findViewById(R.id.signUpButton); // sign up button
        Button toMapButton = (Button) findViewById(R.id.toMapButton); // go straight to map button


        // LOGIN ON CLICK - FIREBASE AUTHENTICATION OF LOGIN INFO
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Logging in...");

                EditText username = (EditText) findViewById(R.id.userName);
                EditText password = (EditText) findViewById(R.id.password);

                ref.authWithPassword(username.getText().toString(), password.getText().toString(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                        System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        loadMap();
                        }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                        // there was an error
                        }
                });

            }
        });

        // SIGN UP BUTTON - IF NO ACCOUNT, CLICK HERE FOR SIGN UP ACTIVITY
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                //intent.putExtra("EXTRA_TEXT", null); // not sure what string to use here...
                startActivity(intent);
            }
        });

        // TO MAP BUTTON ON CLICK TAKES YOU TO MAP ACTIVITY
        toMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                //intent.putExtra("EXTRA_TEXT", null); // not sure what string to use here...
                startActivity(intent);
            }
        });


    }

    // LOADS MAP ACTIVITY GOING STRAIGHT TO THE MAP
    public void loadMap() {
        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
        startActivity(intent);
    }

}








