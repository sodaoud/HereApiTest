package io.sodaoud.heretest.app.util;

import io.sodaoud.heretest.app.BuildConfig;

/**
 * Created by sofiane on 12/12/16.
 */

public class Constants {

    private static final String PLACES_API_ENDPOINT = "https://places.cit.api.here.com/places/v1/";
    private static final String ROUTE_API_ENDPOINT = "https://route.cit.api.here.com/routing/7.2/";

    private static final String PRODUCTION_PLACES_API_ENDPOINT = "https://places.api.here.com/places/v1/";
    private static final String PRODUCTION_ROUTE_API_ENDPOINT = "https://route.api.here.com/routing/7.2/";

    private static String placesEndPoint;
    private static String routeEndPoint;

    public static String getPlacesApiEndpoint() {
        if (placesEndPoint == null) {
            placesEndPoint = BuildConfig.DEBUG ? PLACES_API_ENDPOINT : PRODUCTION_PLACES_API_ENDPOINT;
        }
        return placesEndPoint;
    }

    public static String getRouteApiEndpoint() {
        if (routeEndPoint == null) {
            routeEndPoint = BuildConfig.DEBUG ? ROUTE_API_ENDPOINT : PRODUCTION_ROUTE_API_ENDPOINT;
        }
        return routeEndPoint;
    }
}
