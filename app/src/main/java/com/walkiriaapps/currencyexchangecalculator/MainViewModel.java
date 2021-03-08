package com.walkiriaapps.currencyexchangecalculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.walkiriaapps.domain.CurrencyModel;
import com.walkiriaapps.domain.GetCurrencyValues;
import com.walkiriaapps.domain.Rates;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private GetCurrencyValues getCurrencyValues;
    private MutableLiveData _currencyValues = new MutableLiveData();
    public LiveData currencyValues = _currencyValues;

    private CurrencyModel currencyModel;
    private double baseRate;
    private double destinationRate;

    @Inject
    public MainViewModel(GetCurrencyValues getCurrencyValues) {
        this.getCurrencyValues = getCurrencyValues;
    }

    public void loadData() {
        new Thread(() -> {
            this.currencyModel = getCurrencyValues.requestData();
            if (currencyModel != null)
                _currencyValues.postValue(true);
            else {
                _currencyValues.postValue(false);
            }
        }).start();
    }

    public String[] getCurrencies() {
        return getCurrencyValues.requestCurrencies();
    }

    public String calculateValue(String quantityString, String from, String to) {

        double quantity = Double.parseDouble(quantityString);
        //GET SELECTED CURRENCY IN DOLLARS
        try {
            Rates rates = currencyModel.getRates();
            Method method = currencyModel.getRates().getClass().getMethod("get" + from);
            baseRate = (double) method.invoke(rates);
            double baseDollars = quantity / baseRate;
            Method method2 = currencyModel.getRates().getClass().getMethod("get" + to);
            destinationRate = (double) method2.invoke(rates);
            double result = baseDollars * destinationRate;

            DecimalFormat f = new DecimalFormat("#,##0.00");

            return String.valueOf(f.format(result) + " " + to);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public String getDateOfLastUpdate() {
        return currencyModel.getDate();
    }
    
    public String getBaseCurrency()
    {
        DecimalFormat f = new DecimalFormat("#,##0.00");
        return f.format((1/baseRate) * destinationRate);
    }
}
