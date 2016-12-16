package io.sodaoud.heretest.app.di;


import javax.inject.Singleton;

import dagger.Component;
import io.sodaoud.heretest.app.presenter.MapPresenter;
import io.sodaoud.heretest.app.presenter.RoutePresenter;
import io.sodaoud.heretest.app.presenter.SearchPresenter;
import io.sodaoud.heretest.app.ui.SearchActivity;

/**
 * Created by sofiane on 12/12/16.
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(RoutePresenter presenter);

    void inject(SearchPresenter searchActivity);

    void inject(MapPresenter mapPresenter);
}

