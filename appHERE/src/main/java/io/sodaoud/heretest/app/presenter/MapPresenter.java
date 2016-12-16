package io.sodaoud.heretest.app.presenter;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationListener;

import javax.inject.Inject;

import io.sodaoud.heretest.app.HereTestApplication;
import io.sodaoud.heretest.app.location.LocationProvider;
import io.sodaoud.heretest.app.model.Route;
import io.sodaoud.heretest.app.view.MapView;

/**
 * Created by sofiane on 12/16/16.
 */

public class MapPresenter implements LocationListener {

    private MapView view;

    @Inject
    LocationProvider provider;

    public MapPresenter(MapView view) {
        this.view = view;
    }

    public void init(Context ctx) {
        ((HereTestApplication) ctx.getApplicationContext()).getComponent().inject(this);
    }

    public void initLocation() {
        provider.connect();
        provider.addListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        view.showPosition(location);
    }

    public void onResultRoute(Route route) {
        view.showRoute(route);
    }
}
