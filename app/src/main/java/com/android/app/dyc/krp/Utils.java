package com.android.app.dyc.krp;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.time.LocalDate;
import java.time.Period;

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
        return PreferenceManager.getDefaultSharedPreferences(appContext).getBoolean("admin", false);
    }

    public static void deleteData(DatabaseReference reference) {
        reference.setValue(null);
    }

    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }
}
