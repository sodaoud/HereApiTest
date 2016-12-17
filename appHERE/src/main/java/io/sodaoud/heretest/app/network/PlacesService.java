package io.sodaoud.heretest.app.network;

import io.sodaoud.heretest.app.model.AutoSuggestResult;
import io.sodaoud.heretest.app.model.SearchResult;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sofiane on 12/12/16.
 */

public interface PlacesService {

    @GET("discover/search")
    Observable<SearchResult> searchPlace(@Query("q") String q, @Query("tf") String tf);

    @GET("discover/search")
    Observable<SearchResult> searchPlace(@Query("in") String in, @Query("q") String q, @Query("tf") String tf);

    @GET("autosuggest")
    Observable<AutoSuggestResult> autoSuggest(@Query("q") String q, @Query("tf") String tf, @Query("size") int size);

    @GET("autosuggest")
    Observable<AutoSuggestResult> autoSuggest(@Query("in") String in, @Query("q") String q, @Query("tf") String tf, @Query("size") int size);

}
