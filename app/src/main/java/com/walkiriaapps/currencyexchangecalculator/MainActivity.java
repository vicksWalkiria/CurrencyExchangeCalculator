package com.walkiriaapps.currencyexchangecalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

import static android.view.View.GONE;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private ProgressBar progressBar;
    private EditText quantityEditText;
    private Spinner spinnerFrom, spinnerTo;
    private Button calculateButton;
    private TextView resultTextView, poweredBy, contactMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        //Listen to API
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.currencyValues.observe(this, o -> {
            if (o != null) {
                boolean result = (boolean) o;
                if (!result) {
                    displayMessage(getString(R.string.error_retrieving_values));
                } else {
                    initializeUI();
                    showProgressBar(false);
                }
            }
        });

        mainViewModel.loadData();
    }

    private void initializeUI() {
        progressBar = findViewById(R.id.progressBar);
        quantityEditText = findViewById(R.id.editTextQuantity);
        spinnerFrom = findViewById(R.id.fromSpinner);
        spinnerTo = findViewById(R.id.toSpinner);
        calculateButton = findViewById(R.id.buttonConvert);
        resultTextView = findViewById(R.id.result);
        poweredBy = findViewById(R.id.powered_by);
        contactMe = findViewById(R.id.contactMe);

        calculateButton = findViewById(R.id.buttonConvert);
        initializeSpinners();
        initializeButtonLogic();

        poweredBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://walkiriaapps.com"));
                startActivity(browserIntent);
            }
        });

        contactMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"walkiriaappsdevelopment@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Currency Exchange Calculator");
                email.putExtra(Intent.EXTRA_TEXT, "");

                startActivity(Intent.createChooser(email, ""));
            }
        });
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDatedate = dateFormat.format(new Date());
            Log.d("WALKIRIA", "currentDatedate: " +currentDatedate + " DATE2: " + mainViewModel.getDateOfLastUpdate());
            if (!mainViewModel.getDateOfLastUpdate().equals(currentDatedate)) {
                displayMessage(getString(R.string.outdated_information, mainViewModel.getDateOfLastUpdate()));
            }

    }

    private void initializeButtonLogic() {
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = quantityEditText.getText().toString();
                if (quantity.length() < 1) {
                    displayMessage(getString(R.string.invalid_value));
                } else {
                    String resultOperation = mainViewModel.calculateValue(quantityEditText.getText().toString().replace(",", "."),
                            (String) spinnerFrom.getSelectedItem(), (String) spinnerTo.getSelectedItem());
                    String stringToShow = getString(R.string.results, quantity, spinnerFrom.getSelectedItem(), resultOperation, mainViewModel.getDateOfLastUpdate(),
                            spinnerFrom.getSelectedItem(), String.valueOf(mainViewModel.getBaseCurrency()), spinnerTo.getSelectedItem());
                    resultTextView.setText(stringToShow);
                }
            }
        });
    }

    private void initializeSpinners() {
        String[] currencies = mainViewModel.getCurrencies();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, currencies);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        spinnerFrom.setSelection(Arrays.asList(currencies).indexOf("USD"));
        spinnerTo.setSelection(Arrays.asList(currencies).indexOf("EUR"));
    }

    public void displayMessage(String s) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.error))
                .setMessage(s)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (s.equals(getString(R.string.error_retrieving_values))) {
                            finish();
                        }
                    }
                })
                .show();
    }

    public void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : GONE);
    }
}