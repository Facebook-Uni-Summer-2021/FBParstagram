package com.example.fbparstagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Represents an object in Parse/Back4App
 */
@ParseClassName("Post")//The entity/class name made in Parse/Back4App
public class Post extends ParseObject {
    //Define all attributes made from Parse/Back4App
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";

    public String getDescription () {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription (String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage () {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage (ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser () {
        return getParseUser(KEY_USER);
    }

    public void setUser (ParseUser user) {
        put(KEY_USER, user);
    }
}
