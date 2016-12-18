package io.sodaoud.heretest.app.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.sodaoud.heretest.app.BaseTest;
import io.sodaoud.heretest.app.model.Place;
import io.sodaoud.heretest.app.model.Route;
import io.sodaoud.heretest.app.model.RouteResult;
import io.sodaoud.heretest.app.util.Util;
import io.sodaoud.heretest.app.view.RouteView;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sofiane on 12/18/16.
 */

public class RouteTest extends BaseTest {

    @Mock
    RouteView view;

    RoutePresenter presenter;


    Place from;
    Place first;
    RouteResult result;
    Route[] actualResult;
    Route route;
    Place second;

    @Before
    public void createPresenter() {
        route = Mockito.mock(Route.class);
        actualResult = new Route[]{route};
        from = new Place("From", 1d, 1d);
        first = new Place("To", 2d, 2d);
        second = new Place("To", 3d, 3d);
        result = Mockito.mock(RouteResult.class);
        when(result.getRoutes()).thenReturn(actualResult);

        presenter = new RoutePresenter(view);
        presenter.init(component);

        when(routeService.calculateRoute(
                Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()))
                .thenReturn(Observable.<RouteResult>error(new Exception()));

        when(routeService.calculateRoute(
                Mockito.any()
                , Mockito.any()
                , eq(Util.getWaypointFromPlace(from))
                , eq(Util.getWaypointFromPlace(first))
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()))
                .thenReturn(Observable.just(result));


        when(routeService.calculateRoute(
                Mockito.any()
                , Mockito.any()
                , eq(Util.getWaypointFromPlace(from))
                , eq(Util.getWaypointFromPlace(second))
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()))
                .thenReturn(Observable.just(mock(RouteResult.class)));
    }

    @Test
    public void testBestCase() {

        presenter.setFrom(from);

        verify(view).setFromText(from.getTitle());

        verify(routeService, never()).calculateRoute(
                Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any());

        verify(view, never()).setItems(actualResult);

        presenter.setTo(first);

        verify(view).setToText(first.getTitle());

        verify(routeService, times(1)).calculateRoute(
                Mockito.any()
                , Mockito.any()
                , eq(Util.getWaypointFromPlace(from))
                , eq(Util.getWaypointFromPlace(first))
                , Mockito.any()
                , Mockito.any()
                , Mockito.any()
                , Mockito.any());

        verify(result, atLeastOnce()).getRoutes();
        verify(view).setItems(actualResult);

    }


    @Test
    public void testEmptyCase() {

        presenter.setFrom(from);
        presenter.setTo(second);

        verify(view, never()).setItems(any());
        verify(view).showMessage(any(), anyInt());
    }


    @Test
    public void testErrorCase() {

        presenter.setFrom(from);
        presenter.setTo(new Place("TEST", 0d, 0d));

        verify(view, never()).setItems(any());
        verify(view).showMessage(any(), anyInt());

    }
}
