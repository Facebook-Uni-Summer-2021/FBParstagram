package com.example.fbparstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

/**
 * Represents an object in Parse/Back4App
 */
@ParseClassName("Comment")//The entity/class name made in Parse/Back4App
public class Comment extends ParseObject {
    public static final String KEY_TEXT = "text";
    public static final String KEY_USER = "user";
    public static final String KEY_POSTED_ID = "postedId";
    public static final String KEY_CREATED_AT = "createdAt";

    public String getText () { return getString(KEY_TEXT); }

    public void setText (String text) { put(KEY_TEXT, text); }

    public ParseUser getUser () { return getParseUser(KEY_USER); }

    public void setUser (ParseUser user) { put(KEY_USER, user); }

    public String getPostedId () { return getString(KEY_POSTED_ID); }

    public void setPostedId (String postedId) { put(KEY_POSTED_ID, postedId); }
}
