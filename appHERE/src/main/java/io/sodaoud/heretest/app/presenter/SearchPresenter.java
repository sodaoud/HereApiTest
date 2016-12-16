package io.sodaoud.heretest.app.presenter;

import android.util.Log;

import javax.inject.Inject;

import io.sodaoud.heretest.app.di.ApplicationComponent;
import io.sodaoud.heretest.app.location.LocationProvider;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.network.PlacesService;
import io.sodaoud.heretest.app.view.SearchPlaceView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sofiane on 12/16/16.
 */

public class SearchPresenter {


    private static final String TAG = SearchPresenter.class.getName();
    @Inject
    PlacesService placesService;
    @Inject
    LocationProvider provider;

    private Subscription subscription;
    private SearchPlaceView view;
    private String bbox;

    public SearchPresenter(SearchPlaceView view) {
        this.view = view;
    }

    public void init(ApplicationComponent component){
        component.inject(this);
    }

    public void setBbox(String bbox) {
        this.bbox = bbox;
    }

    public void autoSuggest(String query) {

        if (subscription != null && subscription.isUnsubscribed())
            subscription.unsubscribe();

        subscription = placesService.autoSuggest(bbox, query, "plain", 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(autoSuggestResult -> {
                            view.showSuggestions(autoSuggestResult.getResults());
                            view.showSuggestionProgress(false);
                        },
                        error -> Log.e(TAG, "Error loading suggestions", error));

    }

    public void onPlaceClicked(Place place){
        view.retrunPlace(place);
    }

    private void showError(Throwable error) {
        Log.e(TAG, "Error search", error);
        view.showError("Error searching places");
    }

    public void search(String query) {
        if (subscription != null && subscription.isUnsubscribed())
            subscription.unsubscribe();

        subscription = placesService.searchPlace(bbox, query, "plain")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placesResult -> view.setItems(placesResult.getPlaces()),
                        error -> showError(error));
    }

    public void onLocationClicked() {
        if (provider.getLocation() != null) {
            Place p = new Place("Your Position", provider.getLocation());
            view.retrunPlace(p);
        }
    }
}
