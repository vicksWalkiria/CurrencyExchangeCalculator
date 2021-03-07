package com.walkiriaapps.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.walkiriaapps.data.retrofit.APIClient;
import com.walkiriaapps.data.retrofit.CurrencyValuesService;
import com.walkiriaapps.domain.CurrencyModel;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Retrofit;

public class CurrencyDataSource {
    private final Retrofit retrofit;
    static final String BASE_URL = "https://api.exchangeratesapi.io/";


    @Inject
    public CurrencyDataSource(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    //Devolver CurrencyModel (puede que se necesite un mapper)
    public CurrencyModel provideGetCurrencyData() {

        CurrencyModel model;
        Retrofit retrofit = APIClient.getClient(BASE_URL);
        CurrencyValuesService CERApi = retrofit.create(CurrencyValuesService.class);
        Call<JsonObject> call = CERApi.getExchangeRate("USD");
        try {
            String result = call.execute().body().toString();
            model = new Gson().fromJson(result, CurrencyModel.class);
        } catch (Exception e) {
            model = null;
        }
        return model;
    }
}
