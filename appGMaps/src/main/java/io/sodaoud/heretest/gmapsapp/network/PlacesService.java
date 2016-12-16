package io.sodaoud.heretest.gmapsapp.network;

import io.sodaoud.heretest.gmapsapp.model.AutoSuggestResult;
import io.sodaoud.heretest.gmapsapp.model.SearchResult;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sofiane on 12/12/16.
 */

public interface PlacesService {

    @GET("discover/search")
    Observable<SearchResult> searchPlace(@Query("at") String at, @Query("q") String q, @Query("tf") String tf);


    @GET("autosuggest")
    Observable<AutoSuggestResult> autoSuggest(@Query("at") String at, @Query("q") String q, @Query("tf") String tf, @Query("size") int size);

}
