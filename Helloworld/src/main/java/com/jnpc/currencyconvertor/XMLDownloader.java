package com.jnpc.currencyconvertor;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class XMLDownloader extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        String ret = null;
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            File testDirectory = new File(Environment.getExternalStorageDirectory() + "/CurrencyConvertor/");
            if (!testDirectory.exists()) {
                if (!testDirectory.mkdir()) {
                    return "failed";
                }
            }
            FileOutputStream fileOutput = new FileOutputStream(testDirectory + "local.xml");

            InputStream inputStream = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int bufferLength;
            while ( (bufferLength = inputStream.read(buffer)) > 0 )
                fileOutput.write(buffer, 0, bufferLength);
            fileOutput.close();
            ret = "finish";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    protected void onPostExecute(String result) {
    }
}
