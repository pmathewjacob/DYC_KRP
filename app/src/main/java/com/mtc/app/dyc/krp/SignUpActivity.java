package com.mtc.app.dyc.krp;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mtc.app.dyc.krp.models.RegisterUser;
import com.mtc.app.dyc.krp.widget.DatePickerFragment;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";
    private int mSetAdminCount = 0;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText mPhoneNumberField;
    private EditText mFullNameField;
    private TextView mAgeField;
    private Spinner mParishField;
    private Spinner mCentreField;
    private Spinner mGenderField;
    private LinearLayout mSignUpButton;
    private String mCentre, mParish, mGender;
    private String[] mParishArray;
    private ArrayAdapter<String> parishAdapter;
    private ImageView mIcon;
    private int mYear;
    private int mMonth;
    private int mDay;
    private ImageView mCalendarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mSetAdminCount = 0;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        mPhoneNumberField = findViewById(R.id.field_phoneNumber);
        mFullNameField = findViewById(R.id.field_fName);
        mAgeField = findViewById(R.id.field_Age);
        mCentreField = findViewById(R.id.field_Centre);
        mParishField = findViewById(R.id.field_Parish);
        mGenderField = findViewById(R.id.field_Gender);
        mSignUpButton = findViewById(R.id.button_sign_up);
        mParishArray = getResources().getStringArray(R.array.bangalore_parish_names);
        parishAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mParishArray);
        parishAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        mParishField.setAdapter(parishAdapter);
        mParish = parishAdapter.getItem(0);
        mCentre = getResources().getStringArray(R.array.centres)[0];
        mGender = getResources().getStringArray(R.array.gender)[0];

        mCentreField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCentre = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "mCentre::" + mCentre + " i::" + i);
                switch(i) {
                    case 0:
                        mParishArray = getResources().getStringArray(R.array.andaman_and_nicobar_parish_names);
                        break;
                    case 1:
                        mParishArray = getResources().getStringArray(R.array.bangalore_parish_names);
                        break;
                    case 2:
                        mParishArray = getResources().getStringArray(R.array.chennai_parish_names);
                        break;
                    case 3:
                        mParishArray = getResources().getStringArray(R.array.coimbatore_parish_names);
                        break;
                    case 4:
                        mParishArray = getResources().getStringArray(R.array.hyderabad_parish_names);
                        break;
                    case 5:
                        mParishArray = getResources().getStringArray(R.array.kuwait_parish_names);
                        break;
                    case 6:
                        mParishArray = getResources().getStringArray(R.array.manipal_parish_names);
                        break;
                    case 7:
                        mParishArray = getResources().getStringArray(R.array.mangalore_parish_names);
                        break;
                    case 8:
                        mParishArray = getResources().getStringArray(R.array.mysore_parish_names);
                        break;
                    case 9:
                        mParishArray = getResources().getStringArray(R.array.salem_parish_names);
                        break;
                    case 10:
                        mParishArray = getResources().getStringArray(R.array.vellore_parish_names);
                        break;
                    default:
                        mParishArray = getResources().getStringArray(R.array.no_parish_names);
                }
                parishAdapter = new ArrayAdapter<>(parishAdapter.getContext(), android.R.layout.simple_spinner_item, mParishArray);
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

        mGenderField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mGender = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mGender = null;
            }
        });

        // Click listeners
        mSignUpButton.setOnClickListener(this);

        mCalendarImage = findViewById(R.id.register_user_detail_Age_icon);

        mCalendarImage.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });

        mIcon = findViewById(R.id.icon);
        mIcon.setOnLongClickListener(view -> {
            mSetAdminCount++;
            if (mSetAdminCount == 5) {
                if (Utils.isAdmin(getApplicationContext())) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("admin", false).apply();
                    Toast.makeText(getApplicationContext(), "back to user!!", Toast.LENGTH_SHORT).show();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("admin", true).apply();
                    Toast.makeText(getApplicationContext(), "admin unlocked!!", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });
    }


    public void setDob(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;

        mAgeField.setText(mDay + "/" + mMonth + "/" + mYear);
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
            registerUser(mAuth.getCurrentUser().getUid(), phoneNumber, fullName, mCentre, mParish, age, mGender);
        hideProgressDialog();
        // Go to MainActivity
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

        if (!mGenderField.isSelected() && mGender == null) {
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
    private void registerUser(String userId, String phoneNumber, String fullName, String center, String parish, String age, String gender) {
        RegisterUser registerUser = new RegisterUser(phoneNumber, fullName, center, parish, age, gender);

        String key = mDatabase.child("adminRegister").push().getKey();
        Map<String, Object> postValues = registerUser.toMap();
        Map<String, Object> postAdminValues = postValues;
        postAdminValues.put("userId", userId);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/register/" + userId + "/" + key, postValues);
        childUpdates.put("/adminRegister/" + key, postAdminValues);

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
