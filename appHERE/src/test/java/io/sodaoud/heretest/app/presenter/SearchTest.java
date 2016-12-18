package io.sodaoud.heretest.app.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.sodaoud.heretest.app.BaseTest;
import io.sodaoud.heretest.app.model.PlaceResult;
import io.sodaoud.heretest.app.model.SearchResult;
import io.sodaoud.heretest.app.view.SearchPlaceView;
import rx.Observable;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sofiane on 12/18/16.
 */

public class SearchTest extends BaseTest {


    @Mock
    SearchPlaceView view;

    SearchPresenter presenter;


    String query;
    String query2;
    SearchResult result;
    PlaceResult[] places;

    @Before
    public void createPresenter() {
        presenter = new SearchPresenter(view);
        presenter.init(component);
        result = mock(SearchResult.class);
        places = new PlaceResult[]{mock(PlaceResult.class)};

        when(result.getPlaces()).thenReturn(places);
        query = "query";


        when(placesService.searchPlace(
                Mockito.any()
                , Mockito.any()))
                .thenReturn(Observable.error(new Exception("Error")));

        when(placesService.searchPlace(
                Mockito.any()
                , eq(query)))
                .thenReturn(Observable.just(result));


        when(placesService.searchPlace(
                Mockito.any()
                , eq(query2)))
                .thenReturn(Observable.just(mock(SearchResult.class)));

    }

    @Test
    public void testCase() {
        presenter.search(query);
        verify(view).setItems(places);

        presenter.search(query2);
        verify(view).showMessage(anyString(), anyInt());
    }
}
