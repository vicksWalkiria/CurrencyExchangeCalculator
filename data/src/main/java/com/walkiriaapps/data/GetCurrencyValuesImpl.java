package com.walkiriaapps.data;

import com.walkiriaapps.domain.CurrencyModel;
import com.walkiriaapps.domain.GetCurrencyValues;
import javax.inject.Inject;


public class GetCurrencyValuesImpl implements GetCurrencyValues {

    private CurrencyDataSource currencyDataSource;

    @Inject
    public GetCurrencyValuesImpl(CurrencyDataSource currencyDataSource) {
        this.currencyDataSource = currencyDataSource;
    }

    @Override
    public CurrencyModel requestData() {
        return currencyDataSource.provideGetCurrencyData();
    }

    @Override
    public String[] requestCurrencies() {
        return currencyDataSource.provideCurrencies();
    }
}