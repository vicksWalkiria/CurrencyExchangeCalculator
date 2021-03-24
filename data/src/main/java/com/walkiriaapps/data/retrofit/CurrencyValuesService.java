package com.walkiriaapps.data.retrofit;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyValuesService {
    @GET("rates.txt")
    Call<JsonObject> getExchangeRate(@Query("base") String base);
}
