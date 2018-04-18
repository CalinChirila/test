package com.example.calin.atnmtest.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calin.atnmtest.data.TransactionContract.CurrencyEntry;
import com.example.calin.atnmtest.data.TransactionContract.TransactionsEntry;

public class JSONUtils {

    // Populate the currency rates table with the results from json
    public static void parseCurrencyJsonResponse(Context context, String currencyJson) throws JSONException{
        JSONArray rootJson = new JSONArray(currencyJson);

        ContentResolver contentResolver = context.getContentResolver();
        // Exit early if we already have data
        Cursor cursor = contentResolver.query(CurrencyEntry.CONTENT_URI, null, null, null, null);
        if(cursor.getCount() > 0){
            cursor.close();
            return;
        }

        for (int i = 0; i < rootJson.length(); i++) {
            JSONObject currencyObject = rootJson.getJSONObject(i);
            String fromCurrency = currencyObject.getString("from");
            String toCurrency = currencyObject.getString("to");
            String rate = currencyObject.getString("rate");

            ContentValues values = new ContentValues();
            values.put(CurrencyEntry.COLUMN_CURRENCY_FROM, fromCurrency);
            values.put(CurrencyEntry.COLUMN_CURRENCY_TO, toCurrency);
            values.put(CurrencyEntry.COLUMN_CURRENCY_RATE, rate);

            contentResolver.insert(CurrencyEntry.CONTENT_URI, values);
        }
    }


    // Populate the transactions table with the results from json
    public static void parseTransactionsJsonResponse(Context context, String transactionsJson) {
        try {
            JSONArray rootJson = new JSONArray(transactionsJson);
            ContentResolver contentResolver = context.getContentResolver();

            Cursor cursor = contentResolver.query(TransactionsEntry.CONTENT_URI, null, null, null, null);
            if(cursor.getCount() > 0){
                cursor.close();
                return;
            }

            for (int i = 0; i < rootJson.length(); i++) {
                JSONObject transactionObject = rootJson.getJSONObject(i);
                String productName = transactionObject.getString("sku");
                String productAmount = transactionObject.getString("amount");
                String currency = transactionObject.getString("currency");

                ContentValues values = new ContentValues();
                values.put(TransactionsEntry.COLUMN_PRODUCT_NAME, productName);
                values.put(TransactionsEntry.COLUMN_PRODUCT_AMOUNT, productAmount);
                values.put(TransactionsEntry.COLUMN_CURRENCY, currency);

                contentResolver.insert(TransactionsEntry.CONTENT_URI, values);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
