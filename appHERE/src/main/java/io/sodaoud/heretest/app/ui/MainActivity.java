package io.sodaoud.heretest.app.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapPolyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.sodaoud.heretest.app.R;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.Route;
import io.sodaoud.heretest.app.presenter.MapPresenter;
import io.sodaoud.heretest.app.util.Util;
import io.sodaoud.heretest.app.view.MapView;

public class MainActivity extends AppCompatActivity implements MapView {

    private static final String TAG = MainActivity.class.getName();

    /**
     * permissions request code
     */
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final int DIRECTION_REQ = 142;


    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String BBOX = "BBOX";

    private Map map;
    MapFragment mapFragment;

    private MapMarker myPositionMarker;

    private MapPresenter presenter;
    private MapPolyline routePolyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            for (int index = permissions.length - 1; index >= 0; --index) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                    return;
                }
            }
            initialize();
        }
    }

    private void initialize() {
        presenter = new MapPresenter(this);
        presenter.init(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.init(error -> {
                    if (error == OnEngineInitListener.Error.NONE) {
                        map = mapFragment.getMap();
                        presenter.initLocation();
                    } else {
                        Log.e(TAG, error.getDetails());
                    }
                }
        );
    }

    @OnClick(R.id.directions)
    void onDirectionsClick(View view) {
        Intent i = new Intent(this, RouteActivity.class);
        String bb = Util.getStringBoundingBox(map.getBoundingBox());
        i.putExtra(MainActivity.BBOX,bb);
        startActivityForResult(i, DIRECTION_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == DIRECTION_REQ) {
            presenter.onResultRoute((Route) data.getSerializableExtra(RouteActivity.ROUTE));
        }
    }

    @Override
    public void showRoute(Route route) {
        if (routePolyline != null) {
            map.removeMapObject(routePolyline);
        }
        routePolyline = new MapPolyline(Util.getGeoPolyline(route));
        routePolyline.setLineColor(Color.BLUE);
        routePolyline.setLineWidth(10);
        map.addMapObject(routePolyline);
        map.zoomTo(Util.getGeoBoundingBox(route), Map.Animation.LINEAR, Map.MOVE_PRESERVE_ORIENTATION);
    }

    @Override
    public void showPlace(Place place) {
        // TODO
    }

    @Override
    public void showPosition(Location location) {
        if (myPositionMarker == null) {
            myPositionMarker = new MapMarker();
            myPositionMarker.setCoordinate(new GeoCoordinate(location.getLatitude(), location.getLongitude()));
            try {
                Image i = new Image();
                i.setImageResource(R.mipmap.ic_launcher);
                myPositionMarker.setIcon(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
            myPositionMarker.setAnchorPoint(new PointF(0.5f, 0.5f));
            map.addMapObject(myPositionMarker);
        } else
            myPositionMarker.setCoordinate(new GeoCoordinate(location.getLatitude(), location.getLongitude()));
    }
}
