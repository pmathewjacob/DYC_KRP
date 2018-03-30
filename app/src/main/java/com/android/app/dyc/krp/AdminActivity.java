package com.android.app.dyc.krp;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by mathewjacob on 21/03/18.
 */

public class AdminActivity extends BaseActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        Button export = findViewById(R.id.export);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        export.setOnClickListener(view -> {
            mDatabase.child("adminRegister")
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
                            String val[] = new String[6];
                            int x = 0;
                            while (iterMini.hasNext()) {
                                String temp = iterMini.next();
                                val[x++] = value.getString(temp);
                            }
                            data.add(val);

                            Log.d(AdminActivity.class.getName(), val + "");
                        }

                        writer.writeAll(data); // data is adding to csv
                        writer.close();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    hideProgressDialog();
                    Log.d(AdminActivity.class.getName(), snap);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        });
    }
}
