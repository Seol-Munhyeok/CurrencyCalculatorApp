package kr.pknu.seolmunhyeok201911938;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kr.pknu.seolmunhyeok201911938.api.CurrencyApiService;
import kr.pknu.seolmunhyeok201911938.api.RetrofitClient;
import kr.pknu.seolmunhyeok201911938.model.CurrencyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_COUNTRY = 1;
    private TextView currentTimeText;
    private TextView[] currencyOutputTextViews = new TextView[3];
    private TextView[] amountTextViewKoreanTextViews = new TextView[3];
    private TextView[] countryTextViews = new TextView[3];
    private ImageView[] flagImageViews = new ImageView[3];

    private String[] defaultCountries = {"KRW", "USD", "JPY"};
    private int[] defaultFlags = {
            R.drawable.ic_flag_placeholder, // 실제 KRW 국기 리소스로 교체
            R.drawable.ic_flag_placeholder, // 실제 USD 국기 리소스로 교체
            R.drawable.ic_flag_placeholder  // 실제 JPY 국기 리소스로 교체
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

            // 초기 데이터 설정
            countryTextViews[i].setText(defaultCountries[i]);
            flagImageViews[i].setImageResource(defaultFlags[i]);

            int finalI = i;
            countryTextViews[i].setOnClickListener(view -> openCountrySelectionActivity(finalI));
        }

        fetchExchangeRates();
    }

    private void openCountrySelectionActivity(int countryIndex) {
        Intent intent = new Intent(this, CountrySelectionActivity.class);
        intent.putExtra("countryIndex", countryIndex); // 선택된 국가의 인덱스 전달
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

                    // 각 텍스트뷰에 데이터 반영
                    for (int i = 0; i < countryTextViews.length; i++) {
                        String selectedCurrency = countryTextViews[i].getText().toString();
                        if (rates.data.containsKey(selectedCurrency)) {
                            double rate = rates.data.get(selectedCurrency);

                            // 금액 계산 (예: KRW 기준 10000원)
                            double baseAmount = 10000.0; // 기본값, 필요하면 사용자 입력 반영
                            double convertedAmount = baseAmount * rate;

                            // 환율 값 업데이트
                            currencyOutputTextViews[i].setText(String.format(Locale.getDefault(), "%.2f", convertedAmount));

                            // 한글 금액 표시 업데이트
                            amountTextViewKoreanTextViews[i].setText(convertToKorean((long) convertedAmount));
                        }
                    }

                    // 현재 시간 업데이트
                    String formattedTime = formatTimestamp(rates.timestamp);
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

    // 숫자를 한글로 변환하는 메서드
    private String convertToKorean(long value) {
        if (value == 0) {
            return "0원";
        }

        StringBuilder result = new StringBuilder();
        String[] units = {"", "만", "억", "조"};
        int unitIndex = 0;

        while (value > 0) {
            long part = value % 10000;
            if (part > 0) {
                result.insert(0, part + units[unitIndex]);
            }
            value /= 10000;
            unitIndex++;
        }

        return result.append("원").toString();
    }

    // 타임스탬프 형식화 메서드
    private String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp * 1000); // 서버에서 초 단위로 제공
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return "업데이트: " + formatter.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_COUNTRY && resultCode == RESULT_OK && data != null) {
            int countryIndex = data.getIntExtra("countryIndex", -1);
            String selectedCountry = data.getStringExtra("selectedCountry");
            int selectedFlag = data.getIntExtra("selectedFlag", R.drawable.ic_flag_placeholder);

            if (countryIndex >= 0) {
                // 텍스트뷰와 이미지뷰 업데이트
                countryTextViews[countryIndex].setText(selectedCountry);
                flagImageViews[countryIndex].setImageResource(selectedFlag);
            }
        }
    }

}
