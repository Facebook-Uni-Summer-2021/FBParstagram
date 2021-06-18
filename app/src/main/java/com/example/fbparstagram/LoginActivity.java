package com.example.fbparstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fbparstagram.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    Button btnLogin;
    EditText etUsername;
    EditText etPassword;

    /**
     * Bind to matching xml and set controls to available widgets.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //If a current user is already signed in,
        // bypass login
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity();
        }

        //Login to Parse/Back4App
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "OnClick Login Button");
                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();
                loginUser(username, password);
            }
        });
    }

    /**
     * Login user using Parse
     * @param username User's name
     * @param password User's password
     */
    private void loginUser(String username, String password) {
        Log.i(TAG, "Login user " + username);
        //Runs on background thread for speed and space
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                //Handles onFailure
                if (e != null) {
                    Log.e(TAG, "Login issue: " + e);
                    Toast.makeText(LoginActivity.this,
                            "Login failed, please use correct " +
                                    "username and password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this,
                        "Login successful!",
                        Toast.LENGTH_SHORT).show();
                goToMainActivity();
            }
        });
    }

    /**
     * Sends user to MainActivity
     */
    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this,
                PostsViewActivity.class);
        startActivity(intent);
        finish();
    }
}
/*
Some notes:
I used MongoDB instead of the other choice (Back4App seems to have updated)
“Session” tracks all “User” activity
Create a class to introduce a new model (such as User presenting a user, and Post representing a post on Instagram)
To connect models/classes to one another, use “Pointer” as type of column

Questions:
 */