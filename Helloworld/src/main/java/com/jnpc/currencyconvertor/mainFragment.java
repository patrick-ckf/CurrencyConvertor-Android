package com.jnpc.currencyconvertor;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class mainFragment extends android.app.Fragment {
    private View view;
    private Map<String, Float> CurrencyMapping = new HashMap<String,Float>();
    String mSelectedItem;
    int mBtnSelected = -1;
    int mTxtSelected = -1;
    boolean ignoreNextTextChange = false;
    EditText[] textArray = new EditText[Global.ListSize];
    ImageButton[] btnArray = new ImageButton[Global.ListSize];
    float [] rate = new float[Global.ListSize];

    public mainFragment() {}

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
    public void onAttach(Activity act) {
        super.onAttach(act);
        if (act instanceof Helloworld) {
            ((Helloworld) act).setCurrentFragmentID(R.layout.fragment_main);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        if (view != null) {
            Object obj = view.getLayoutParams();
            if (obj instanceof  FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) obj;
                params.setMargins(-33,0,0,10);
                view.setLayoutParams(params);
            }
        }


        Bundle bundle = this.getArguments();
        if (getArguments() != null) {
            if (bundle != null) {
                mSelectedItem = bundle.getString("selectedItem");
                mBtnSelected = bundle.getInt("btnSelected");
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String str;
        int drawable = -1;
        ImageButton btn = null;
        if (mSelectedItem != null) {
            str = mSelectedItem.toLowerCase(Locale.ENGLISH)+"_flag";
            Activity act = getActivity();
            if (act != null) {
                drawable = act.getResources().getIdentifier(str, "drawable", getActivity().getPackageName());
            }
        }

        if (view == null) {
            return;
        }
        textArray[0] = (EditText) view.findViewById(R.id.editText1);
        textArray[1] = (EditText) view.findViewById(R.id.editText2);
        textArray[2] = (EditText) view.findViewById(R.id.editText3);
        textArray[3] = (EditText) view.findViewById(R.id.editText4);
        textArray[4] = (EditText) view.findViewById(R.id.editText5);
        textArray[5] = (EditText) view.findViewById(R.id.editText6);
        textArray[6] = (EditText) view.findViewById(R.id.editText7);
        textArray[7] = (EditText) view.findViewById(R.id.editText8);
        textArray[8] = (EditText) view.findViewById(R.id.editText9);

        btnArray[0] = (ImageButton) view.findViewById(R.id.imageButton1);
        if (btnArray[0] != null) {
            btnArray[0].setTag(R.drawable.hkd_flag);
        }
        btnArray[1] = (ImageButton) view.findViewById(R.id.imageButton2);
        if (btnArray[1] != null) {
            btnArray[1].setTag(R.drawable.usd_flag);
        }
        btnArray[2] = (ImageButton) view.findViewById(R.id.imageButton3);
        if (btnArray[2] != null) {
            btnArray[2].setTag(R.drawable.jpy_flag);
        }
        btnArray[3] = (ImageButton) view.findViewById(R.id.imageButton4);
        if (btnArray[3] != null) {
            btnArray[3].setTag(R.drawable.gbp_flag);
        }
        btnArray[4] = (ImageButton) view.findViewById(R.id.imageButton5);
        if (btnArray[4] != null) {
            btnArray[4].setTag(R.drawable.bgn_flag);
        }
        btnArray[5] = (ImageButton) view.findViewById(R.id.imageButton6);
        if (btnArray[5] != null) {
            btnArray[5].setTag(R.drawable.cny_flag);
        }
        btnArray[6] = (ImageButton) view.findViewById(R.id.imageButton7);
        if (btnArray[6] != null) {
            btnArray[6].setTag(R.drawable.aud_flag);
        }
        btnArray[7] = (ImageButton) view.findViewById(R.id.imageButton8);
        if (btnArray[7] != null) {
            btnArray[7].setTag(R.drawable.php_flag);
        }
        btnArray[8] = (ImageButton) view.findViewById(R.id.imageButton9);
        if (btnArray[8] != null) {
            btnArray[8].setTag(R.drawable.krw_flag);
        }

        for (EditText text : textArray) {
            text.addTextChangedListener(new textWatcher(this));
        }

        if (mBtnSelected >= 0 && mBtnSelected <= Global.ListSize-1) btn = btnArray[mBtnSelected];

        if (drawable > 0) {
            if (btn != null) {
                btn.setImageResource(drawable);
                btn.setTag(drawable);
            }
        }
    }

    private class textWatcher implements TextWatcher {
        mainFragment fragment;
        int fromCurrency;

        public textWatcher(Object t) {
            if (t instanceof  mainFragment) {
                fragment = (mainFragment)t;
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
            for (i = 0; i < Global.ListSize; i++) {
                Editable edit = textArray[i].getText();
                if (edit != null) {
                    if (edit.toString().equals(s.toString())) {
                        String str = s.toString();
                        mTxtSelected = i;
                        if (!str.isEmpty()) {
                            float temp = Float.valueOf(str);
                            fromCurrency = (int)temp;
                        }
                    }
                }
            }
            for (i = 0; i < Global.ListSize; i++) rate[i] = getRateByBtn(btnArray[i]);
            for (i = 0; i < Global.ListSize; i++) {
                if (fromCurrency > 0) {
                    if (i != mTxtSelected) {
                        float output = fromCurrency*(rate[i]/rate[mTxtSelected]);
                        String displayString = String.format("%.02f", output);
                        fragment.SetIgnoreNextTextChange(true);
                        textArray[i].setText(displayString, TextView.BufferType.EDITABLE);
                    }
                }
            }
        }
    }
}