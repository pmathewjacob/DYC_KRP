package com.mtc.app.dyc.krp.models;

/*
 * Created by mathewjacob on 29/10/17.
 */

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class RegisterUser {

    public String phoneNumber;
    public String parish;
    public String center;
    public String dob;
    public String fullName;
    public String gender;
    public String userId;

    public RegisterUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public RegisterUser(String phoneNumber, String fullName, String center, String parish, String dob, String gender) {
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.parish = parish;
        this.dob = dob;
        this.gender = gender;
        this.center = center;
    }

    public RegisterUser(String phoneNumber, String fullName, String center, String parish, String dob, String gender, String userId) {
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.parish = parish;
        this.dob = dob;
        this.gender = gender;
        this.center = center;
        this.userId = userId;
    }

    // [START RegisterUser_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("phoneNumber", phoneNumber);
        result.put("fullName", fullName);
        result.put("parish", parish);
        result.put("dob", dob);
        result.put("gender", gender);
        result.put("center", center);
        return result;
    }
    // [END RegisterUser_to_map]

}
// [END blog_user_class]

