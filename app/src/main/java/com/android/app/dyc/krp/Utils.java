package com.android.app.dyc.krp;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.android.app.dyc.krp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
 * Created by mathewjacob on 27/01/18.
 */

public class Utils {

    private static boolean sAdmin = false;

    @NonNull
    public static String getUid() throws NullPointerException {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static boolean isAdmin(final Context appContext) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user information
                User user = dataSnapshot.getValue(User.class);
                if (user != null && user.username.equals("admin")) {
                    sAdmin = true;
                    if (!PreferenceManager.getDefaultSharedPreferences(appContext).getString("username", "user").equals("admin")) {
                        PreferenceManager.getDefaultSharedPreferences(appContext).edit().putString("username", "admin").apply();
                    }
                } else {
                    sAdmin = false;
                    if (!PreferenceManager.getDefaultSharedPreferences(appContext).getString("username", "user").equals("user")) {
                        PreferenceManager.getDefaultSharedPreferences(appContext).edit().putString("username", "user").apply();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sAdmin = false;
            }
        });
        return sAdmin;
    }
}
