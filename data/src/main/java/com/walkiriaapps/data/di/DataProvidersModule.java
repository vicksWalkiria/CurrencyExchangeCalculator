package com.walkiriaapps.data.di;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public final class DataProvidersModule {

    @Provides
    static Retrofit provideRetrofit() {
        return  new Retrofit.Builder()
                .baseUrl("https://api.exchangeratesapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
