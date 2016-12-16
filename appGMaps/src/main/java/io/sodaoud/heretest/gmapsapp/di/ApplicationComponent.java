package io.sodaoud.heretest.gmapsapp.di;

import javax.inject.Singleton;

import dagger.Component;
import io.sodaoud.heretest.gmapsapp.ui.RouteActivity;
import io.sodaoud.heretest.gmapsapp.ui.MapActivity;
import io.sodaoud.heretest.gmapsapp.ui.SearchActivity;

/**
 * Created by sofiane on 12/12/16.
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(RouteActivity activity);

    void inject(SearchActivity searchActivity);

    void inject(MapActivity mapsActivity);
}

