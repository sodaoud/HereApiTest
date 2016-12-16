package io.sodaoud.heretest.app.util;


import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPolyline;

import java.util.ArrayList;
import java.util.List;

import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.Route;

/**
 * Created by sofiane on 12/13/16.
 */

public class Util {

    public static String vicinityTextTransform(String vicinity) {
        return vicinity.replaceAll("<br/>", ", ");
    }

    public static String getPlace(Place place) {
        return "geo!" + place.getPosition()[0] + "," + place.getPosition()[1];
    }


    public static GeoPolyline getGeoPolyline(Route route) {
        List<GeoCoordinate> coordinates = new ArrayList<>();

        for (String point : route.getShape()) {

            double lat = Double.parseDouble(point.substring(0, point.indexOf(',')));
            double lon = Double.parseDouble(point.substring(point.indexOf(',') + 1));
            coordinates.add(new GeoCoordinate(lat, lon));
        }
        GeoPolyline polyline = new GeoPolyline(coordinates);

        return polyline;
    }

    public static GeoBoundingBox getGeoBoundingBox(Route route) {
        return new GeoBoundingBox(
                new GeoCoordinate(route.getTopLeft().getLatitude(),
                        route.getTopLeft().getLongitude()),
                new GeoCoordinate(route.getBottomRight().getLatitude(),
                        route.getBottomRight().getLongitude())
        );
    }
}
