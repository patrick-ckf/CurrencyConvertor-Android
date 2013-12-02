package com.jnpc.currencyconvertor;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.FragmentManager;
import android.view.Window;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.Map;

public class Helloworld extends Activity implements OnTaskCompleted, OnQueryTextListener {
    FragmentManager fragmentManager;
    SearchView mSearchView;
    MenuItem searchButton;
    private Map<String, Float> CurrencyMapping = new HashMap<String,Float>();
    private int btnSelected = -1;
    AdView adView;
    int mCurrentFragmentID = -1;

    mainFragment mFragment = new mainFragment();
    currencyListFragment cFragment = new currencyListFragment();

    public void onTaskCompleted(Map<String, Float> map) {
        setCurrencyMapping(map);
        mFragment.setCurrencyMapping(map);
    }

    public void retryDownloadingXML() {
        new CurrencyXMLParser(this).execute("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        new XMLDownloader().execute("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
    }

    public void setCurrentFragmentID(int id) {
        mCurrentFragmentID = id;
        if (id == R.layout.fragment_currencylistview) {
            searchButton.setEnabled(true);
            setupSearchView();
        } else if (id == R.layout.fragment_main) {
            if (mSearchView != null) {
                mSearchView.clearFocus();
                mSearchView.setVisibility(View.GONE);
            }
        }
    }

    public int getCurrentFragmentID() {
        return mCurrentFragmentID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTitle("Currency Convertor");
        setContentView(R.layout.activity_helloworld);

        if (savedInstanceState == null) {
            adView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();

            fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.contentFragment, mFragment, "main");
            transaction.commit();

            adView.loadAd(adRequest);
        }
        retryDownloadingXML();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        searchButton = menu.findItem(R.id.search);
        if (searchButton != null) {
            mSearchView = (SearchView)searchButton.getActionView();
            searchButton.setEnabled(false);
        }
        return super.onCreateOptionsMenu(menu);
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
            case R.id.imageButton6:
                btnSelected = 5;
                break;
            case R.id.imageButton7:
                btnSelected = 6;
                break;
            case R.id.imageButton8:
                btnSelected = 7;
                break;
            case R.id.imageButton9:
                btnSelected = 8;
                break;
            default:
                break;
        }

        Bundle Extras = new Bundle();
        if (this.CurrencyMapping != null && this.CurrencyMapping.size() > 0)
            Extras.putSerializable("hashMap", (HashMap<String, Float>) this.CurrencyMapping);
        Extras.putInt("btnSelected", btnSelected);
        if (cFragment != null) {
            cFragment.setArguments(Extras);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contentFragment, cFragment, "currency");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }

    public void setCurrencyMapping(Map<String, Float> map) {
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            CurrencyMapping.put(key, value);
        }
    }

    public void onItemClick(int mPosition) {
        HashMap<String, String> data = cFragment.getSelectedItem(mPosition);
        Bundle bundle = new Bundle();
        String country = data.get("Country");
        bundle.putString("selectedItem", country);
        bundle.putInt("btnSelected", btnSelected);
        if (mFragment != null) {
            mFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contentFragment, mFragment, "main");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.search:
                break;
            case R.id.action_settings:
                break;
            default:
                break;
        }
        return true;
        //return item.getItemId() == R.id.action_settings && super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Search words...");
        mSearchView.setSubmitButtonEnabled(false);
    }

    @Override
    public boolean onQueryTextChange(String queryText) {
        int id = getCurrentFragmentID();
        if (id == R.layout.fragment_currencylistview)
            cFragment.searchOnTheList(queryText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String queryText) {
        return false;
    }
}
