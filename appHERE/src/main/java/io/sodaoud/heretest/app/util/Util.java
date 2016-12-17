package io.sodaoud.heretest.app.util;


import android.location.Location;

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

    public static String getPositionFormat(Location location) {
        if (location == null) return "";
        return new StringBuilder().append("geo:")
                .append(location.getLatitude())
                .append(",")
                .append(location.getLongitude())
                .toString();
    }

    public static String getStringBoundingBox(GeoBoundingBox boundingBox) {
        return new StringBuilder()
                .append(boundingBox.getBottomRight().getLongitude())
                .append(",")
                .append(boundingBox.getBottomRight().getLatitude())
                .append(",")
                .append(boundingBox.getTopLeft().getLongitude())
                .append(",")
                .append(boundingBox.getTopLeft().getLatitude())
                .toString();
    }

    public static GeoCoordinate getGeoCoordinate(Place place) {
        return new GeoCoordinate(place.getPosition()[0], place.getPosition()[1]);
    }
}
