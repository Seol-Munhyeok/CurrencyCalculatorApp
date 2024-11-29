package kr.pknu.seolmunhyeok201911938;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import kr.pknu.seolmunhyeok201911938.api.CurrencyApiService;
import kr.pknu.seolmunhyeok201911938.api.RetrofitClient;
import kr.pknu.seolmunhyeok201911938.model.CurrencyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView currentTimeText;
    private Spinner currencySpinner;
    private EditText currencyInput;
    private Button buttonClear;
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTimeText = findViewById(R.id.currentTimeText);
        currencySpinner = findViewById(R.id.currencySpinner);
        currencyInput = findViewById(R.id.currencyInput);
        buttonClear = findViewById(R.id.buttonClear);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);

        buttonClear.setOnClickListener(view -> currencyInput.setText(""));

        fetchExchangeRates();
    }

    private void fetchExchangeRates() {
        CurrencyApiService apiService = RetrofitClient.getClient().create(CurrencyApiService.class);
        String apiKey = BuildConfig.API_KEY;

        apiService.getLatestRates(apiKey).enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CurrencyResponse rates = response.body();
                    currentTimeText.setText(rates.data.toString());
                }
            }
            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                currentTimeText.setText("ERROR");
            }
        });
    }
}