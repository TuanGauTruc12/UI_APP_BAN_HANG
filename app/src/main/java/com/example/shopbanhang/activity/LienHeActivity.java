package com.example.shopbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shopbanhang.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LienHeActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    GoogleMap map;
    final LatLng vitri = new LatLng(10.9035, 106.5801);
    Toolbar toolbarLienHe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lien_he);

        initView();
        actionBar();
        mapFragment.getMapAsync(this);
    }

    private void actionBar() {
        setSupportActionBar(toolbarLienHe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLienHe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void initView() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.apiMaps);
        toolbarLienHe = findViewById(R.id.toolbarLienHe);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(vitri, 15));

        map.addMarker(new MarkerOptions()
                .title("Anh Tuấn Shop")
                .snippet("Shop điện thoại, laptop, đồng hồ chính hãng.")
                .position(vitri));
    }
}