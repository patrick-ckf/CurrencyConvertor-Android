package com.jnpc.currencyconvertor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CurrencyTableActivity extends Activity implements OnQueryTextListener {
    public ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    public ArrayList<HashMap<String, String>> searchList;
    int btnSelected;
    private ListView mListView;
    private SearchView mSearchView;
    CustomCurrencyListViewAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        HashMap<?, ?> hashMap = null;

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setTitle("Currency Listing");

        setContentView(R.layout.currency_table_activity);
        mListView = (ListView) findViewById(R.id.MyListView);

        Intent intent = getIntent();
        Bundle a = intent.getExtras();
        if (a != null) {
            if (intent != null && a.containsKey("hashMap")) {
                Object object = a.getSerializable("hashMap");
                if (object instanceof  HashMap) {
                   hashMap = (HashMap) object;
                }
            }
            btnSelected = a.getInt("btnSelected");
        }

        if (hashMap !=null && hashMap.size() > 0) {
            for (Map.Entry<?, ?> entry : hashMap.entrySet())
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Country", entry.getKey().toString());
                map.put("Rate", entry.getValue().toString());
                mylist.add(map);
            }
        }

        searchList = new ArrayList<HashMap<String,String>>(mylist);

        adapter = new CustomCurrencyListViewAdapter(this, searchList);
        mListView.setAdapter(adapter);

        mListView.setTextFilterEnabled(true);
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Search words...");
        mSearchView.setSubmitButtonEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        if (item != null) {
            mSearchView = (SearchView) item.getActionView();
        }
        setupSearchView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextChange(String queryText) {
        if(TextUtils.isEmpty(queryText)) {
            mListView.clearTextFilter();
        } else {
            int textLength = queryText.length();
            searchList.clear();
            for (HashMap<String, String> data : mylist) {
                String countryName = data.get("Country");
                if (queryText.equalsIgnoreCase(countryName.substring(0, textLength))) {
                    searchList.add(data);
                }
            }
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String queryText) {
        return false;
    }
}