package com.example.iguest.flowww;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
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

        setContentView(R.layout.activity_login);

        Button login = (Button) findViewById(R.id.loginButton);
        Button signUp = (Button) findViewById(R.id.signUpButton);

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
                    Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                    //intent.putExtra("EXTRA_TEXT", null); // not sure what string to use here...
                    startActivity(intent);
                        }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                        // there was an error
                        }
                });

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                //intent.putExtra("EXTRA_TEXT", null); // not sure what string to use here...
                startActivity(intent);            }
        });

    }


}





