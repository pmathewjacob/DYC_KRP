package com.mtc.app.dyc.krp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mtc.app.dyc.krp.R;

public class ContactUsFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "ContactUsFragment";

    public ContactUsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ImageView facebook = view.findViewById(R.id.facebook_link);
        ImageView instagram = view.findViewById(R.id.instagram_link);
        ImageView youtube = view.findViewById(R.id.youtube_link);
        facebook.setOnClickListener(this);
        instagram.setOnClickListener(this);
        youtube.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        //Get url from tag
        switch (v.getId()) {
            case R.id.facebook_link:
            case R.id.instagram_link:
            case R.id.youtube_link:
                String url = (String)v.getTag();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                //pass the url to intent data
                intent.setData(Uri.parse(url));

                startActivity(intent);
            default:

        }

    }
}
