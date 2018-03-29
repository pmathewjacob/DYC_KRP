package com.android.app.dyc.krp.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.app.dyc.krp.AdminActivity;
import com.android.app.dyc.krp.ComingSoonActivity;
import com.android.app.dyc.krp.InfoActivity;
import com.android.app.dyc.krp.R;
import com.android.app.dyc.krp.Utils;

import java.util.Calendar;
import java.util.Locale;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private LinearLayout mShare;
    private LinearLayout mInfo;
    private LinearLayout mLyrics;
    private LinearLayout mSchedule;
    private LinearLayout mAdmin;
    private ImageView mIcon;
    private TextView mCountdownTimer;
    private long mStartTime;
    private long mLastClickTime = 0;
    private CountDownTimer mCountTimer;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if (Utils.isAdmin(getActivity().getApplicationContext())) {
            mAdmin = rootView.findViewById(R.id.admin_button);
            mAdmin.setVisibility(View.VISIBLE);
            mAdmin.setOnClickListener(view -> gotoAdmin());
        }

        mIcon = rootView.findViewById(R.id.icon);
        mIcon.setOnClickListener(view -> alert());
        mShare = rootView.findViewById(R.id.share_button);
        mShare.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            shareVia();
        });

        mInfo = rootView.findViewById(R.id.info_button);
        mInfo.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            startActivity(new Intent(getActivity(), InfoActivity.class));
        });

        mLyrics = rootView.findViewById(R.id.lyrics_button);
        mLyrics.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            startActivity(new Intent(getActivity(), ComingSoonActivity.class));
        });

        mSchedule = rootView.findViewById(R.id.schedule_button);
        mSchedule.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            startActivity(new Intent(getActivity(), ComingSoonActivity.class));
        });

        mCountdownTimer = rootView.findViewById(R.id.countdown_timer);

        return rootView;
    }

    private void startCountDown() {

        final Calendar cal = Calendar.getInstance();
        cal.set(2018, 4, 24, 0, 0, 0);
        mStartTime = cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        Log.d(TAG, "diff in time 1:: " + (cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) + " current:: " + Calendar.getInstance().getTimeInMillis() + " cal:: " + cal.getTimeInMillis());
        if (mStartTime > 0) {
            final Calendar def = Calendar.getInstance();
            int days = cal.get(Calendar.DAY_OF_YEAR) - def.get(Calendar.DAY_OF_YEAR) - 1;
            int hours = 23 - def.get(Calendar.HOUR_OF_DAY);
            int mins = 59 - def.get(Calendar.MINUTE);
            int sec = 59 - def.get(Calendar.SECOND);
            mCountdownTimer.setText(String.format(Locale.ENGLISH, "%d days %d:%d:%d", days, hours, mins, sec));

            if (mCountTimer != null) {
                mCountTimer.cancel();
            }
            mCountTimer = new CountDownTimer(mStartTime, 1000) {

                public void onTick(long millisUntilFinished) {
                    if (cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis() >= 0) {
                        Log.d(TAG, "diff in time :: " + (cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()));
                        mCountdownTimer.setText(
                                String.format(Locale.ENGLISH, "%d days %d:%d:%d", cal.get(Calendar.DAY_OF_YEAR) - Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1, 23 - Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 59 - Calendar.getInstance().get(Calendar.MINUTE), 59 - Calendar.getInstance().get(Calendar.SECOND)));
                    } else {
                        mCountdownTimer.setText("DYC Started!");
                    }
                }

                public void onFinish() {
                    mCountdownTimer.setText("DYC Started!");
                }
            };
            mCountTimer.start();
        } else {
            if (mCountTimer != null) {
                mCountTimer.cancel();
            }
            mCountdownTimer.setText("DYC Started!");
        }


    }

    @Override
    public void onPause() {
        if (mCountTimer != null) {
            mCountTimer.cancel();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        startCountDown();
    }

    private void gotoAdmin() {
        startActivity(new Intent(getActivity(), AdminActivity.class));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up FirebaseRecyclerAdapter with the Query
        //Query registeredUsersQuery = getQuery(mDatabase);

        // Button launches NewPostActivity

    }

    public void alert() {

        final AlertDialog.Builder alertAdd = new AlertDialog.Builder(
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
        sAux = sAux + "https://play.google.com/store/apps/details?id=com.android.app.dyc.krp";
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(i, "Share via"));
    }

}
