package io.sodaoud.heretest.app.di;


import javax.inject.Singleton;

import dagger.Component;
import io.sodaoud.heretest.app.ui.MainActivity;
import io.sodaoud.heretest.app.ui.RouteActivity;
import io.sodaoud.heretest.app.ui.SearchActivity;

/**
 * Created by sofiane on 12/12/16.
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(RouteActivity activity);

    void inject(SearchActivity searchActivity);

    void inject(MainActivity mapsActivity);
}

