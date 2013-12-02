package com.jnpc.currencyconvertor;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CurrencyXMLParser extends AsyncTask<String, Void, String> {
    private OnTaskCompleted listener;
    private Map<String, Float> CurrencyMapping = new HashMap<String,Float>();

    public CurrencyXMLParser(OnTaskCompleted listener) {
        this.listener = listener;
    }

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
            listener.retryDownloadingXML();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onTaskCompleted(CurrencyMapping);
    }
}
