package kr.pknu.seolmunhyeok201911938;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import kr.pknu.seolmunhyeok201911938.model.CountryAdapter;

public class CountrySelectionActivity extends Activity {
    private final String[] countries = {"KRW", "USD", "JPY", "EUR", "GBP", "CNY", "AUD", "CAD", "CHF", "NZD"};
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
            R.drawable.ic_flag_nzd
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
