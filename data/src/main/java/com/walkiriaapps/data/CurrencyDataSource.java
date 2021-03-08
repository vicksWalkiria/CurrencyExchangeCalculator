package com.walkiriaapps.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.walkiriaapps.data.retrofit.CurrencyValuesService;
import com.walkiriaapps.domain.CurrencyModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Call;
import retrofit2.Retrofit;

public class CurrencyDataSource {
    private static final String CURRENCY_RESULTS = "currencyResults";
    private final Retrofit retrofit;
    private final @ApplicationContext
    Context app;
    private SharedPreferences prefs;


    @Inject
    public CurrencyDataSource(Retrofit retrofit, @ApplicationContext Context app) {
        this.retrofit = retrofit;
        this.app = app;
        prefs = app.getSharedPreferences(app.getPackageName(), Context.MODE_PRIVATE);
    }


    //Devolver CurrencyModel (puede que se necesite un mapper)
    public CurrencyModel provideGetCurrencyData() {
        CurrencyModel model;
        try {
            if(isThereAnActiveInternetConnection()) {
                CurrencyValuesService CERApi = retrofit.create(CurrencyValuesService.class);
                Call<JsonObject> call = CERApi.getExchangeRate("USD");
                try {
                    String result = call.execute().body().toString();
                    prefs.edit().putString(CURRENCY_RESULTS, result).commit();
                    model = new Gson().fromJson(result, CurrencyModel.class);
                } catch (Exception e) {
                    model = null;
                }
            }
            else
            {
              model = getModelFromPrefs();
            }
        } catch (InterruptedException | IOException e1){
            model = getModelFromPrefs();
        }
        return model;
    }

    private CurrencyModel getModelFromPrefs() {
        String provModel = prefs.getString(CURRENCY_RESULTS, "");
        if(provModel.length()>1) {
            return new Gson().fromJson(prefs.getString(CURRENCY_RESULTS, ""), CurrencyModel.class);
        }
        else
        {
            return null;
        }
    }

    public String[] provideCurrencies() {
        String[] result = {};
        try {
            JSONObject object = new JSONObject(prefs.getString(CURRENCY_RESULTS, ""));
            JSONObject currencies = object.getJSONObject("rates");
            result = new String[currencies.length()];
            Iterator<String> keys = currencies.keys();
            int i = 0;
            while (keys.hasNext()) {
                result[i] = keys.next();
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Arrays.sort(result);
        return result;
    }

    public boolean isThereAnActiveInternetConnection() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
}
