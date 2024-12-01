package kr.pknu.seolmunhyeok201911938;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.pknu.seolmunhyeok201911938.model.CountryAdapter;

public class CountrySelectionActivity extends Activity {
    private final String[] countries = {"KRW", "USD", "JPY", "EUR", "GBP",
                                        "CNY", "AUD", "CAD", "CHF", "NZD",
                                        "SGD", "THB", "HKD", "PHP", "MYR",
                                        "IDR", "RUB", "INR", "ZAR", "CZK"};
    private final String[] countryNames = {"대한민국 원", "미국 달러", "일본 엔", "유로", "파운드 스털링",
            "중국 위안화", "호주 달러", "캐나다 달러", "스위스 프랑", "뉴질랜드 달러",
            "싱가포르 달러", "태국 바트", "홍콩 달러", "필리핀 페소", "말레이시아 링깃",
            "인도네시아 루피아", "러시아 루블", "인도 루피", "남아프리카공화국 랜드", "체코 코루나"};

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

    private Map<String, Integer> countryIndexMap = new HashMap<>();
    private CountryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selection);

        ListView listView = findViewById(R.id.countryListView);
        SearchView searchView = findViewById(R.id.countrySearchView);

        adapter = new CountryAdapter(this, countries, countryNames, flags);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    adapter.getFilter().filter("");
                } else {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int countryIndex = getIntent().getIntExtra("countryIndex", -1);

            if (adapter.getCount() == 0) {
                Toast.makeText(CountrySelectionActivity.this, "선택할 수 있는 국가가 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            int originalIndex = adapter.getOriginalPosition(position);
            if (originalIndex == -1) {
                return;
            }

            String selectedCountry = countries[originalIndex];
            String selectedCountryName = countryNames[originalIndex];
            int selectedFlag = flags[originalIndex];

            Intent resultIntent = new Intent();
            resultIntent.putExtra("countryIndex", countryIndex);
            resultIntent.putExtra("selectedCountry", selectedCountry);
            resultIntent.putExtra("selectedCountryName", selectedCountryName);
            resultIntent.putExtra("selectedFlag", selectedFlag);

            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }
}
