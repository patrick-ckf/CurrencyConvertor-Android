package com.example.helloworld;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Helloworld extends ActionBarActivity {
    private int mSelected = -1;
    private Map<String, Float> CurrencyMapping = new HashMap<String,Float>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Bundle a = data.getExtras();
                mSelected = a.getInt("selectedItem");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Currency Convertor");
        setContentView(R.layout.activity_helloworld);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mSelected = extra.getInt("selectedItem");
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
        view.getDrawingTime();
        Intent intent = new Intent(this, CurrencyTableActivity.class);
        if (this.CurrencyMapping != null && this.CurrencyMapping.size() > 0)
            intent.putExtra("hashMap", (HashMap<String, Float>)this.CurrencyMapping);
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_helloworld, container, false);
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
