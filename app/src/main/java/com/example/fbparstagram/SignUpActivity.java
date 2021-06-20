package com.example.fbparstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fbparstagram.databinding.ActivitySignUpBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    EditText etNewUsername;
    EditText etNewPassword;
    EditText etNewPasswordConfirm;
    Button btnCreateAccount;

    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    private void signUp() {
        String username = binding.etNewUsername.getText().toString();
        String password = binding.etNewPassword.getText().toString();
        String passwordConfirm = binding.etNewPasswordConfirm.getText().toString();
        if (password.compareTo(passwordConfirm) != 0) {
            Toast.makeText(SignUpActivity.this,
                    "The passwords must match!",
                    Toast.LENGTH_SHORT).show();
        } else if (username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(SignUpActivity.this,
                    "Please complete all fields!",
                    Toast.LENGTH_SHORT).show();
        }

        ParseUser user = new ParseUser();
        //Set core properties
        user.setUsername(username);
        user.setPassword(password);
        //For standard values that don't exist, use .put("new_value", "value")
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error occurred while signing up: ", e);
                    Toast.makeText(SignUpActivity.this,
                            "Something went wrong. Make sure your name is unique!",
                            Toast.LENGTH_LONG).show();
                }
                //Sign in user automatically
                Log.i(TAG, "User successfully created!");

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error occurred signing in new user: ", e);
                        }
                        Intent intent = new Intent(SignUpActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}