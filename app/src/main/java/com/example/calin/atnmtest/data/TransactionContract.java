package com.example.calin.atnmtest.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URL;

public class TransactionContract {

    public static final String AUTHORITY = "com.example.calin.atnmtest";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_CURRENCY = "currency";
    public static final String PATH_TRANSACTIONS = "transactions";

    public static final class CurrencyEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_CURRENCY)
                .build();

        public static final String TABLE_NAME = "currencyRates";

        public static final String COLUMN_CURRENCY_FROM = "fromCurrency";
        public static final String COLUMN_CURRENCY_TO = "toCurrency";
        public static final String COLUMN_CURRENCY_RATE = "rate";
    }

    public static final class TransactionsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_TRANSACTIONS)
                .build();

        public static final String TABLE_NAME = "transactions";

        public static final String COLUMN_PRODUCT_NAME = "product";
        public static final String COLUMN_PRODUCT_AMOUNT = "amount";
        public static final String COLUMN_CURRENCY = "currency";
    }
}
