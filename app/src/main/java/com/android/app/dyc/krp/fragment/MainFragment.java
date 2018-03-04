package com.android.app.dyc.krp.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.app.dyc.krp.InfoActivity;
import com.android.app.dyc.krp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    private LinearLayout mShare;
    private LinearLayout mInfo;
    private ImageView mIcon;
    private TextView mCountdownTimer;
    private long mStartTime;
    // [END define_database_reference]

    public MainFragment() {}

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]
        mIcon = rootView.findViewById(R.id.icon);
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert();
            }
        });
        mShare = rootView.findViewById(R.id.share_button);
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareVia();
            }
        });

        mInfo = rootView.findViewById(R.id.info_button);
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InfoActivity.class));
            }
        });

        final Calendar cal = Calendar.getInstance();
        cal.set(2018, 4, 24);
        mCountdownTimer = rootView.findViewById(R.id.countdown_timer);
        mStartTime = cal.getTimeInMillis() - System.currentTimeMillis();
        if (mStartTime > 0) {
            final Calendar def = Calendar.getInstance();
            int days = cal.get(Calendar.DAY_OF_YEAR) - def.get(Calendar.DAY_OF_YEAR) - 1;
            int hours = 23 - def.get(Calendar.HOUR_OF_DAY);
            int mins = 59 - def.get(Calendar.MINUTE);
            int sec = 59 - def.get(Calendar.SECOND);
            mCountdownTimer.setText(days + " days " + hours + ":" + mins + ":" + sec);

            new CountDownTimer(mStartTime, 1000) {

                public void onTick(long millisUntilFinished) {
                    if (cal.getTimeInMillis() - System.currentTimeMillis() > 0) {
                        mCountdownTimer.setText(
                                (cal.get(Calendar.DAY_OF_YEAR) - Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1) + " days "
                                        + (23 - Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + ":"
                                        + (59 - Calendar.getInstance().get(Calendar.MINUTE)) + ":"
                                        + (59 - Calendar.getInstance().get(Calendar.SECOND)));
                    } else {
                        mCountdownTimer.setText("DYC Started!");
                    }
                }

                public void onFinish() {
                    mCountdownTimer.setText("DYC Started!");
                }
            }.start();
        } else {
            mCountdownTimer.setText("DYC Started!");
        }


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up FirebaseRecyclerAdapter with the Query
        //Query registeredUsersQuery = getQuery(mDatabase);

        // Button launches NewPostActivity

    }

    public void alert() {

        AlertDialog.Builder alertAdd = new AlertDialog.Builder(
                getActivity());
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(R.layout.dialog_main, null);

        alertAdd.setView(view);
        alertAdd.show();
    }


    public void shareVia() {
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "DYC KRP APP");
        String sAux = "Hey check out the DYC app for 2018!!\n";
        sAux = sAux + "https://play.google.com/store/apps/details?id=com.sec.android.app.shealth";
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(i, "Share via"));
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Query getQuery(DatabaseReference databaseReference) {

        return databaseReference
                .child("register")
                .child(getUid())
                .limitToFirst(100);
    }

}
