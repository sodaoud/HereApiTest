package io.sodaoud.heretest.app;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;

import io.sodaoud.heretest.app.di.ApplicationComponent;
import io.sodaoud.heretest.app.network.PlacesService;
import io.sodaoud.heretest.app.network.RouteService;
import io.sodaoud.heretest.app.test.TestModule;
import io.sodaoud.heretest.app.util.RxSchedulersOverrideRule;

@RunWith(MockitoJUnitRunner.class)
public class BaseTest {

    protected ApplicationComponent component;

    @Inject
    public PlacesService placesService;

    @Inject
    public RouteService routeService;

    @Mock
    HereTestApplication application;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() throws Exception {
        component = DaggerTestApplicationComponent.builder()
                .testModule(new TestModule(application))
                .build();

        ((TestApplicationComponent) component).inject(this);
    }

}