package io.sodaoud.heretest.app.presenter;

import android.util.Log;

import javax.inject.Inject;

import io.sodaoud.heretest.app.di.ApplicationComponent;
import io.sodaoud.heretest.app.location.LocationProvider;
import io.sodaoud.heretest.app.model.AutoSuggestResult;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.SearchResult;
import io.sodaoud.heretest.app.network.PlacesService;
import io.sodaoud.heretest.app.view.SearchPlaceView;
import rx.Observable;
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

    public void init(ApplicationComponent component) {
        component.inject(this);
    }

    public void setBbox(String bbox) {
        this.bbox = bbox;
    }

    public void autoSuggest(String query) {

        if (subscription != null && subscription.isUnsubscribed())
            subscription.unsubscribe();

        // when location available don t use the bounding box parameter
        Observable<AutoSuggestResult> observable = (provider.getLocation() == null) ?
                placesService.autoSuggest(bbox, query, "plain", 4) :
                placesService.autoSuggest(query, "plain", 4);

        subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(autoSuggestResult -> {
                            view.showSuggestions(autoSuggestResult.getResults());
                            view.showSuggestionProgress(false);
                        },
                        error -> Log.e(TAG, "Error loading suggestions", error));

    }

    public void onPlaceClicked(Place place) {
        view.retrunPlace(place);
    }

    private void showError(Throwable error) {
        Log.e(TAG, "Error search", error);
        view.showProgress(false);
        view.showError("Error searching places");
    }

    public void search(String query) {
        if (subscription != null && subscription.isUnsubscribed())
            subscription.unsubscribe();
        view.showProgress(true);

        // when location available don t use the bounding box parameter
        Observable<SearchResult> observable = (provider.getLocation() == null) ?
                placesService.searchPlace(bbox, query, "plain") :
                placesService.searchPlace(query, "plain");

        subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placesResult -> {
                            view.showProgress(false);
                            view.setItems(placesResult.getPlaces());
                        },
                        error -> {
                            view.showProgress(false);
                            showError(error);
                        });
    }

    public void onLocationClicked() {
        if (provider.getLocation() != null) {
            Place p = new Place("Your Position", provider.getLocation());
            view.retrunPlace(p);
        }
    }
}
