package com.example.fbparstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fbparstagram.fragments.ComposeFragment;
import com.example.fbparstagram.fragments.PostsViewFragment;
import com.example.fbparstagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

/**
 * Originally represented the Instagram timeline, where you can view
 * all posts made and saved in Parse/Back4App; currently renamed
 * to MainActivity to support fragments.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    BottomNavigationView navigationView;

    /**
     * Defines RecyclerView information.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "in MainActivity");

        //Define bottomnavigation
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                //Android suggests to avoid switch on menu
                if (item.getItemId() == R.id.action_home) {
                    Log.i(TAG, "To home");
                    fragment = new PostsViewFragment();
                } else if (item.getItemId() == R.id.action_compose) {
                    Log.i(TAG, "To compose");
                    fragment = new ComposeFragment();
                } else if (item.getItemId() == R.id.action_profile) {
                    Log.i(TAG, "To profile");
                    fragment = new ProfileFragment();
                } else
                    //Default fragment
                    fragment = new PostsViewFragment();
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        //Set default section (for fragment)
        navigationView.setSelectedItemId(R.id.action_home);

    }

    /**
     * Create menu and menu items.
     * @param menu The menu.
     * @return Modified menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Define meu items
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle clicking of items in menu.
     * @param item The items in the menu.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.action_compose) {
//            //Open new compose activity
//            Intent intent = new Intent(MainActivity.this, ComposeActivity.class);
//            startActivity(intent);
//        } else
        if (item.getItemId() == R.id.mSignOut) {
            //Sign out of Parse/Back4App
            ParseUser.logOut();
            //ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
            Intent intent = new Intent(this, LoginActivity.class);
            //New things to close current activity to return to login
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}