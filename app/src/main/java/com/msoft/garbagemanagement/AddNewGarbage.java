package com.msoft.garbagemanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.msoft.garbagemanagement.dao.Garbage;

import java.util.ArrayList;
import java.util.List;

public class AddNewGarbage extends FragmentActivity implements OnMapReadyCallback {
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
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSave();
            }
        });

    }

    private void handleSave() {
        Garbage g=new Garbage();
        try {
            g.setGarbageType(((TextInputEditText) findViewById(R.id.txt_garbage_type)).getText().toString());
            g.setSize(Float.parseFloat(((TextInputEditText) findViewById(R.id.txt_stock_size)).getText().toString()));
            g.setPrice(Float.parseFloat(((TextInputEditText) findViewById(R.id.txt_product_price)).getText().toString()));
            ArrayList<String> types = new ArrayList<>();
            ChipGroup chips = ((ChipGroup) findViewById(R.id.chip_type));
            List<Integer> l = chips.getCheckedChipIds();
            for (int i : l) {
                types.add(((Chip) findViewById(i)).getText().toString());
            }
            g.setTypes(types);
            g.setLat(lat);
            g.setLog(log);
            g.setUserId(FirebaseAuth.getInstance().getUid());
        }
        catch (NumberFormatException e){
            Snackbar.make(findViewById(R.id.root),"Enter Valid Details",Snackbar.LENGTH_SHORT).show();
        }
        if(g.isValid()){
            Log.d(TAG, "handleSave: valid");
            storeToFirebase(g);
            
        }
        else {
//            Toast.makeText(getApplicationContext(),"Enter Valid Details",Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.root),"Enter Valid Details",Snackbar.LENGTH_SHORT).show();
        }
    }

    private void storeToFirebase(Garbage g) {
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        database.collection("garbage").add(g).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                documentReference.update("id",documentReference.getId());
            }
        });
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
                        Intent i=new Intent(AddNewGarbage.this,MapsActivity.class);
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