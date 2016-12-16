package io.sodaoud.heretest.gmapsapp;

import android.app.Application;

import io.sodaoud.heretest.gmapsapp.di.AppModule;
import io.sodaoud.heretest.gmapsapp.di.ApplicationComponent;
import io.sodaoud.heretest.gmapsapp.di.DaggerApplicationComponent;

/**
 * Created by sofiane on 12/12/16.
 */

public class HereTestApplication extends Application {


    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder().appModule(new AppModule(this)).build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
