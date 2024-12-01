package kr.pknu.seolmunhyeok201911938.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kr.pknu.seolmunhyeok201911938.R;

public class CountryAdapter extends BaseAdapter implements Filterable {
    private final Context context;
    private final String[] originalCountries;
    private final String[] originalCountryNames;
    private final int[] originalFlags;

    private String[] filteredCountries;
    private String[] filteredCountryNames;
    private int[] filteredFlags;

    public CountryAdapter(Context context, String[] countries, String[] countryNames, int[] flags) {
        this.context = context;
        this.originalCountries = countries;
        this.originalCountryNames = countryNames;
        this.originalFlags = flags;

        this.filteredCountries = countries;
        this.filteredCountryNames = countryNames;
        this.filteredFlags = flags;
    }

    @Override
    public int getCount() {
        return filteredCountries.length;
    }

    @Override
    public Object getItem(int position) {
        return filteredCountries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateData(String[] countries) {
        this.filteredCountries = countries;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false);
        }
        ImageView flagImageView = convertView.findViewById(R.id.flagImageView);
        TextView countryNameTextView = convertView.findViewById(R.id.countryNameTextView);
        TextView currencyUnitTextView = convertView.findViewById(R.id.currencyUnitTextView);

        flagImageView.setImageResource(filteredFlags[position]);
        countryNameTextView.setText(filteredCountries[position]);
        String currencyUnit = getCurrencyUnit(filteredCountries[position]);
        currencyUnitTextView.setText(currencyUnit);

        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase(Locale.getDefault()).trim();
                FilterResults results = new FilterResults();

                if (query.isEmpty()) {
                    results.values = new Object[]{originalCountries, originalCountryNames, originalFlags};
                    results.count = originalCountries.length;
                } else {
                    ArrayList<String> filteredCountriesList = new ArrayList<>();
                    ArrayList<String> filteredCountryNamesList = new ArrayList<>();
                    ArrayList<Integer> filteredFlagsList = new ArrayList<>();

                    for (int i = 0; i < originalCountries.length; i++) {
                        if (originalCountries[i].toLowerCase().contains(query) ||
                                originalCountryNames[i].contains(query)) {
                            filteredCountriesList.add(originalCountries[i]);
                            filteredCountryNamesList.add(originalCountryNames[i]);
                            filteredFlagsList.add(originalFlags[i]);
                        }
                    }

                    results.values = new Object[]{
                            filteredCountriesList.toArray(new String[0]),
                            filteredCountryNamesList.toArray(new String[0]),
                            filteredFlagsList.stream().mapToInt(i -> i).toArray()
                    };
                    results.count = filteredCountriesList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values instanceof Object[]) {
                    Object[] filteredResults = (Object[]) results.values;

                    if (filteredResults.length == 3) {
                        filteredCountries = (String[]) filteredResults[0];
                        filteredCountryNames = (String[]) filteredResults[1];
                        filteredFlags = (int[]) filteredResults[2];
                    }
                }
                notifyDataSetChanged();
            }
        };
    }


    public int getOriginalPosition(int filteredPosition) {
        for (int i = 0; i < originalCountries.length; i++) {
            if (originalCountries[i].equals(filteredCountries[filteredPosition])) {
                return i;
            }
        }
        return -1;
    }


    private String getCurrencyUnit(String country) {
        switch (country) {
            case "KRW": return "대한민국 원";
            case "USD": return "미국 달러";
            case "JPY": return "일본 엔";
            case "EUR": return "유로";
            case "GBP": return "파운드 스털링";
            case "CNY": return "중국 위안화";
            case "AUD": return "호주 달러";
            case "CAD": return "캐나다 달러";
            case "CHF": return "스위스 프랑";
            case "NZD": return "뉴질랜드 달러";
            case "SGD": return "싱가포르 달러";
            case "THB": return "태국 바트";
            case "HKD": return "홍콩 달러";
            case "PHP": return "필리핀 페소";
            case "MYR": return "말레이시아 링깃";
            case "IDR": return "인도네시아 루피아";
            case "RUB": return "러시아 루블";
            case "INR": return "인도 루피";
            case "ZAR": return "남아프리카공화국 랜드";
            case "CZK": return "체코 코루나";
            default: return "단위";
        }
    }
}