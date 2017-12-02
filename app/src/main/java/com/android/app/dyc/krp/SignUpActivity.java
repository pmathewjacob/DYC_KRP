package com.android.app.dyc.krp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.app.dyc.krp.models.RegisterUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.android.app.dyc.krp.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText mPhoneNumberField;
    private EditText mFullNameField;
    private EditText mAgeField;
    private Spinner mParishField;
    private Spinner mCentreField;
    private Button mSignUpButton;
    private String mCentre, mParish;
    private String[] mParishArray;
    private ArrayAdapter<String> parishAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        mPhoneNumberField = findViewById(R.id.field_phoneNumber);
        mFullNameField = findViewById(R.id.field_fName);
        mAgeField = findViewById(R.id.field_Age);
        mCentreField = findViewById(R.id.field_Centre);
        mParishField = findViewById(R.id.field_Parish);
        mSignUpButton = findViewById(R.id.button_sign_up);
        mParishArray = getResources().getStringArray(R.array.bangalore_parish_names);
        parishAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mParishArray);
        parishAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        mParishField.setAdapter(parishAdapter);
        mCentreField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCentre = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "mCentre::" + mCentre + " i::" + i);
                switch(i) {
                    case 0:
                        mParishArray = getResources().getStringArray(R.array.bangalore_parish_names);
                        break;
                    case 2:
                        mParishArray = getResources().getStringArray(R.array.hyderabad_parish_names);
                        break;
                    case 1:
                        mParishArray = getResources().getStringArray(R.array.chennai_parish_names);
                        break;
                    default:
                        mParishArray = getResources().getStringArray(R.array.no_parish_names);
                }
                parishAdapter = new ArrayAdapter<String>(parishAdapter.getContext(), android.R.layout.simple_spinner_item, mParishArray);
                parishAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                mParishField.setAdapter(parishAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mCentre = null;
            }
        });

        mParishField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mParish = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mParish = null;
            }
        });

        // Click listeners
        mSignUpButton.setOnClickListener(this);
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String phoneNumber = mPhoneNumberField.getText().toString();
        final String fullName = mFullNameField.getText().toString();
        final String age = mAgeField.getText().toString();

        if(mAuth.getCurrentUser() != null)
            registerUser(mAuth.getCurrentUser().getUid(), phoneNumber, fullName, mParish, age);
        hideProgressDialog();
        // Go to MainActivity
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();

    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mPhoneNumberField.getText().toString())) {
            mPhoneNumberField.setError("Required");
            result = false;
        } else {
            mPhoneNumberField.setError(null);
        }

        if (TextUtils.isEmpty(mFullNameField.getText().toString())) {
            mFullNameField.setError("Required");
            result = false;
        } else {
            mFullNameField.setError(null);
        }

        if (!mCentreField.isSelected() && mCentre == null) {
            result = false;
        }

        if (!mParishField.isSelected() && mParish == null) {
            result = false;
        }

        if (TextUtils.isEmpty(mAgeField.getText().toString())) {
            mAgeField.setError("Required");
            result = false;
        } else {
            mAgeField.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void registerUser(String userId, String phoneNumber, String fullName, String parish, String age) {
        RegisterUser registerUser = new RegisterUser(phoneNumber, fullName, parish, age);

        String key = mDatabase.child("register").push().getKey();
        Map<String, Object> postValues = registerUser.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/register/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END basic_write]

    @Override
    public void onClick(View v) {
        int i = v.getId();
    if (i == R.id.button_sign_up) {
            signUp();
        }
    }
}
