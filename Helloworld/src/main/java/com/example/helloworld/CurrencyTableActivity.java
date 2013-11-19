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
    int btnSelected;

    public void onCreate(Bundle savedInstanceState) {
        HashMap<?, ?> hashMap = null;
        LazyAdapter adapter;

        super.onCreate(savedInstanceState);

        setTitle("Currency Listing");

        setContentView(R.layout.currency_table_activity);
        ListView list = (ListView) findViewById(R.id.MyListView);

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
        adapter = new LazyAdapter(this, mylist);
        list.setAdapter(adapter);
    }

    public void onItemClick(int mPosition) {
        Intent intent = new Intent(this, Helloworld.class);
        HashMap<String, String> data = mylist.get(mPosition);
        Bundle bundle = new Bundle();
        String country = data.get("Country");
        bundle.putString("selectedItem", country);
        bundle.putInt("btnSelected", btnSelected);
        intent.putExtras(bundle);
        //setResult(RESULT_OK, intent);
        startActivityForResult(intent, RESULT_OK);
    }
}