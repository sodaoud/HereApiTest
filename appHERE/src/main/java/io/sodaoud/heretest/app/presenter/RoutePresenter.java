package io.sodaoud.heretest.app.presenter;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import io.sodaoud.heretest.app.BuildConfig;
import io.sodaoud.heretest.app.R;
import io.sodaoud.heretest.app.di.ApplicationComponent;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.RouteResult;
import io.sodaoud.heretest.app.network.RouteService;
import io.sodaoud.heretest.app.util.Util;
import io.sodaoud.heretest.app.view.RouteView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sofiane on 12/16/16.
 */
public class RoutePresenter {
    private static final String TAG = RoutePresenter.class.getName();

    private Subscription subscription;

    public RoutePresenter(RouteView view) {
        this.view = view;
    }

    @Inject
    RouteService routeService;

    @Inject
    Context context;

    private RouteView view;

    private Place from;
    private Place to;

    private void calculateRoute() {
        if (subscription != null && subscription.isUnsubscribed())
            subscription.unsubscribe();

        if (from != null && to != null) {
            view.showProgress(true);

            subscription = routeService.calculateRoute(
                    BuildConfig.appId,
                    BuildConfig.appCode,
                    Util.getWaypointFromPlace(from),
                    Util.getWaypointFromPlace(to),
                    "fastest;car",
                    5,
                    "text",
                    "shape,labels,bb")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::showRoute,
                            this::showError);
        }
    }

    private void showRoute(RouteResult route) {
        view.showProgress(false);
        if (route.getRoutes() != null && route.getRoutes().length > 0)
            view.setItems(route.getRoutes());
        else
            view.showMessage("No Routes Found for this query", R.drawable.ic_not_interested);
    }

    private void showError(Throwable error) {
        Log.e(TAG, "Error calculate route", error);
        view.showProgress(false);
        view.showMessage("Error Calculating Route, please retry", R.drawable.ic_error);
    }

    public void setFrom(Place from) {
        if (from == null) {
            view.setFromText("");
            return;
        }
        this.from = from;
        view.setFromText(from.getTitle());
        calculateRoute();
    }

    public void setTo(Place to) {
        if (to == null) {
            view.setToText("");
            return;
        }
        this.to = to;
        view.setToText(to.getTitle());
        calculateRoute();
    }

    public void swapPlaces() {
        Place tmp = from;
        from = to;
        to = tmp;

        view.setFromText(from == null ? "" : from.getTitle());
        view.setToText(to == null ? "" : to.getTitle());

        calculateRoute();
    }

    public void onStop() {
        subscription.unsubscribe();
    }

    public void init(ApplicationComponent component) {
        component.inject(this);
    }
}
