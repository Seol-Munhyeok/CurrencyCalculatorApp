package kr.pknu.seolmunhyeok201911938;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kr.pknu.seolmunhyeok201911938.api.CurrencyApiService;
import kr.pknu.seolmunhyeok201911938.api.RetrofitClient;
import kr.pknu.seolmunhyeok201911938.model.CurrencyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_COUNTRY = 1;  // 다른 액티비티로 이동할 때 요청 구분용
    private int selectedCountryIndex = -1;
    private int lastSelectedCountryIndex = -1;
    private Map<String, Double> exchangeRates = new HashMap<>();

    private StringBuilder currentInput = new StringBuilder();
    private TextView currentTimeText;
    private TextView[] currencyOutputTextViews = new TextView[3];
    private TextView[] amountTextViewKoreanTextViews = new TextView[3];
    private TextView[] countryTextViews = new TextView[3];
    private ImageView[] flagImageViews = new ImageView[3];

    private String[] defaultCountries = {"KRW", "USD", "JPY"};
    private int[] defaultFlags = {
            R.drawable.ic_flag_krw,
            R.drawable.ic_flag_usd,
            R.drawable.ic_flag_jpy
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTimeText = findViewById(R.id.currentTimeText);

        for (int i = 0; i < countryTextViews.length; i++) {
            int textResId = getResources().getIdentifier("currencyTextView" + (i + 1), "id", getPackageName());
            int imageResId = getResources().getIdentifier("flagImageView" + (i + 1), "id", getPackageName());
            int outputResId = getResources().getIdentifier("currencyOutput" + (i + 1), "id", getPackageName());
            int koreanResId = getResources().getIdentifier("amountTextViewKorean" + (i + 1), "id", getPackageName());

            countryTextViews[i] = findViewById(textResId);
            flagImageViews[i] = findViewById(imageResId);
            currencyOutputTextViews[i] = findViewById(outputResId);
            amountTextViewKoreanTextViews[i] = findViewById(koreanResId);

            countryTextViews[i].setText(defaultCountries[i]);
            flagImageViews[i].setImageResource(defaultFlags[i]);

            int finalI = i;
            countryTextViews[i].setOnClickListener(view -> openCountrySelectionActivity(finalI));

            currencyOutputTextViews[i].setOnClickListener(view -> {
                selectedCountryIndex = finalI;
                currentInput.setLength(0);
            });
        }

        for (int i = 0; i < currencyOutputTextViews.length; i++) {
            int finalI = i;
            currencyOutputTextViews[i].setOnClickListener(view -> {
                if (lastSelectedCountryIndex != -1) {
                    currencyOutputTextViews[lastSelectedCountryIndex].setBackgroundResource(0);
                }

                selectedCountryIndex = finalI;
                lastSelectedCountryIndex = finalI;

                currencyOutputTextViews[finalI].setBackgroundResource(R.drawable.yellow_border);

                currentInput.setLength(0);
            });

            countryTextViews[i].setOnClickListener(view -> openCountrySelectionActivity(finalI));
        }
        fetchExchangeRates();
        setupCalculatorButtons();
    }

    private void initViews() {

    }

    private static final long MAX_INPUT_VALUE = 9_999_999_999_999L;
    private void setupCalculatorButtons() {
        int[] buttonIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9, R.id.button00, R.id.buttonClear,
                R.id.buttonDelete
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(view -> {
                if (selectedCountryIndex == -1) return;

                Button button = (Button) view;
                String buttonText = button.getText().toString();

                if (buttonText.equals("C")) {
                    currentInput.setLength(0);
                    currencyOutputTextViews[selectedCountryIndex].setText("0");
                } else if (buttonText.equals("←")) {
                    if (currentInput.length() > 0) {
                        currentInput.deleteCharAt(currentInput.length() - 1);
                    } else {
                        currencyOutputTextViews[selectedCountryIndex].setText("0");
                    }
                } else if (buttonText.equals("00")) {
                    currentInput.append("00");
                    currencyOutputTextViews[selectedCountryIndex].setText("0");
                }
                else {
                    if (currentInput.length() > 0) {
                        long currentValue = Long.parseLong(currentInput.toString());
                        if (currentValue > MAX_INPUT_VALUE) {
                            Toast.makeText(
                                    this,
                                    "입력 값이 너무 큽니다!",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }
                    }
                    currentInput.append(buttonText);
                }

                String inputText = currentInput.toString();
                currencyOutputTextViews[selectedCountryIndex].setText(inputText.isEmpty() ? "0" : inputText);

                updateExchangeRates();
            });
        }
    }

    private void updateExchangeRates() {
        if (selectedCountryIndex == -1 || currentInput.length() == 0 || currentInput.toString().equals("0")) {
            for (int i = 0; i < amountTextViewKoreanTextViews.length; i++) {
                currencyOutputTextViews[i].setText("0");
                amountTextViewKoreanTextViews[i].setText(convertToKorean(0, countryTextViews[i].getText().toString()));
            }
            return;
        }

        double baseAmount = Double.parseDouble(currentInput.toString());
        String baseCurrency = countryTextViews[selectedCountryIndex].getText().toString();

        for (int i = 0; i < countryTextViews.length; i++) {
            String targetCurrency = countryTextViews[i].getText().toString();
            if (i == selectedCountryIndex) {
                amountTextViewKoreanTextViews[i].setText(convertToKorean((long) baseAmount, targetCurrency));
                continue;
            }
            double rate = exchangeRates.get(targetCurrency) / exchangeRates.get(baseCurrency);
            double convertedAmount = baseAmount * rate;

            currencyOutputTextViews[i].setText(String.format(Locale.getDefault(), "%.2f", convertedAmount));
            amountTextViewKoreanTextViews[i].setText(convertToKorean((long) convertedAmount, targetCurrency));
        }
    }

    private void openCountrySelectionActivity(int countryIndex) {
        Intent intent = new Intent(this, CountrySelectionActivity.class);
        intent.putExtra("countryIndex", countryIndex);
        startActivityForResult(intent, REQUEST_CODE_COUNTRY);
    }


    private void fetchExchangeRates() {
        CurrencyApiService apiService = RetrofitClient.getClient().create(CurrencyApiService.class);
        String apiKey = BuildConfig.API_KEY;

        apiService.getLatestRates(apiKey).enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CurrencyResponse rates = response.body();

                    exchangeRates.clear();
                    exchangeRates.putAll(rates.data);

                    // 각 텍스트뷰에 데이터 반영
                    for (int i = 0; i < countryTextViews.length; i++) {
                        String selectedCurrency = countryTextViews[i].getText().toString();
                        if (rates.data.containsKey(selectedCurrency)) {
                            double rate = rates.data.get(selectedCurrency);

                            double baseAmount = 1.0;
                            double convertedAmount = baseAmount * rate;

                            currencyOutputTextViews[i].setText(String.format(Locale.getDefault(), "%.2f", convertedAmount));

                            amountTextViewKoreanTextViews[i].setText(convertToKorean((long) convertedAmount, selectedCurrency));
                        }
                    }

                    String formattedTime = getCurrentTime();
                    currentTimeText.setText(formattedTime);

                } else {
                    currentTimeText.setText("환율 데이터를 가져오지 못했습니다.");
                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                currentTimeText.setText("네트워크 오류");
            }
        });
    }

    private String convertToKorean(long value, String currencyCode) {
        if (value == 0) {
            return "0";
        }

        String unit;
        switch (currencyCode) {
            case "KRW": unit = "대한민국 원"; break;
            case "USD": unit = "미국 달러"; break;
            case "JPY": unit = "일본 엔"; break;
            case "EUR": unit = "유로"; break;
            case "GBP": unit = "파운드 스털링"; break;
            case "CNY": unit = "중국 위안화"; break;
            case "AUD": unit = "호주 달러"; break;
            case "CAD": unit = "캐나다 달러"; break;
            case "CHF": unit = "스위스 프랑"; break;
            case "NZD": unit = "뉴질랜드 달러"; break;
            case "SGD": unit = "싱가포르 달러"; break;
            case "THB": unit = "태국 바트"; break;
            case "HKD": unit = "홍콩 달러"; break;
            case "PHP": unit = "필리핀 페소"; break;
            case "MYR": unit = "말레이시아 링깃"; break;
            case "IDR": unit = "인도네시아 루피아"; break;
            case "RUB": unit = "러시아 루블"; break;
            case "INR": unit = "인도 루피"; break;
            case "ZAR": unit = "남아프리카공화국 랜드"; break;
            case "CZK": unit = "체코 코루나"; break;

            default: unit = "단위"; break;
        }

        StringBuilder result = new StringBuilder();
        String[] units = {"", "만 ", "억 ", "조 "};
        int unitIndex = 0;

        while (value > 0) {
            long part = value % 10000;
            if (part > 0) {
                result.insert(0, part + units[unitIndex]);
            }
            value /= 10000;
            unitIndex++;
        }

        return result.append(" " + unit).toString();
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return "업데이트: " + formatter.format(new Date());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_COUNTRY && resultCode == RESULT_OK && data != null) {
            int countryIndex = data.getIntExtra("countryIndex", -1);
            String selectedCountry = data.getStringExtra("selectedCountry");
            int selectedFlag = data.getIntExtra("selectedFlag", R.drawable.ic_flag_placeholder);

            if (countryIndex >= 0) {
                countryTextViews[countryIndex].setText(selectedCountry);
                flagImageViews[countryIndex].setImageResource(selectedFlag);

                updateExchangeRates();
            }
        }
    }
}
