package com.mtc.app.dyc.krp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mtc.app.dyc.krp.R;

public class InfoMainFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "InfoMainFragment";

    public InfoMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_info_main, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(12.911199, 77.707425);
        googleMap.addMarker(new MarkerOptions().position(location)
                .title("Nirjhari Conference Centre"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        //Build camera position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(17).build();
        //Zoom in and animate the camera.
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
