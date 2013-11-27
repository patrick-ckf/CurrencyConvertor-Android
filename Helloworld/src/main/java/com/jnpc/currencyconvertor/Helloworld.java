package com.jnpc.currencyconvertor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.Map;

public class Helloworld extends ActionBarActivity implements OnTaskCompleted {
    private Map<String, Float> CurrencyMapping = new HashMap<String,Float>();
    String selectedItem;
    private int btnSelected = -1;
    AdView adView;

    mainFragment mFragment = null;

    public void onTaskCompleted(Map<String, Float> map) {
        setCurrencyMapping(map);
        if (mFragment == null)
            mFragment = (mainFragment) getFragmentManager().findFragmentById(R.id.main_fragment);
        if (mFragment != null)
            mFragment.setCurrencyMapping(map);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Currency Convertor");
        setContentView(R.layout.activity_helloworld);

        if (savedInstanceState == null) {
            adView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            Bundle extra = getIntent().getExtras();

            if (extra != null) {
                selectedItem = extra.getString("selectedItem");
                btnSelected = extra.getInt("btnSelected");
            }
            if (mFragment == null) {
                mFragment = (mainFragment) getFragmentManager().findFragmentById(R.id.main_fragment);
            }
            if (mFragment != null) {
                mFragment.setSelectedItem(selectedItem);
                mFragment.setBtnSelected(btnSelected);
            }
            adView.loadAd(adRequest);
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
            case R.id.imageButton6:
                btnSelected = 5;
                break;
            case R.id.imageButton7:
                btnSelected = 6;
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
}
