package io.sodaoud.heretest.app.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashSet;
import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * Created by sofiane on 12/13/16.
 */
public class LocationProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Context mContext;

    private final GoogleApiClient client;
    Set<LocationListener> listeners = new HashSet<>();

    private Location location;

    public LocationProvider(Context context) {
        this.mContext = context;
        client = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void connect() {
        if (client.isConnected()) {
            startLocationUpdates();
        } else {


            if (!client.isConnecting())
                client.connect();
        }
    }

    public void disconnect() {
        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        client.disconnect();
    }

    public void addListener(LocationListener listener) {
        listeners.add(listener);
    }

    private void startLocationUpdates() {
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(2000);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client,
                mLocationRequest, this);

        location = LocationServices.FusedLocationApi.getLastLocation(client);
        if (location != null) onLocationChanged(location);
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "GoogleApiClient connection failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        for (LocationListener listener : listeners) {
            listener.onLocationChanged(location);
        }
    }
}
