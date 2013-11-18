package com.example.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CurrencyTableActivity extends Activity {
    public ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

    public void onCreate(Bundle savedInstanceState) {
        HashMap<?, ?> hashMap = null;
        LazyAdapter adapter;

        super.onCreate(savedInstanceState);

        setTitle("Currency Listing");

        setContentView(R.layout.currency_table_activity);
        ListView list = (ListView) findViewById(R.id.MyListView);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("hashMap")) {
            Object object = intent.getSerializableExtra("hashMap");
            if (object instanceof  HashMap) {
               hashMap = (HashMap) object;
            }
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
        adapter = new LazyAdapter(this, mylist);
        list.setAdapter(adapter);
    }

    public void onItemClick(int mPosition) {
        Intent data = new Intent(this, Helloworld.class);
        data.putExtra("selectedItem", mPosition);
        setResult(RESULT_OK, data);
        startActivityForResult(data, RESULT_OK);
    }
}