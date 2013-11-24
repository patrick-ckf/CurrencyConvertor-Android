package com.example.helloworld;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Helloworld extends ActionBarActivity implements OnTaskCompleted{
    private Map<String, Float> CurrencyMapping = new HashMap<String,Float>();
    String selectedItem;
    private int btnSelected = -1;
    PlaceholderFragment fragment = new PlaceholderFragment();

    public void onTaskCompleted(Map<String, Float> map) {
        setCurrencyMapping(map);
        fragment.setCurrencyMapping(map);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Currency Convertor");
        setContentView(R.layout.activity_helloworld);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
            Bundle extra = getIntent().getExtras();
            if (extra != null) {
                selectedItem = extra.getString("selectedItem");
                btnSelected = extra.getInt("btnSelected");
            }
            if (fragment != null) {
                fragment.setSelectedItem(selectedItem);
                fragment.setBtnSelected(btnSelected);
            }
        }
        new CurrencyXMLParser(this).execute("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.helloworld, menu);
        return true;
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imageButton1:
                btnSelected = 0;
                break;
            case R.id.imageButton2:
                btnSelected = 1;
                break;
            case R.id.imageButton3:
                btnSelected = 2;
                break;
            case R.id.imageButton4:
                btnSelected = 3;
                break;
            case R.id.imageButton5:
                btnSelected = 4;
                break;
            default:
                break;
        }
        Intent intent = new Intent(this, CurrencyTableActivity.class);
        Bundle Extras = new Bundle();
        if (this.CurrencyMapping != null && this.CurrencyMapping.size() > 0)
            Extras.putSerializable("hashMap", (HashMap<String, Float>) this.CurrencyMapping);
        Extras.putInt("btnSelected", btnSelected);
        intent.putExtras(Extras);
        startActivity(intent);
    }

    public void setCurrencyMapping(Map<String, Float> map) {
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            CurrencyMapping.put(key, value);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return item.getItemId() == R.id.action_settings && super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private Map<String, Float> CurrencyMapping = new HashMap<String,Float>();
        String mSelectedItem;
        int mBtnSelected = -1;
        int mTxtSelected = -1;
        boolean ignoreNextTextChange = false;
        EditText [] textArray = new EditText[5];
        ImageButton [] btnArray = new ImageButton[5];
        float [] rate = new float[5];

        public PlaceholderFragment() {}

        public void setSelectedItem(String str){
            mSelectedItem = str;
        }
        public void setBtnSelected(int btn) { mBtnSelected = btn; }

        public boolean IsIgnoreNextTextChange() { return ignoreNextTextChange; }
        public void SetIgnoreNextTextChange(boolean _ignoreNextTextChange) { ignoreNextTextChange = _ignoreNextTextChange; }

        public void setCurrencyMapping(Map<String, Float> map) {
            for (Map.Entry<String, Float> entry : map.entrySet()) {
                String key = entry.getKey();
                Float value = entry.getValue();
                CurrencyMapping.put(key, value);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_helloworld, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            String str;
            int drawable = -1;
            ImageButton btn = null;
            if (mSelectedItem != null) {
                str = mSelectedItem.toLowerCase(Locale.ENGLISH)+"_flag";
                drawable = getActivity().getResources().getIdentifier(str, "drawable", getActivity().getPackageName());
            }

            textArray[0] = (EditText) getActivity().findViewById(R.id.editText1);
            textArray[1] = (EditText) getActivity().findViewById(R.id.editText2);
            textArray[2] = (EditText) getActivity().findViewById(R.id.editText3);
            textArray[3] = (EditText) getActivity().findViewById(R.id.editText4);
            textArray[4] = (EditText) getActivity().findViewById(R.id.editText5);

            btnArray[0] = (ImageButton) getActivity().findViewById(R.id.imageButton1);
            btnArray[0].setTag(R.drawable.hkd_flag);
            btnArray[1] = (ImageButton) getActivity().findViewById(R.id.imageButton2);
            btnArray[1].setTag(R.drawable.usd_flag);
            btnArray[2] = (ImageButton) getActivity().findViewById(R.id.imageButton3);
            btnArray[2].setTag(R.drawable.jpy_flag);
            btnArray[3] = (ImageButton) getActivity().findViewById(R.id.imageButton4);
            btnArray[3].setTag(R.drawable.gbp_flag);
            btnArray[4] = (ImageButton) getActivity().findViewById(R.id.imageButton5);
            btnArray[4].setTag(R.drawable.bgn_flag);

            for (EditText text : textArray) {
                text.setOnFocusChangeListener(new focusListener());
                text.addTextChangedListener(new textWatcher(this));
            }

            if (mBtnSelected >= 0 && mBtnSelected <= 4) btn = btnArray[mBtnSelected];

            if (drawable > 0) {
                if (btn != null) {
                    btn.setImageResource(drawable);
                    btn.setTag(drawable);
                }
            }
        }
        private class focusListener implements View.OnFocusChangeListener {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
            }
        }
        private class textWatcher implements TextWatcher {
            PlaceholderFragment fragment;
            int fromCurrency;

            public textWatcher(Object t) {
                if (t instanceof  PlaceholderFragment) {
                    fragment = (PlaceholderFragment)t;
                }
            }

            private float getRateByBtn(ImageButton view) {
                int drawID = (Integer)view.getTag();
                float rate = 0;
                if (CurrencyMapping.size() <= 0) rate = 0;
                else {
                    switch (drawID) {
                        case R.drawable.usd_flag:
                            rate = CurrencyMapping.get("USD");
                            break;
                        case R.drawable.jpy_flag:
                            rate = CurrencyMapping.get("JPY");
                            break;
                        case R.drawable.bgn_flag:
                            rate = CurrencyMapping.get("BGN");
                            break;
                        case R.drawable.czk_flag:
                            rate = CurrencyMapping.get("CZK");
                            break;
                        case R.drawable.dkk_flag:
                            rate = CurrencyMapping.get("DKK");
                            break;
                        case R.drawable.gbp_flag:
                            rate = CurrencyMapping.get("GBP");
                            break;
                        case R.drawable.huf_flag:
                            rate = CurrencyMapping.get("HUF");
                            break;
                        case R.drawable.ltl_flag:
                            rate = CurrencyMapping.get("LTL");
                            break;
                        case R.drawable.lvl_flag:
                            rate = CurrencyMapping.get("LVL");
                            break;
                        case R.drawable.pln_flag:
                            rate = CurrencyMapping.get("PLN");
                            break;
                        case R.drawable.ron_flag:
                            rate = CurrencyMapping.get("RON");
                            break;
                        case R.drawable.sek_flag:
                            rate = CurrencyMapping.get("SEK");
                            break;
                        case R.drawable.chf_flag:
                            rate = CurrencyMapping.get("CHF");
                            break;
                        case R.drawable.nok_flag:
                            rate = CurrencyMapping.get("NOK");
                            break;
                        case R.drawable.hrk_flag:
                            rate = CurrencyMapping.get("HRK");
                            break;
                        case R.drawable.rub_flag:
                            rate = CurrencyMapping.get("RUB");
                            break;
                        case R.drawable.try_flag:
                            rate = CurrencyMapping.get("TRY");
                            break;
                        case R.drawable.aud_flag:
                            rate = CurrencyMapping.get("AUD");
                            break;
                        case R.drawable.brl_flag:
                            rate = CurrencyMapping.get("BRL");
                            break;
                        case R.drawable.cad_flag:
                            rate = CurrencyMapping.get("CAD");
                            break;
                        case R.drawable.cny_flag:
                            rate = CurrencyMapping.get("CNY");
                            break;
                        case R.drawable.hkd_flag:
                            rate = CurrencyMapping.get("HKD");
                            break;
                        case R.drawable.idr_flag:
                            rate = CurrencyMapping.get("IDR");
                            break;
                        case R.drawable.ils_flag:
                            rate = CurrencyMapping.get("ILS");
                            break;
                        case R.drawable.inr_flag:
                            rate = CurrencyMapping.get("INR");
                            break;
                        case R.drawable.krw_flag:
                            rate = CurrencyMapping.get("KRW");
                            break;
                        case R.drawable.mxn_flag:
                            rate = CurrencyMapping.get("MXN");
                            break;
                        case R.drawable.myr_flag:
                            rate = CurrencyMapping.get("MYR");
                            break;
                        case R.drawable.nzd_flag:
                            rate = CurrencyMapping.get("NZD");
                            break;
                        case R.drawable.php_flag:
                            rate = CurrencyMapping.get("PHP");
                            break;
                        case R.drawable.sgd_flag:
                            rate = CurrencyMapping.get("SGD");
                            break;
                        case R.drawable.thb_flag:
                            rate = CurrencyMapping.get("THB");
                            break;
                        case R.drawable.zar_flag:
                            rate = CurrencyMapping.get("ZAR");
                            break;
                        default:
                            break;
                    }
                }
                return rate;
            }
            
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after){ }

            public void onTextChanged(CharSequence s, int start, int before, int count){
                int i;
                if (fragment.IsIgnoreNextTextChange()) {
                    fragment.SetIgnoreNextTextChange(false);
                    return;
                }
                for (i = 0; i < 5; i++) {
                    if (textArray[i].getText().toString().equals(s.toString())) {
                        String str = s.toString();
                        mTxtSelected = i;
                        if (!str.isEmpty()) {
                            float temp = Float.valueOf(str);
                            fromCurrency = (int)temp;
                        }
                    }
                }
                for (i = 0; i < 5; i++) rate[i] = getRateByBtn(btnArray[i]);
                for (i = 0; i < 5; i++) {
                    if (fromCurrency > 0) {
                        if (i != mTxtSelected) {
                            float output = fromCurrency*(rate[mTxtSelected]/rate[i]);
                            String displayString = String.format("%.02f", output);
                            fragment.SetIgnoreNextTextChange(true);
                            textArray[i].setText(displayString, TextView.BufferType.EDITABLE);
                        }
                    }
                }
            }
        }
    }
}
