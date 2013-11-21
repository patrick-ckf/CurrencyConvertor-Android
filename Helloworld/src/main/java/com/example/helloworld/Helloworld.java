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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Helloworld extends ActionBarActivity {
    private Map<String, Float> CurrencyMapping = new HashMap<String,Float>();
    String selectedItem;
    private int btnSelected = -1;
    PlaceholderFragment fragment = new PlaceholderFragment();

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
        new CurrencyXMLParser().execute("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
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
        String mSelectedItem;
        int mBtnSelected = -1;
        int mTxtSelected = -1;
        EditText [] textArray = new EditText[5];
        ImageButton [] btnArray = new ImageButton[5];
        String [] currency = new String[5];

        public void setSelectedItem(String str){
            mSelectedItem = str;
        }

        public void setBtnSelected(int btn) {
            mBtnSelected = btn;
        }


        public PlaceholderFragment() {
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
            btnArray[1] = (ImageButton) getActivity().findViewById(R.id.imageButton2);
            btnArray[2] = (ImageButton) getActivity().findViewById(R.id.imageButton3);
            btnArray[3] = (ImageButton) getActivity().findViewById(R.id.imageButton4);
            btnArray[4] = (ImageButton) getActivity().findViewById(R.id.imageButton5);

            for (EditText text : textArray) {
                text.setOnFocusChangeListener(new focusListener());
                text.addTextChangedListener(new textWatcher());
            }

            if (mBtnSelected >= 0 && mBtnSelected <= 4) btn = btnArray[mBtnSelected];

            if (drawable > 0) {
                if (btn != null)
                    btn.setImageResource(drawable);
            }
        }
        private class focusListener implements View.OnFocusChangeListener {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
            }
        }
        private class textWatcher implements TextWatcher {
            public void afterTextChanged(Editable s) {
                int i;
                String str = s.toString();
                Log.e(getActivity().getString(R.string.app_name), str);
                for (i = 0; i < 5; i++) {
                    if (textArray[i].getText().toString().equals(s.toString())) {
                        mTxtSelected = i;
                    }
                }
                for (i = 0; i < 5; i++) {
                    Drawable drawable = btnArray[i].getDrawable();
                    getActivity().getResources().getResourceEntryName(id);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            public void onTextChanged(CharSequence s, int start, int before, int count){

            }
        }
    }

    private class CurrencyXMLParser extends AsyncTask<String, Void, String> {

        private Map<String, Float> CurrencyMapping = new HashMap<String,Float>();

        //public Map<String, Float> getCurrencyMap() { return CurrencyMapping; }

        private void insertCurrencyToMap(Map<String,String> attributes) {
            if (attributes != null) {
                String currency = attributes.get("currency");
                String rate = attributes.get("rate");
                if (currency !=null && rate != null) {
                    CurrencyMapping.put(currency, Float.valueOf(rate));
                }
            }
        }

        private Map<String,String>  getAttributes(XmlPullParser parser) {
            Map<String,String> attrs=null;
            int account = parser.getAttributeCount();
            if(account != -1) {
                attrs = new HashMap<String,String>(account);
                for(int x = 0; x < account;x++)  attrs.put(parser.getAttributeName(x), parser.getAttributeValue(x));
            }
            return attrs;
        }

        void parse(XmlPullParser xpParser) {
            int eventType;
            String strNode;
            Map<String,String> attributes;
            try {
                eventType = xpParser.next();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_TAG) {
                        strNode = xpParser.getName();
                        if (strNode.compareTo("Cube") == 0) {
                            eventType = xpParser.next();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                if (eventType == XmlPullParser.START_TAG) {
                                    attributes = getAttributes(xpParser);
                                    insertCurrencyToMap(attributes);
                                } else if(eventType == XmlPullParser.TEXT){
                                    attributes = getAttributes(xpParser);
                                    insertCurrencyToMap(attributes);
                                } else if(eventType == XmlPullParser.END_TAG){
                                    attributes = getAttributes(xpParser);
                                    insertCurrencyToMap(attributes);
                                }
                                eventType = xpParser.next();
                            }
                        }
                    }
                    eventType = xpParser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                URL url = new URL(urls[0]);
                InputStream is = url.openStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpParser = factory.newPullParser();
                xpParser.setInput(is,"utf-8");
                parse(xpParser);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Helloworld.this.setCurrencyMapping(CurrencyMapping);
        }
    }
}
