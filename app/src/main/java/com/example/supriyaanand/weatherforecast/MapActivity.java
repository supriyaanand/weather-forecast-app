package com.example.supriyaanand.weatherforecast;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hamweather.aeris.communication.AerisEngine;
import com.hamweather.aeris.communication.parameter.PlaceParameter;

public class MapActivity extends AppCompatActivity {

    PlaceParameter place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MapFragment fragment = new MapFragment();

        fragment.setArguments(getIntent().getExtras());

        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}
