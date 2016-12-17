package io.sodaoud.heretest.app.presenter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

import io.sodaoud.heretest.app.R;
import io.sodaoud.heretest.app.di.ApplicationComponent;
import io.sodaoud.heretest.app.location.LocationProvider;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.network.PlacesService;
import io.sodaoud.heretest.app.ui.SearchActivity;
import io.sodaoud.heretest.app.view.SearchPlaceView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by sofiane on 12/16/16.
 */

public class SearchPresenter {


    private static final String TAG = SearchPresenter.class.getName();
    @Inject
    PlacesService placesService;
    @Inject
    LocationProvider provider;

    Activity activity; // used to return result

    private Subscription subscription;
    private SearchPlaceView view;
    private String bbox;

    public SearchPresenter(SearchPlaceView view) {
        this.view = view;
        activity = (Activity) view;
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

        subscription = placesService.autoSuggest(bbox, query, "plain", 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(autoSuggestResult -> {
                            view.showSuggestions(autoSuggestResult.getResults());
                            view.showSuggestionProgress(false);
                        },
                        error -> Log.e(TAG, "Error loading suggestions", error));

    }

    public void onPlaceClicked(Place place) {
        retrunPlace(place);
    }

    public void retrunPlace(Place place) {
        Intent intent = activity.getIntent();
        intent.putExtra(SearchActivity.PLACE, place);
        activity.setResult(RESULT_OK, intent);
        activity.finish();
    }

    private void showError(Throwable error) {
        Log.e(TAG, "Error search", error);
        view.showProgress(false);
        view.showMessage("Error searching places", R.drawable.ic_error);
    }

    public void search(String query) {
        if (subscription != null && subscription.isUnsubscribed())
            subscription.unsubscribe();
        view.showProgress(true);

        subscription = placesService.searchPlace(bbox, query, "plain")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placesResult -> {
                            view.showProgress(false);
                            if (placesResult.getPlaces() != null && placesResult.getPlaces().length > 0)
                                view.setItems(placesResult.getPlaces());
                            else
                                view.showMessage("No Places Found for this query", R.drawable.ic_not_interested);
                        },
                        error -> {
                            view.showProgress(false);
                            showError(error);
                        });
    }

    public void onLocationClicked() {
        if (provider.getLocation() != null) {
            Place p = new Place("Your Position", provider.getLocation());
            retrunPlace(p);
        }
    }

    public void onStop() {
        subscription.unsubscribe();
    }
}
