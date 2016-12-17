package io.sodaoud.heretest.app.network;

import io.sodaoud.heretest.app.model.AutoSuggestResult;
import io.sodaoud.heretest.app.model.SearchResult;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sofiane on 12/12/16.
 */

public interface PlacesService {


    @GET("discover/search")
    Observable<SearchResult> searchPlace(@Header("X-Map-Viewport") String in, @Query("q") String q, @Query("tf") String tf);

    @GET("autosuggest")
    Observable<AutoSuggestResult> autoSuggest(@Header("X-Map-Viewport") String in, @Query("q") String q, @Query("tf") String tf, @Query("size") int size);

}
