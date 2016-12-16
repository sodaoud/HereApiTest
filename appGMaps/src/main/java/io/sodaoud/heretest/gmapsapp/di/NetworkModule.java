package io.sodaoud.heretest.gmapsapp.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.sodaoud.heretest.gmapsapp.BuildConfig;
import io.sodaoud.heretest.gmapsapp.network.PlacesService;
import io.sodaoud.heretest.gmapsapp.network.RouteService;
import io.sodaoud.heretest.gmapsapp.util.Constants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sofiane on 12/12/16.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    public Gson providesGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    @Provides
    @Singleton // make sure it does not affect using the same with multiple retrofit adapters
    @Named("HEREApi")
    public OkHttpClient providesOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Authorization", "Basic " + BuildConfig.auth).build();
            return chain.proceed(request);
        });
        return builder.build();
    }


    @Provides
    @Singleton
    @Named("Places")
    public Retrofit providesPlacesRetrofit(Gson gson, @Named("HEREApi") OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Constants.PLACES_API_ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("Route")
    public Retrofit providesRouteRetrofit(Gson gson, @Named("HEREApi") OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Constants.ROUTE_API_ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public PlacesService providesPlacesService(@Named("Places") Retrofit retrofit) {
        return retrofit.create(PlacesService.class);
    }

    @Provides
    @Singleton
    public RouteService providesRouteService(@Named("Route") Retrofit retrofit) {
        return retrofit.create(RouteService.class);
    }
}
