package io.sodaoud.heretest.app.test;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.sodaoud.heretest.app.network.PlacesService;
import io.sodaoud.heretest.app.network.RouteService;

/**
 * Created by sofiane on 12/12/16.
 */
@Module
public class TestNetworkModule {

//    @Provides
//    @Singleton
//    public Gson providesGson() {
//        GsonBuilder builder = new GsonBuilder();
//        return builder.create();
//    }

//    @Provides
//    @Singleton
//    // TODO make sure it does not affect using the same client with multiple retrofit adapters
//    @Named("HEREApi")
//    public OkHttpClient providesOkHttpClient(LocationProvider provider) {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.addInterceptor(chain -> {
//            Request request = chain.request().newBuilder().addHeader("Authorization", "Basic " + BuildConfig.auth)
//                    .addHeader("Geolocation", Util.getPositionFormat(provider.getLocation())).build();
//            return chain.proceed(request);
//        });
//        return builder.build();
//    }

//    @Provides
//    @Singleton
//    @Named("Places")
//    public Retrofit providesPlacesRetrofit(Gson gson, @Named("HEREApi") OkHttpClient client) {
//        return new Retrofit.Builder()
//                .baseUrl(Constants.getPlacesApiEndpoint())
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//    }
//
//    @Provides
//    @Singleton
//    @Named("Route")
//    public Retrofit providesRouteRetrofit(Gson gson, @Named("HEREApi") OkHttpClient client) {
//        return new Retrofit.Builder()
//                .baseUrl(Constants.getRouteApiEndpoint())
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//    }

    @Provides
    @Singleton
    public PlacesService providesPlacesService() {
        return Mockito.mock(PlacesService.class);
    }

    @Provides
    @Singleton
    public RouteService providesRouteService() {
        return Mockito.mock(RouteService.class);
    }
}
