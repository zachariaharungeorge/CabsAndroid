package com.rdxcabs.Utilities;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

/**
 * Created by arung on 17/4/16.
 */
public class GPSTracker extends Service implements LocationListener {

    public final Context mContext;

    boolean isGPSEnabled = false;

    boolean isNetWorkEnabled = false;

    boolean canGetLocation = false;

    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    public static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60 * 1;

    Location location;
    double latitude;
    double longitude;

    protected LocationManager locationManager;

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try{
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetWorkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled && isNetWorkEnabled) {
                this.canGetLocation = true;

                if (isNetWorkEnabled) {
                    //if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if(locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if(location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    //}
                }

                if(isGPSEnabled){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BETWEEN_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Location Services not Enabled");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to Settings and Enable it");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
