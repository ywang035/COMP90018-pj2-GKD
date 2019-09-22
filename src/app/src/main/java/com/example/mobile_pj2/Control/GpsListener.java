package com.example.mobile_pj2.Control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.mobile_pj2.Data.Model.Building;
import com.example.mobile_pj2.Data.Model.GeoPoint;
import com.example.mobile_pj2.Data.UpdateCallback;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class GpsListener implements Runnable {

    private Context context;
    private final String TAG = getClass().getName();
    private String provider;
    private CopyOnWriteArrayList<Building> buildingList;
    private MainController mainController;
    private UpdateCallback updateCallback;

    public GpsListener(Context context, CopyOnWriteArrayList<Building> buildingList, MainController mainController,UpdateCallback updateCallback) {
        this.context = context;
        this.buildingList = buildingList;
        this.mainController = mainController;
        this.updateCallback = updateCallback;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void run() {

        final LocationManager locationManager;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> list = locationManager.getProviders(true);
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            updateLocation(location);
        }
        locationManager.requestLocationUpdates(provider, 3, 5,locationlistener);
    }


    LocationListener locationlistener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
            Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0) {

                }
            }
        };
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void updateLocation(Location location) {
        double x = location.getLongitude();
        double y = location.getLatitude();
        GeoPoint myLocation = new GeoPoint(x,y);
        for(Building building: this.buildingList) {
            building.calculateDistanse(myLocation);
            if(building.getInside()){
                if(!building.isInside(myLocation)){
                    mainController.getMyPool().execute(new SubmitTask(-1,building.getBuildingName()));
                    building.setInside(false);
                }
            }else{
                if(building.isInside(myLocation)){
                    mainController.getMyPool().execute(new SubmitTask(1,building.getBuildingName()));
                    building.setInside(true);
                }
            }
        }
        Collections.sort(buildingList);

    }
}