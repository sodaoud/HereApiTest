package io.sodaoud.heretest.gmapsapp.model;

/**
 * Created by sofiane on 12/12/16.
 */

public class SearchResult{

    static class Results{
        PlaceResult[] items;
    }

    Results results;

    public PlaceResult[] getPlaces(){
        return results.items;
    }
}
