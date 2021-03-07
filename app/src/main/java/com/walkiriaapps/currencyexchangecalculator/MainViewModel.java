package com.walkiriaapps.currencyexchangecalculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.walkiriaapps.domain.CurrencyModel;
import com.walkiriaapps.domain.GetCurrencyValues;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private GetCurrencyValues getCurrencyValues;
    private MutableLiveData _currencyValues = new MutableLiveData();
    public LiveData currencyValues = _currencyValues;

    private CurrencyModel currencyModel;

    @Inject
    public MainViewModel(GetCurrencyValues getCurrencyValues) {
        this.getCurrencyValues = getCurrencyValues;
    }

    public void loadData() {
        new Thread(() -> {
            this.currencyModel = getCurrencyValues.requestData();

            if(currencyModel != null)
                _currencyValues.postValue(false);
            else
            {
                _currencyValues.postValue(true);
            }
        }).run();

    }

}
