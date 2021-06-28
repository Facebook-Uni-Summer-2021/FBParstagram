package com.example.fbparstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcel;

/**
 * Represents an object in Parse/Back4App
 */
@Parcel(analyze = Post.class)//Required due to use of Parse?
@ParseClassName("Post")//The entity/class name made in Parse/Back4App
public class Post extends ParseObject {
    //Define all attributes made from Parse/Back4App
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_LIKE_COUNT = "likeCount";
    public static final String KEY_IS_LIKED = "isLiked";
    //public static final String KEY_OBJECT_ID = "objectId";

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

    public long getLikeCount () { return getLong(KEY_LIKE_COUNT); }

    public void setLikeCount (long likeCount) { put(KEY_LIKE_COUNT, likeCount); }

    public boolean getIsLiked () { return getBoolean(KEY_IS_LIKED); }

    public void setIsLiked (boolean isLiked) { put(KEY_IS_LIKED, isLiked); }

//    public String getObjectId () { return getString(KEY_OBJECT_ID); }
//
//    //No setter for Post ID
//    public void setObjectId (String objectId) { put(KEY_OBJECT_ID, objectId); }

//    //May be incorrect
//    public String getDate () {
//        return getString(KEY_CREATED_AT);
//    }
}
