package com.msoft.garbagemanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddNewProduct extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "AddNewProduct";
    private static final int MAP_ACTIVITY_REQUEST = 1 ;
    private GoogleMap mMap;
    double lat=0,log=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== MAP_ACTIVITY_REQUEST){
            if(resultCode== Activity.RESULT_OK){
                lat= data.getDoubleExtra("lat",0);
                log= data.getDoubleExtra("long",0);
                if((lat!=0 || log!=0) && mMap!=null){
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat,log)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,log)));
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                        Intent i=new Intent(AddNewProduct.this,MapsActivity.class);
                        i.putExtra("lat",lat);
                        i.putExtra("log",log);
                        startActivityForResult(i, MAP_ACTIVITY_REQUEST);
            }
        });
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if(lat!=0 || log!=0){
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat,log)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,log)));
        }
    }
}