package io.sodaoud.heretest.app.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

import javax.inject.Inject;

import io.sodaoud.heretest.app.R;
import io.sodaoud.heretest.app.di.ApplicationComponent;
import io.sodaoud.heretest.app.location.LocationProvider;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.Route;
import io.sodaoud.heretest.app.network.PlacesService;
import io.sodaoud.heretest.app.ui.MainActivity;
import io.sodaoud.heretest.app.ui.RouteActivity;
import io.sodaoud.heretest.app.view.MapView;
import io.sodaoud.heretest.app.view.SearchPlaceView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sofiane on 12/16/16.
 */

public class MapPresenter implements LocationListener {

    private static final String TAG = MapPresenter.class.getName();

    private Subscription subscription;
    private String bbox;
    private Place currentSearch;

    private MapView view;
    private SearchPlaceView searchView;

    @Inject
    LocationProvider provider;

    @Inject
    PlacesService placesService;

    public MapPresenter(MapView view) {
        this.view = view;
    }

    public void setSearchView(SearchPlaceView searchView) {
        this.searchView = searchView;
    }

    public void initLocation() {
        provider.connect();
        provider.addListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        view.showPosition(location);
    }

    public void onResultRoute(Route route) {
        view.removeAllObjects();
        view.showRoute(route, Color.BLUE);
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
                            searchView.showSuggestions(autoSuggestResult.getResults());
                            searchView.showSuggestionProgress(false);
                        },
                        error -> Log.e(TAG, "Error loading suggestions", error));

    }

    public void onPlaceClicked(Place place) {
        currentSearch = place;
        view.removeAllObjects();
        view.showPlace(place);
    }

    private void showError(Throwable error) {
        Log.e(TAG, "Error search", error);
        searchView.showProgress(false);
        searchView.showMessage("Error searching places", R.drawable.ic_error);
    }

    public void search(String query) {
        if (subscription != null && subscription.isUnsubscribed())
            subscription.unsubscribe();
        searchView.showProgress(true);

        subscription = placesService.searchPlace(bbox, query, "plain")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placesResult -> {
                            searchView.showProgress(false);
                            if (placesResult.getPlaces() != null && placesResult.getPlaces().length > 0)
                                onPlaceClicked(placesResult.getPlaces()[0]);
                            else
                                searchView.showMessage("No Places Found for this query", R.drawable.ic_not_interested);
                        },
                        error -> {
                            searchView.showProgress(false);
                            showError(error);
                        });
    }

    public void onLocationClicked() {
        if (provider.getLocation() != null) {
        }
    }

    public void onStop() {
        if (subscription != null)
            subscription.unsubscribe();
    }

    public void onDestroy() {
        provider.disconnect();
    }

    public void onDirectionClicked(Activity activity) {
        Intent i = new Intent(activity, RouteActivity.class);
        i.putExtra(MainActivity.BBOX, bbox);
        i.putExtra(RouteActivity.DESTINATION, currentSearch);
        activity.startActivityForResult(i, MainActivity.DIRECTION_REQ);
    }
}
