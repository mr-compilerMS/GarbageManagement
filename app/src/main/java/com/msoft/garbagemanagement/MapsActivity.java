package com.msoft.garbagemanagement;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    private static final String TAG = "MapsActivity";
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    Intent returnIntent = new Intent();
    double lat,log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED, returnIntent);
        super.onBackPressed();
        //finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.setOnMarkerDragListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        setCurrentLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setCurrentLocation();
        }
    }

    public void setCurrentLocation() {
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
                }
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled( true);
            if(this.getIntent().hasExtra("lat") && getIntent().getDoubleExtra("lat",0)!=0){
                findViewById(R.id.btn_save).setEnabled(true);
                LatLng latLng=new LatLng(getIntent().getDoubleExtra("lat",0), getIntent().getDoubleExtra("log",0));
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Location of Garbage")
                        .draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            else
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng curr=new LatLng(location.getLatitude(),location.getLongitude());
                                returnIntent.putExtra("lat",location.getLatitude());
                                returnIntent.putExtra("long",location.getLongitude());
                                findViewById(R.id.btn_save).setEnabled(true);
                                mMap.clear();
                                mMap.addMarker(new MarkerOptions()
                                        .position(curr)
                                        .title("Location of Garbage")
                                        .draggable(true));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(curr));
                            }
                        }
                    });
        }
    }


    @Override
    public void onMarkerDragStart(Marker marker) {    }
    @Override
    public void onMarkerDrag(Marker marker) {    }
    @Override
    public void onMarkerDragEnd(Marker marker) {
        returnIntent.putExtra("lat",marker.getPosition().latitude);
        returnIntent.putExtra("long",marker.getPosition().longitude);
    }
}