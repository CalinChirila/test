package com.example.calin.atnmtest.utils;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    public static final String CURRENCY_URL_STRING = "http://gnb.dev.airtouchmedia.com/rates.json";
    public static final String TRANSACTIONS_URL_STRING = "http://gnb.dev.airtouchmedia.com/transactions.json";

    public static URL buildURL(String urlString){
        if(urlString == null || TextUtils.isEmpty(urlString)) return null;

        URL url;
        try{
            url = new URL(urlString);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String makeHTTPRequest(URL url){

        HttpURLConnection urlConnection = null;
        StringBuilder stringBuilder = new StringBuilder();

        try{
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);

            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            while(scanner.hasNext()){
                stringBuilder.append(scanner.next());
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        // Return the json string
        return stringBuilder.toString();
    }
}
