package com.mtc.app.dyc.krp;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/*
 * Created by mathewjacob on 27/01/18.
 */

public class Utils {

    @NonNull
    public static String getUid() throws NullPointerException {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            return firebaseUser.getUid();
        else return "";
    }

    public static boolean isAdmin(final Context appContext) {
        return PreferenceManager.getDefaultSharedPreferences(appContext).getBoolean("admin", false);
    }

    public static void deleteData(DatabaseReference reference) {
        reference.setValue(null);
    }

}
