package io.sodaoud.heretest.app.view;

import android.location.Location;

import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.Route;

/**
 * Created by sofiane on 12/16/16.
 */

public interface MapView {

    public void showRoute(Route route);

    public void showPlace(Place place);

    public void showPosition(Location location);
}
