package io.sodaoud.heretest.app.test;

import android.content.Context;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.sodaoud.heretest.app.location.LocationProvider;

/**
 * Created by sofiane on 12/12/16.
 */

@Module
public class TestModule {

    Context mApplication;

    public TestModule(Context application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    LocationProvider providesLocationProvider() {
        return Mockito.mock(LocationProvider.class);
    }

}
