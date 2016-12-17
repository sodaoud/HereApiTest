package io.sodaoud.heretest.app.view;

import io.sodaoud.heretest.app.model.Route;

/**
 * Created by sofiane on 12/16/16.
 */

public interface RouteView extends ListView<Route> {

    void setFromText(String from);

    void setToText(String to);
}
