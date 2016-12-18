package io.sodaoud.heretest.app;

import javax.inject.Singleton;

import dagger.Component;
import io.sodaoud.heretest.app.di.ApplicationComponent;
import io.sodaoud.heretest.app.test.TestModule;
import io.sodaoud.heretest.app.test.TestNetworkModule;

/**
 * Created by sofiane on 12/18/16.
 */
@Singleton
@Component(modules = {TestModule.class, TestNetworkModule.class})
public interface TestApplicationComponent extends ApplicationComponent {
    void inject(BaseTest baseTest);
}

