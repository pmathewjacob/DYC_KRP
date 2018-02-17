package com.android.app.dyc.krp.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String phoneNumber;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String phoneNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

}
// [END blog_user_class]
