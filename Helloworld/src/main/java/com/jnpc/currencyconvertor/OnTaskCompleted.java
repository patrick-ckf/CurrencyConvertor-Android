package com.jnpc.currencyconvertor;

import java.util.Map;

public interface OnTaskCompleted {
    void onTaskCompleted(Map<String, Float> map);
    public void retryDownloadingXML();
}
