package com.jnpc.currencyconvertor;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class currencyListFragment extends ListFragment {
    int btnSelected;
    public ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    public ArrayList<HashMap<String, String>> searchList;
    private ListView mListView;
    CustomCurrencyListViewAdapter myAdapter;
    View view = null;

    public HashMap<String, String> getSelectedItem(int position) {
        return searchList.get(position);
    }

    @Override
    public void onAttach(Activity act) {
        super.onAttach(act);
        if (act instanceof Helloworld) {
            ((Helloworld) act).setCurrentFragmentID(R.layout.fragment_currencylistview);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_currencylistview, container, false);
        HashMap<?, ?> hashMap = null;
        Bundle bundle = this.getArguments();

        if (view != null) {
            Object obj = view.getLayoutParams();
            if (obj instanceof  FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) obj;
                params.setMargins(0,0,0,10);
                view.setLayoutParams(params);
            }
        }

        mListView = (ListView) view.findViewById(R.id.MyListView);

        if (getArguments() != null) {
            if (bundle != null) {
                if (bundle.containsKey("hashMap")) {
                    Object object = bundle.getSerializable("hashMap");
                    if (object instanceof  HashMap) {
                        hashMap = (HashMap) object;
                    }
                }
                btnSelected = bundle.getInt("btnSelected");
            }
        }

        if (hashMap != null && hashMap.size() > 0) {
            for (Map.Entry<?, ?> entry : hashMap.entrySet())
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Country", entry.getKey().toString());
                map.put("Rate", entry.getValue().toString());
                mylist.add(map);
            }
        }

        searchList = new ArrayList<HashMap<String,String>>(mylist);

        myAdapter = new CustomCurrencyListViewAdapter(this, searchList);

        setListAdapter(myAdapter);

        mListView.setTextFilterEnabled(true);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void searchOnTheList(String queryText) {
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
            myAdapter.notifyDataSetChanged();
        }
    }
}