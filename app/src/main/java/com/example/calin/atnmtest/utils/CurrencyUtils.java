package com.example.calin.atnmtest.utils;

import android.content.Context;
import android.database.Cursor;

import com.example.calin.atnmtest.data.TransactionContract;

public class CurrencyUtils {
    static Cursor currencyCursor;
    static Cursor allCurrencyRatesCursor;
    static Context mContext;
    static float rates = 1;
    static float returnRates = 1;


    public static float getRates(String from, String to){
        if(from.equals(to)) return 1;
        boolean isConvertible = false;
        // Query currency cursor for all items with value of "to" into the column_currency_to
        String selection = TransactionContract.CurrencyEntry.COLUMN_CURRENCY_TO + "=?";
        String[] selectionArgs = {to};
        currencyCursor = mContext.getContentResolver().query(TransactionContract.CurrencyEntry.CONTENT_URI, null, selection, selectionArgs, null);


        // Check if at least one value in fromCurrency column is equal to our "from" currency
        while(currencyCursor.moveToNext()){
            if(currencyCursor.getString(currencyCursor.getColumnIndex(TransactionContract.CurrencyEntry.COLUMN_CURRENCY_FROM)).equals(from)){
                isConvertible = true;
                break;
            }
        }

        if(isConvertible){
            rates = rates * Float.parseFloat(currencyCursor.getString(currencyCursor.getColumnIndex(TransactionContract.CurrencyEntry.COLUMN_CURRENCY_RATE)));
            returnRates = rates;
            rates = 1;
            return returnRates;
        } else {
            currencyCursor.moveToPrevious();
            rates = rates * Float.parseFloat(currencyCursor.getString(currencyCursor.getColumnIndex(TransactionContract.CurrencyEntry.COLUMN_CURRENCY_RATE)));
            if(allCurrencyRatesCursor == null) {
                allCurrencyRatesCursor = mContext.getContentResolver().query(TransactionContract.CurrencyEntry.CONTENT_URI, null, null, null, null);
            }
            String newFrom;
            if(allCurrencyRatesCursor.moveToNext()) {

                newFrom = allCurrencyRatesCursor.getString(allCurrencyRatesCursor.getColumnIndex(TransactionContract.CurrencyEntry.COLUMN_CURRENCY_FROM));
            } else {
                allCurrencyRatesCursor.moveToFirst();
                newFrom = allCurrencyRatesCursor.getString(allCurrencyRatesCursor.getColumnIndex(TransactionContract.CurrencyEntry.COLUMN_CURRENCY_FROM));
            }
            getRates(newFrom, from);
        }

        return returnRates;
    }

    public static void setCurrencyContext(Context context){
        mContext = context;
    }

}
