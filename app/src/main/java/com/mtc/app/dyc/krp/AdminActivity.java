package com.mtc.app.dyc.krp;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mathewjacob on 21/03/18.
 */

public class AdminActivity extends BaseActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private DatabaseReference mDatabase;
    private TextView mTotalCount, mMaleCount, mFemaleCount;
    private int mMale, mFemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        Button export = findViewById(R.id.export);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTotalCount = findViewById(R.id.total_count);
        mMaleCount = findViewById(R.id.male_count);
        mFemaleCount = findViewById(R.id.female_count);
        mMale = mFemale = 0;
        checkWritePermission();
        export.setOnClickListener(view -> mDatabase.child("adminRegister")
                .orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Gson gson = new Gson();
                String snap = gson.toJson(dataSnapshot.getValue(false), Object.class);
                showProgressDialog();

                try {

                    JSONObject json = new JSONObject(snap);

                    String csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/Users.csv");
                    CSVWriter writer = new CSVWriter(new FileWriter(csv));

                    List<String[]> data = new ArrayList<>();
                    Iterator<String> iter = json.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        JSONObject value = (JSONObject) json.get(key);
                        Iterator<String> iterMini = value.keys();
                        String val[] = new String[value.length()];
                        int x = 0;
                        while (iterMini.hasNext()) {
                            String temp = iterMini.next();
                            val[x++] = value.getString(temp);
                        }
                        data.add(val);
                    }

                    writer.writeAll(data); // data is adding to csv
                    writer.close();

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
                Log.d(AdminActivity.class.getName(), snap);
                Toast.makeText(getApplicationContext(), "Exported!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));

        mDatabase.child("adminRegister").orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                if (map != null) {
                    for (Object user :
                            map.values()) {
                        if (((HashMap<String, String>) user).get("gender").equalsIgnoreCase("male")) {
                            mMale++;
                        } else if (((HashMap<String, String>) user).get("gender").equalsIgnoreCase("female")) {
                            mFemale++;
                        }
                    }
                    if (mMaleCount != null && mFemaleCount != null && mTotalCount != null) {
                        mMaleCount.setText(mMale + "");
                        mFemaleCount.setText(mFemale + "");
                        mTotalCount.setText(dataSnapshot.getChildrenCount() + "");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(), "Write Permission denied", Toast.LENGTH_LONG).show();
                }
            }
            break;
            default:

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
