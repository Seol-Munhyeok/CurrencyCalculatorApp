package kr.pknu.seolmunhyeok201911938;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

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

        initailizeIndexMap();

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
                String lowerCaseQuery = newText.toLowerCase();

                List<String> filteredCountryList = new ArrayList<>();
                List<String> filteredCountryNameList = new ArrayList<>();
                List<Integer> filteredFlagList = new ArrayList<>();

                for (int i = 0; i < countries.length; i++) {
                    if (countries[i].toLowerCase().contains(lowerCaseQuery) ||
                            countryNames[i].contains(newText)) {
                        filteredCountryList.add(countries[i]);
                        filteredCountryNameList.add(countryNames[i]);
                        filteredFlagList.add(flags[i]);
                    }
                }

                String[] filteredCountries = filteredCountryList.toArray(new String[0]);
                String[] filteredCountryNames = filteredCountryNameList.toArray(new String[0]);
                int[] filteredFlags = filteredFlagList.stream().mapToInt(i -> i).toArray();

                adapter.updateData(filteredCountries, filteredCountryNames, filteredFlags);
                return true;
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int countryIndex = getIntent().getIntExtra("countryIndex", -1);

            String selectedCountry = adapter.getCountryAtPosition(position);
            int originalIndex = countryIndexMap.get(selectedCountry);

            String selectedCountryName = countryNames[originalIndex];
            int selectedFlag = flags[originalIndex];

            Intent resultIntent = new Intent();
            resultIntent.putExtra("countryIndex", countryIndex);
            resultIntent.putExtra("selectedCountry", countries[position]);
            resultIntent.putExtra("selectedCountryName", selectedCountryName);
            resultIntent.putExtra("selectedFlag", flags[position]);

            setResult(RESULT_OK, resultIntent);
            finish();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void initailizeIndexMap() {
        for (int i = 0; i < countries.length; i++) {
            countryIndexMap.put(countries[i], i);
        }
    }
}
