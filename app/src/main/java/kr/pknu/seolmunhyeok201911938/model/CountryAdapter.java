package kr.pknu.seolmunhyeok201911938.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import kr.pknu.seolmunhyeok201911938.R;

public class CountryAdapter extends BaseAdapter {
    private final Context context;
    private final String[] countries;
    private final int[] flags;

    public CountryAdapter(Context context, String[] countries, int[] flags) {
        this.context = context;
        this.countries = countries;
        this.flags = flags;
    }

    @Override
    public int getCount() {
        return countries.length;
    }

    @Override
    public Object getItem(int position) {
        return countries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false);
        }
        ImageView flagImageView = convertView.findViewById(R.id.flagImageView);
        TextView countryNameTextView = convertView.findViewById(R.id.countryNameTextView);
        TextView currencyUnitTextView = convertView.findViewById(R.id.currencyUnitTextView);

        flagImageView.setImageResource(flags[position]);
        countryNameTextView.setText(countries[position]);
        String currencyUnit = getCurrencyUnit(countries[position]);
        currencyUnitTextView.setText(currencyUnit);

        return convertView;
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
            default: return "단위";
        }
    }
}
