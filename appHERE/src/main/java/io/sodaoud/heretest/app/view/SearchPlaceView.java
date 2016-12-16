package io.sodaoud.heretest.app.view;

import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.PlaceResult;

/**
 * Created by sofiane on 12/16/16.
 */

public interface SearchPlaceView extends ListView<PlaceResult> {

    public void showSuggestionProgress(boolean show);

    public void showSuggestions(Place[] places);

    void retrunPlace(Place p);
}
