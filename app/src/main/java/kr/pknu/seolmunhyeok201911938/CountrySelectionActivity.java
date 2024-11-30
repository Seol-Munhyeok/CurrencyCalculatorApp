package kr.pknu.seolmunhyeok201911938;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import kr.pknu.seolmunhyeok201911938.model.CountryAdapter;

public class CountrySelectionActivity extends Activity {
    private final String[] countries = {"KRW", "USD", "JPY", "EUR", "GBP",
                                        "CNY", "AUD", "CAD", "CHF", "NZD",
                                        "SGD", "THB", "HKD", "PHP", "MYR",
                                        "IDR", "RUB", "INR", "ZAR", "CZK"};
    private final int[] flags = {
            R.drawable.ic_flag_krw,
            R.drawable.ic_flag_usd,
            R.drawable.ic_flag_jpy,
            R.drawable.ic_flag_eur,
            R.drawable.ic_flag_gbp,
            R.drawable.ic_flag_cny,
            R.drawable.ic_flag_aud,
            R.drawable.ic_flag_cad,
            R.drawable.ic_flag_chf,
            R.drawable.ic_flag_nzd,
            R.drawable.ic_flag_sgd,
            R.drawable.ic_flag_thb,
            R.drawable.ic_flag_hkd,
            R.drawable.ic_flag_php,
            R.drawable.ic_flag_myr,
            R.drawable.ic_flag_idr,
            R.drawable.ic_flag_rub,
            R.drawable.ic_flag_inr,
            R.drawable.ic_flag_zar,
            R.drawable.ic_flag_czk
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selection);

        ListView listView = findViewById(R.id.countryListView);

        CountryAdapter adapter = new CountryAdapter(this, countries, flags);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int countryIndex = getIntent().getIntExtra("countryIndex", -1);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("countryIndex", countryIndex);
            resultIntent.putExtra("selectedCountry", countries[position]);
            resultIntent.putExtra("selectedFlag", flags[position]);

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
