package com.mtc.app.dyc.krp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mtc.app.dyc.krp.models.RegisterUser;

public class RegisterUserDetailActivity extends BaseActivity {

    public static final String EXTRA_POST_KEY = "post_key";
    private static final String TAG = "RegisterUserDetailAct";
    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;
    private String mPostKey;

    private TextView mName;
    private TextView mPhoneNumber;
    private TextView mAge;
    private TextView mParish;
    private TextView mGender;
    private DatabaseReference mPostReferenceAdmin;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_detail);

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }


        // Initialize Database
        mPostReferenceAdmin = FirebaseDatabase.getInstance().getReference()
                .child("adminRegister")
                .child(mPostKey);

        // Initialize Views
        mName = findViewById(R.id.register_user_detail_fName);
        mPhoneNumber = findViewById(R.id.register_user_detail_phoneNumber);
        mParish = findViewById(R.id.register_user_detail_Parish);
        mAge = findViewById(R.id.register_user_detail_Age);
        mGender = findViewById(R.id.register_user_detail_gender);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_delete && mPostReference != null && mPostReferenceAdmin != null && mUserId != null) {
            mPostReference = FirebaseDatabase.getInstance().getReference()
                    .child("register")
                    .child(mUserId)
                    .child(mPostKey);
            Utils.deleteData(mPostReference);
            Utils.deleteData(mPostReferenceAdmin);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get RegisterUser object and use the values to update the UI
                RegisterUser registerUser = dataSnapshot.getValue(RegisterUser.class);
                // [START_EXCLUDE]
                if (registerUser != null) {
                    mName.setText(registerUser.fullName);
                    mPhoneNumber.setText(registerUser.phoneNumber);
                    mAge.setText(registerUser.dob);
                    mParish.setText(registerUser.parish);
                    mGender.setText(registerUser.gender);
                    mUserId = registerUser.userId;
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(RegisterUserDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        //mPostReference.addValueEventListener(postListener);
        mPostReferenceAdmin.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            //mPostReference.removeEventListener(mPostListener);
            mPostReferenceAdmin.removeEventListener(mPostListener);
        }
    }
}
