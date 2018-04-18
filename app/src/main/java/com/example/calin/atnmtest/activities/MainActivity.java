package com.example.calin.atnmtest.activities;


import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.calin.atnmtest.R;
import com.example.calin.atnmtest.adapters.ProductsAdapter;
import com.example.calin.atnmtest.data.TransactionContract;
import com.example.calin.atnmtest.utils.JSONUtils;
import com.example.calin.atnmtest.utils.NetworkUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ProductsAdapter.ProductsAdapterOnClickHandler{


    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;

    private LoaderManager.LoaderCallbacks<Cursor> mTransactionsCallback;

    private LinearLayoutManager layoutManager;
    private ProductsAdapter mProductsAdapter;

    private static final int TRANSACTIONS_LOADER_ID = 1;

    public static final String EXTRA_PRODUCT_NAME = "productName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = findViewById(R.id.rv_products_list);
        mProgressBar = findViewById(R.id.progress_bar);

        mTransactionsCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new android.content.AsyncTaskLoader<Cursor>(getApplicationContext()) {
                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }

                    @Override
                    public Cursor loadInBackground() {

                        // Populate the transactions database
                        URL transactionsURL = NetworkUtils.buildURL(NetworkUtils.TRANSACTIONS_URL_STRING);
                        String transactionsJson = NetworkUtils.makeHTTPRequest(transactionsURL);
                        JSONUtils.parseTransactionsJsonResponse(getContext(), transactionsJson);

                        Cursor cursor = getContentResolver().query(TransactionContract.TransactionsEntry.CONTENT_URI, null, null, null, null);

                        return cursor;
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

                mProductsAdapter = new ProductsAdapter(MainActivity.this);
                mProductsAdapter.setTransactionsData(data);

                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(mProductsAdapter);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };


        getLoaderManager().initLoader(TRANSACTIONS_LOADER_ID, null, mTransactionsCallback);
    }

    @Override
    public void onClick(Cursor cursor) {
        // Launch activity in which you will query the cursor for the items with the name of the product
        // that the user clicked on
        Intent intent = new Intent(MainActivity.this, ProductTransactionsActivity.class);
        String productName = cursor.getString(cursor.getColumnIndex(TransactionContract.TransactionsEntry.COLUMN_PRODUCT_NAME));
        intent.putExtra(EXTRA_PRODUCT_NAME, productName);

        startActivity(intent);

    }
}
