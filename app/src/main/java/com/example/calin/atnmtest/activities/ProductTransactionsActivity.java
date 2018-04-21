package com.example.calin.atnmtest.activities;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.calin.atnmtest.R;
import com.example.calin.atnmtest.data.TransactionContract;
import com.example.calin.atnmtest.data.TransactionsDbHelper;
import com.example.calin.atnmtest.utils.CurrencyUtils;
import com.example.calin.atnmtest.utils.JSONUtils;
import com.example.calin.atnmtest.utils.NetworkUtils;

import org.json.JSONException;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductTransactionsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView mProductNameTextView;
    TextView mProductAmountTextView;
    TextView mProductCurrencyTextView;
    TextView mTotalTextView;

    String mProductName;
    Cursor mCursor;

    private static final int CURRENCY_LOADER_ID = 2;

    private LoaderManager.LoaderCallbacks<String> mCurrencyCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_transactions);


        mProductNameTextView = findViewById(R.id.tv_product_name);
        mProductAmountTextView = findViewById(R.id.tv_product_amount);
        mProductCurrencyTextView = findViewById(R.id.tv_product_currency);
        mTotalTextView = findViewById(R.id.tv_total);

        CurrencyUtils.setCurrencyContext(this);


        // Here will be a bunch of TextViews with information passed on from the main activity
        mProductName = getIntent().getStringExtra(MainActivity.EXTRA_PRODUCT_NAME);

        mCurrencyCallback = new LoaderManager.LoaderCallbacks<String>(){
            @Override
            public Loader<String> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<String>(getApplicationContext()) {
                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }

                    @Override
                    public String loadInBackground() {
                        // Populate the exchange rates database
                        URL currencyURL = NetworkUtils.buildURL(NetworkUtils.CURRENCY_URL_STRING);
                        String currencyJson = NetworkUtils.makeHTTPRequest(currencyURL);

                        return currencyJson;
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<String> loader, String data) {
                try {
                    JSONUtils.parseCurrencyJsonResponse(getApplicationContext(), data);
                } catch (JSONException e){e.printStackTrace();}
                getLoaderManager().initLoader(0, null, ProductTransactionsActivity.this);
            }

            @Override
            public void onLoaderReset(Loader<String> loader) {

            }
        };

        getLoaderManager().initLoader(CURRENCY_LOADER_ID, null, mCurrencyCallback);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getApplicationContext()) {
            @Override
            protected void onStartLoading() {
                if(mCursor == null) {
                    forceLoad();
                } else {
                    deliverResult(mCursor);
                }
            }

            @Override
            public Cursor loadInBackground() {
                String selection = TransactionContract.TransactionsEntry.COLUMN_PRODUCT_NAME + "=?";
                String[] selectionArgs = {mProductName};
                Cursor cursor = getContentResolver().query(TransactionContract.TransactionsEntry.CONTENT_URI, null, selection, selectionArgs, null);
                return cursor;
            }

            @Override
            public void deliverResult(Cursor data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        float total = 0;

        mCursor = data;


        mProductNameTextView.setText(mProductName);
        mProductCurrencyTextView.setText("");
        mProductAmountTextView.setText("");

        while (data.moveToNext()) {

            String productCurrency = data.getString(data.getColumnIndex(TransactionContract.TransactionsEntry.COLUMN_CURRENCY));
            mProductCurrencyTextView.append(productCurrency + "\n");
            String productAmount = data.getString(data.getColumnIndex(TransactionContract.TransactionsEntry.COLUMN_PRODUCT_AMOUNT));
            mProductAmountTextView.append(productAmount + "\n");

            // At each iteration, convert product amount to desired currency and add it to total
            CurrencyUtils currencyUtils = new CurrencyUtils();
            float rates = currencyUtils.getRates(productCurrency, "EUR");
            total = total + (rates * Float.parseFloat(productAmount));


        }

        mTotalTextView.setText(String.valueOf(total));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
