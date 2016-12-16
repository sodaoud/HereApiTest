package io.sodaoud.heretest.app.di;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.sodaoud.heretest.app.HereTestApplication;
import io.sodaoud.heretest.app.location.LocationProvider;

/**
 * Created by sofiane on 12/12/16.
 */

@Module
public class AppModule {

    HereTestApplication mApplication;

    public AppModule(HereTestApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    HereTestApplication providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    LocationProvider providesLocationProvider(HereTestApplication application) {
        return new LocationProvider(application);
    }

    @Provides
    @Singleton
    GoogleApiClient providesGoogleApiClient(HereTestApplication application) {
        return new GoogleApiClient.Builder(application)
                .addApi(LocationServices.API)
                .build();
    }


}
