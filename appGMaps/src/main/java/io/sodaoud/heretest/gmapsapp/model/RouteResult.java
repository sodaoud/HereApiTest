package io.sodaoud.heretest.gmapsapp.model;

/**
 * Created by sofiane on 12/13/16.
 */

public class RouteResult {
    class Response {
        Route[] route;
    }

    Response response;

    public Route[] getRoutes() {
        return response.route;
    }
}
