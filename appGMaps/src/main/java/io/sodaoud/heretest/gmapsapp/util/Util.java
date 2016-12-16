package io.sodaoud.heretest.gmapsapp.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import io.sodaoud.heretest.gmapsapp.model.Place;
import io.sodaoud.heretest.gmapsapp.model.Position;
import io.sodaoud.heretest.gmapsapp.model.Route;

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

    public static LatLng[] getLatLngArray(String[] shape) {
        LatLng[] array = new LatLng[shape.length];

        for (int i = 0; i < shape.length; i++) {
            String point = shape[i];
            double lat = Double.parseDouble(point.substring(0, point.indexOf(',')));
            double lon = Double.parseDouble(point.substring(point.indexOf(',') + 1));
            array[i] = new LatLng(lat, lon);
        }

        return array;
    }

    public static LatLng getLatLng(Position p) {
        return new LatLng(p.getLatitude(), p.getLongitude());
    }

    public static LatLngBounds getLatLngBoundsFromRoute(Route route) {

        return LatLngBounds.builder()
                .include(getLatLng(route.getBottomRight()))
                .include(getLatLng(route.getTopLeft()))
                .build();
    }

}
