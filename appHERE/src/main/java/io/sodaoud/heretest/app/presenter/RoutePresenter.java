package io.sodaoud.heretest.app.presenter;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import io.sodaoud.heretest.app.BuildConfig;
import io.sodaoud.heretest.app.di.ApplicationComponent;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.RouteResult;
import io.sodaoud.heretest.app.network.RouteService;
import io.sodaoud.heretest.app.util.Util;
import io.sodaoud.heretest.app.view.RouteView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by sofiane on 12/16/16.
 */
public class RoutePresenter {

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
        if (from != null && to != null) {
            view.showProgress(true);
            routeService.calculateRoute(
                    BuildConfig.appId,
                    BuildConfig.appCode,
                    Util.getPlace(from),
                    Util.getPlace(to),
                    "fastest;car",
                    5,
                    "text",
                    "shape,labels,bb").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(route -> showRoute(route),
                            error -> showError(error));
        }
    }

    private void showRoute(RouteResult route) {
        view.showProgress(false);
        view.setItems(route.getRoutes());
    }

    private void showError(Throwable error) {
        Log.e(TAG, "Error calculate route", error);
        view.showProgress(false);
        view.showError("Error Calculating Route, please retry");
    }

    public void setFrom(Place from) {
        this.from = from;
        view.setFromText(from.getTitle());
        calculateRoute();
    }

    public void setTo(Place to) {
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

    public void init(ApplicationComponent component) {
        component.inject(this);
    }
}
