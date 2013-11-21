package com.example.helloworld;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter implements View.OnClickListener{

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public String currencyToCountry(String currency) {
        if (currency.equals("USD")) return "USA";
        else if (currency.equals("JPY")) return "Japan";
        else if (currency.equals("BGP")) return "Bulgarian";
        else if (currency.equals("CZK")) return "Czech";
        else if (currency.equals("DKK")) return "Denmark";
        else if (currency.equals("GBP")) return "United Kingdom";
        else if (currency.equals("HUF")) return "Hungary";
        else if (currency.equals("LTL")) return "lithuania";
        else if (currency.equals("LVL")) return "Latvia";
        else if (currency.equals("PLN")) return "Poland";
        else if (currency.equals("RON")) return "Romania";
        else if (currency.equals("SEK")) return "Sweden";
        else if (currency.equals("CHF")) return "Switzerland";
        else if (currency.equals("NOK")) return "Norway";
        else if (currency.equals("HRK")) return "Croatia";
        else if (currency.equals("RUB")) return "Russia";
        else if (currency.equals("TRY")) return "Turkey";
        else if (currency.equals("AUD")) return "Australia";
        else if (currency.equals("BRL")) return "Brazil";
        else if (currency.equals("CAD")) return "Canada";
        else if (currency.equals("CNY")) return "Republic of China";
        else if (currency.equals("HKD")) return "Hong Kong";
        else if (currency.equals("IDR")) return "India";
        else if (currency.equals("ILS")) return "Israeli";
        else if (currency.equals("INR")) return "India";
        else if (currency.equals("KRW")) return "South Korea";
        else if (currency.equals("MXN")) return "Mexico";
        else if (currency.equals("MYR")) return "Turkey";
        else if (currency.equals("NZD")) return "New Zealand";
        else if (currency.equals("PHP")) return "Philippines ";
        else if (currency.equals("SGD")) return "Singapore";
        else if (currency.equals("THB")) return "Thailand";
        else if (currency.equals("ZAR")) return "South Africa";
        else return null;
    }

    public Drawable getDrawableByCurrency(String currency) {
        Drawable drawable = null;
        String str = currency.toLowerCase(Locale.ENGLISH)+"_flag";
        //Log.e(activity.getString(R.string.app_name), str);
        if (str != null)drawable = activity.getResources().getDrawable(activity.getResources().getIdentifier(str, "drawable", activity.getPackageName()));
        //Log.e(activity.getString(R.string.app_name), drawable.toString());
        return drawable;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) vi = inflater.inflate(R.layout.list_row, null);

        TextView country = (TextView)vi.findViewById(R.id.country);
        TextView rate = (TextView)vi.findViewById(R.id.rate);
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);

        HashMap<String, String> currency = new HashMap<String, String>();
        currency = data.get(position);

        // Setting all values in listview
        country.setText(currencyToCountry(currency.get("Country")));
        String str = "1 EUR to " + currency.get("Rate") + " " + currency.get("Country");
        //Log.e(activity.getString(R.string.app_name), str);
        rate.setText(str);
        thumb_image.setImageDrawable(getDrawableByCurrency(currency.get("Country")));
        vi.setOnClickListener(new OnItemClickListener(position));
        return vi;
    }

    @Override
    public void onClick(View view) {
        //Log.v(activity.getString(R.string.app_name), "Row button clicked");
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition = 0;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View view) {
            CurrencyTableActivity act = (CurrencyTableActivity)activity;
            act.onItemClick(mPosition);
        }

    };
}