package io.sodaoud.heretest.app.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapPolyline;
import com.here.android.mpa.mapping.MapState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.sodaoud.heretest.app.HereTestApplication;
import io.sodaoud.heretest.app.R;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.PlaceResult;
import io.sodaoud.heretest.app.model.Route;
import io.sodaoud.heretest.app.presenter.MapPresenter;
import io.sodaoud.heretest.app.util.Util;
import io.sodaoud.heretest.app.view.MapView;
import io.sodaoud.heretest.app.view.SearchPlaceView;

public class MainActivity extends AppCompatActivity implements MapView, SearchPlaceView {

    private static final String TAG = MainActivity.class.getName();

    /**
     * permissions request code
     */
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    public static final int DIRECTION_REQ = 142;


    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String BBOX = "BBOX";


    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    private Map map;
    MapFragment mapFragment;

    private MapMarker myPositionMarker;

    private MapPresenter presenter;

    List<MapObject> objects = new ArrayList<>();

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
        presenter.setSearchView(this);
        presenter.init(((HereTestApplication) getApplicationContext()).getComponent());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.init(error -> {
                    if (error == OnEngineInitListener.Error.NONE) {
                        map = mapFragment.getMap();
                        presenter.initLocation();
                        presenter.setBbox(Util.getStringBoundingBox(map.getBoundingBox()));
                        initMapListeners();
                    } else {
                        Log.e(TAG, error.getDetails());
                    }
                }
        );
        setupFloatingSearch();
    }

    private void initMapListeners() {
        map.addTransformListener(new Map.OnTransformListener() {
            @Override
            public void onMapTransformStart() {
                // DO NOTHING
            }

            @Override
            public void onMapTransformEnd(MapState mapState) {
                presenter.setBbox(Util.getStringBoundingBox(map.getBoundingBox()));
            }
        });
    }

    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
                    if (!oldQuery.equals("") && newQuery.equals("")) {
                        mSearchView.clearSuggestions();
                    } else {
                        presenter.autoSuggest(newQuery);
                    }
                }
        );

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                presenter.onPlaceClicked((Place) searchSuggestion);
            }

            @Override
            public void onSearchAction(String query) {
                presenter.search(query);

            }
        });

        mSearchView.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_location) {
                presenter.onLocationClicked();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @OnClick(R.id.directions)
    void onDirectionsClick(View view) {
        presenter.onDirectionClicked(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == DIRECTION_REQ) {
            presenter.onResultRoute((Route) data.getSerializableExtra(RouteActivity.ROUTE));
        }
    }

    @Override
    public void showRoute(Route route, int color) {
        MapPolyline routePolyline = new MapPolyline(Util.getGeoPolyline(route));
        routePolyline.setLineColor(color);
        routePolyline.setLineWidth(10);
        map.addMapObject(routePolyline);
        map.zoomTo(Util.getGeoBoundingBox(route), Map.Animation.LINEAR, Map.MOVE_PRESERVE_ORIENTATION);
        objects.add(routePolyline);
    }

    @Override
    public void showPlace(Place place) {
        MapMarker placeMarker = new MapMarker();
        placeMarker.setCoordinate(new GeoCoordinate(place.getPosition()[0], place.getPosition()[1]));
        map.addMapObject(placeMarker);
        map.setCenter(Util.getGeoCoordinate(place), Map.Animation.LINEAR);
        objects.add(placeMarker);
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

    @Override
    public void removeAllObjects() {
        map.removeMapObjects(objects);
        objects.clear();
    }

    @Override
    public void showSuggestionProgress(boolean show) {
        showProgress(show);
    }

    @Override
    public void showSuggestions(Place[] places) {
        mSearchView.swapSuggestions(Arrays.asList(places));
    }

    @Override
    public void showProgress(boolean b) {
        if (b) mSearchView.showProgress();
        else mSearchView.hideProgress();
    }

    @Override
    public void setItems(PlaceResult[] items) {
// NOT IMPLEMENTED YET (show multiple places on map)
    }

    @Override
    public void showMessage(String text, int resource) {
        View v = getLayoutInflater().inflate(R.layout.message, null);
        ((ImageView) v.findViewById(R.id.message_img)).setImageResource(resource);
        ((TextView) v.findViewById(R.id.message_text)).setText(text);
        new AlertDialog.Builder(this)
                .setView(v)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
