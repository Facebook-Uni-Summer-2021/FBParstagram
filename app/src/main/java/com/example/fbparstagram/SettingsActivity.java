package com.example.fbparstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fbparstagram.databinding.ActivitySettingsBinding;
import com.parse.ParseUser;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log out current user
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                //Return to login activity (use twitter?)
                Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

    }
}