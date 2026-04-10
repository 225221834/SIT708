package com.example.mytravelcompanion;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText currencyAmount;
    private Spinner convertFrom;
    private Spinner convertTo;

    @SuppressLint({"MissingInflatedId", "SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //currency implementations----------------------------------------------------------------
        currencyAmount = findViewById(R.id.getAmount);
        convertFrom = findViewById(R.id.spinFrom);
        convertTo = findViewById(R.id.spinTo);

        Button btnConvertCurrency = findViewById(R.id.btnConvertCurrency);
        String[] currencies = {"USD", "EUR", "GBP", "AUD", "JPY"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        convertFrom.setAdapter(adapter);
        convertTo.setAdapter(adapter);

        convertFrom.setSelection(0); // default USD
        convertTo.setSelection(1);   // default EUR
        btnConvertCurrency.setOnClickListener(v -> convert());

        //fuel efficiency & distance implementation------------------------------------------------

        Button btnConvertFuel = findViewById(R.id.btnFuel);
        //obtain results
        TextView fuelEfficiencyResult = findViewById(R.id.fuelResult);
        //get input
        TextView fuelDistance = findViewById(R.id.getDistance);
        Spinner convertFuel = findViewById(R.id.spinFuel);

        String[] fuelUnit = {"MPG to km/L", "Gallon to Litres","Nautical Miles to Kilometers"};

        //distance unit adapter
        ArrayAdapter<String> fuelUnitAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, fuelUnit);

        fuelUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        convertFuel.setAdapter(fuelUnitAdapter);

        btnConvertFuel.setOnClickListener(v -> {
            String fuelInput = fuelDistance.getText().toString().trim();
            if(fuelInput.isEmpty()){
                Toast.makeText(this, "Enter a value", Toast.LENGTH_SHORT).show();
                return;
            }
            double fuelValue = Double.parseDouble(fuelInput);
            int selection = convertFuel.getSelectedItemPosition();

            String result = "";
            switch (selection){
                case 0: // mpg to km/L
                    result=String.format("%.2f km/L", fuelValue * 0.425);
                    break;

                case 1: //US Gallon to Liters
                    result = String.format("%.2f Liters", fuelValue * 3.785);
                    break;
                case 2: //Nautical mile to Kilometer
                    result = String.format("%.2f km", fuelValue * 1.852);
                    break;
            }
            fuelEfficiencyResult.setText(result);
        });
        //Temperature conversion-------------------------------------------------------------------
        String[] temperature = {"Celsius to Fahrenheit", "Fahrenheit to Celsius", "Celsius to Kelvin"};
        Spinner spinTemp = findViewById(R.id.spinTemp);
        Button buttonTemp = findViewById(R.id.btnTemp);
        EditText editTemp = findViewById(R.id.editTemp);
        TextView txtTempResult = findViewById(R.id.tempResult);

        ArrayAdapter<String> tempAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, temperature);
        tempAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTemp.setAdapter(tempAdapter);

        buttonTemp.setOnClickListener(v -> {
            String input = editTemp.getText().toString().trim();
            if (input.isEmpty()) {
                //TempResult.setText("Enter temperature");
                Toast.makeText(this, "Enter a value", Toast.LENGTH_SHORT).show();
                return;
            }

            double tempResult = Double.parseDouble(input);
            int selected = spinTemp.getSelectedItemPosition();

            String result = "";
            switch (selected) {
                case 0: // Celsius to F
                    result = String.format("%.2f °F", (tempResult * 1.8) + 32);
                    break;
                case 1: // Fahrenheit to C
                    result = String.format("%.2f °C", (tempResult - 32) / 1.8);
                    break;
                case 2: // Celsius to K
                    result = String.format("%.2f K", tempResult + 273.15);
                    break;
            }
            txtTempResult.setText(result);
        });

    }

    //convert currency
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void convert() {
        String input = currencyAmount.getText().toString().trim();
        TextView currencyResult = findViewById(R.id.currencyResult);

        if (input.isEmpty()) {
            Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            currencyResult.setText("Invalid number");
            return;
        }

        String fromCurrency = convertFrom.getSelectedItem().toString();
        String toCurrency = convertTo.getSelectedItem().toString();

        double rateFrom = getCurrencyRate(fromCurrency);
        double rateTo = getCurrencyRate(toCurrency);

        //does the conversion
        double result = amount * (rateTo / rateFrom);
        currencyResult.setText(String.format("%.2f %s", result, toCurrency));
    }

    private double getCurrencyRate(String currency) {
        // conversion rates (base = USD)
        double USD = 1.0, EUR = 0.92, GBP = 0.78,
                AUD = 1.55, JPY = 148.50;

        switch (currency) {
            case "USD":
                return USD;
            case "EUR":
                return EUR;
            case "GBP":
                return GBP;
            case "AUD":
                return AUD;
            case "JPY":
                return JPY;
            default: return 1.0;
        }
    }
}