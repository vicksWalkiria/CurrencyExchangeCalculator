package com.walkiriaapps.currencyexchangecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import dagger.hilt.android.AndroidEntryPoint;

import static android.view.View.GONE;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        showProgressBar(true);

        //Listen to API
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.currencyValues.observe(this, o -> {
            if (o != null) {
                boolean result = (boolean) o;
                //Hide progressBar
              //  showProgressBar(false);
                if(result){
                    showProgressBar((boolean)o);
                }
                else
                {
                    displayMessage(getString(R.string.error_retrieving_values));
                }
            }
        });

        mainViewModel.loadData();
    }

    public void displayMessage(String s) {

    }

    public void showProgressBar(boolean show){
        progressBar.setVisibility( show ? View.VISIBLE : GONE);
    }
}