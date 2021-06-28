package com.example.fbparstagram;

import android.app.Application;

import com.example.fbparstagram.models.Comment;
import com.example.fbparstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {
    private static final String TAG = "ParseApp";
    //Placed keys in secret place just in case
    private static final String APPLICATION_ID = BuildConfig.APPLICATION_ID;
    private static final String CLIENT_KEY = BuildConfig.CLIENT_KEY;

    @Override
    public void onCreate() {
        super.onCreate();

        //Register parse models
        //VERY IMPORTANT
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

        //Parse/Back4App copy-paste from API ref
        //SECURE THESE THINGS!!!
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APPLICATION_ID)
                .clientKey(CLIENT_KEY)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
