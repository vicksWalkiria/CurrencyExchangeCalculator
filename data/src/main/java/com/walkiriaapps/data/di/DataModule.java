package com.walkiriaapps.data.di;

import com.walkiriaapps.data.GetCurrencyValuesImpl;
import com.walkiriaapps.domain.GetCurrencyValues;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class DataModule {
    @Binds
    abstract GetCurrencyValues bindsGetData(GetCurrencyValuesImpl implementation);
}

