package com.ip.barcodescanner.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import com.ip.barcodescanner.utils.JSONHelper;
import com.ip.barcodescanner.utils.MyLocation;
import com.ip.barcodescanner.utils.persistance.Keys;
import com.ip.barcodescanner.utils.persistance.PersistanceManager;

/**
 * Created by deepak on 7/9/15.
 */
public class LocationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.err.println("Location Service onStartCommand()...");
        try {

            final PersistanceManager pm = new PersistanceManager(this);

            MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                @Override
                public void gotLocation(Location location) {
                    //Got the location!
                    // store the location
                    pm.setValue(Keys._geolocation, JSONHelper.Serialize(location));
                    LocationService.this.stopSelf();
                }
            };
            MyLocation myLocation = new MyLocation();
            myLocation.getLocation(this, locationResult);
        } catch (Exception e) {
            System.err.println("Location Service Execution..." + e);
        }

        return super.onStartCommand(intent, flags, startId);
    }
}