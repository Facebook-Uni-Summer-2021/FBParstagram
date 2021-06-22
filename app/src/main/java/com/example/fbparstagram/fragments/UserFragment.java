package com.example.fbparstagram.fragments;

import com.parse.ParseUser;

public class UserFragment extends ProfileFragment{
    ParseUser temp;

    public UserFragment (ParseUser user) {
        temp = user;
    }

    @Override
    protected void setUser() {
        user = temp;
    }
}
