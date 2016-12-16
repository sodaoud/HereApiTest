package io.sodaoud.heretest.gmapsapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.sodaoud.heretest.gmapsapp.HereTestApplication;
import io.sodaoud.heretest.gmapsapp.R;
import io.sodaoud.heretest.gmapsapp.location.LocationProvider;
import io.sodaoud.heretest.gmapsapp.model.Route;
import io.sodaoud.heretest.gmapsapp.util.Util;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final String TAG = MapActivity.class.getName();
    private static final int REQ_CODE = 231;
    private static final int DIRECTION_REQ = 142;
    private GoogleMap mMap;

    @Inject
    LocationProvider provider;

    private Marker myPositionMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ((HereTestApplication) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @OnClick(R.id.directions)
    void onDirectionsClick(View view) {
        Intent i = new Intent(this, RouteActivity.class);
        startActivityForResult(i, DIRECTION_REQ);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initLocation();
    }

    private void initLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE);
            return;
        }
        provider.connect();
        provider.addListener(this);
    }

    public void onLocationChanged(Location location) {
        updateMyPositionOnMap(location);
    }

    void updateMyPositionOnMap(Location location) {
        if (myPositionMarker == null)
            myPositionMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                    .draggable(true));
        else
            myPositionMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE) {
            initLocation();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mMap.setPadding(0, (int) (metrics.density * 60), 0, (int) (metrics.density * 75));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == DIRECTION_REQ) {
            Route route = (Route) data.getSerializableExtra(RouteActivity.ROUTE);
            showRoute(route);
        }
    }

    private void showRoute(Route route) {
        mMap.addPolyline(new PolylineOptions()
                .add(Util.getLatLngArray(route.getShape())).width(10).color(Color.BLUE));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(Util.getLatLngBoundsFromRoute(route), 60));

    }
}
