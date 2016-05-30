package com.example.iguest.flowww;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://flowww.firebaseio.com/");
        setContentView(R.layout.activity_sign_up);

        Button btn = (Button) findViewById(R.id.signUpButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("blah", "creating an account!!!!");

                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);


                ref.createUser(username.getText().toString(), password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                        }
                @Override
                public void onError(FirebaseError firebaseError) {
                        // there was an error
                        }
                });
            }
        });
    }
}


