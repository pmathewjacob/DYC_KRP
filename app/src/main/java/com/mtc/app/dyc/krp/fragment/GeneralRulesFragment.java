package com.mtc.app.dyc.krp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mtc.app.dyc.krp.R;

public class GeneralRulesFragment extends Fragment {

    private static final String TAG = "ContactUsFragment";

    public GeneralRulesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_general_rules, container, false);
    }
}
