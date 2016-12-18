package io.sodaoud.heretest.app.network;

import io.sodaoud.heretest.app.model.RouteResult;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sofiane on 12/12/16.
 */

public interface RouteService {

    @GET("calculateroute.json")
    Observable<RouteResult> calculateRoute(@Query("app_id") String appId,
                                           @Query("app_code") String appCode,
                                           @Query("waypoint0") String waypoint0,
                                           @Query("waypoint1") String waypoint1,
                                           @Query("mode") String mode,
                                           @Query("alternatives") Integer alternatives,
                                           @Query("instructionFormat") String instructionFormat,
                                           @Query("routeattributes") String routeattributes
    );

}
