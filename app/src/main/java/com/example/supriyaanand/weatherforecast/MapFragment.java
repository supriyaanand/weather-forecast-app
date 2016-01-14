package com.example.supriyaanand.weatherforecast;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hamweather.aeris.communication.AerisCallback;
import com.hamweather.aeris.communication.AerisEngine;
import com.hamweather.aeris.communication.EndpointType;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.maps.interfaces.OnAerisMapLongClickListener;
import com.hamweather.aeris.model.AerisResponse;
import com.hamweather.aeris.tiles.AerisTile;

public class MapFragment extends MapViewFragment implements OnAerisMapLongClickListener, AerisCallback {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interactive_maps, container, false);

        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), "com.example.supriyaanand.weatherforecast");

        Bundle bundle = getArguments();
        String lat = bundle.getString("lat");
        String lon = bundle.getString("lon");
        Location location = new Location("");
        location.setLatitude(Double.valueOf(lat));
        location.setLongitude(Double.valueOf(lon));

        mapView = (AerisMapView)view.findViewById(R.id.aerisfragment_map);
        mapView.init(savedInstanceState, AerisMapView.AerisMapType.GOOGLE);
        mapView.moveToLocation(location, 5);
        mapView.addLayer(AerisTile.RADSAT);
        mapView.setOnAerisMapLongClickListener(this);

        return view;
    }

    @Override
    public void onMapLongClick(double lat, double longitude) {
        // code to handle map long press. i.e. Fetch current conditions?
        // see demo app MapFragment.java
    }

    @Override
    public void onResult(EndpointType endpointType, AerisResponse aerisResponse) {
        // code to handle map long press. i.e. Fetch current conditions?
        // see demo app MapFragment.java
    }
}
