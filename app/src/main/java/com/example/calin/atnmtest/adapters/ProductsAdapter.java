package com.example.calin.atnmtest.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.calin.atnmtest.R;
import com.example.calin.atnmtest.data.TransactionContract;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsAdapterViewHolder> {

    private Cursor productsCursor;
    private final ProductsAdapterOnClickHandler mClickHandler;
    private int productCount;
    private Context mContext;

    private ArrayList<String> productsArray;

    public ProductsAdapter(Context context, ProductsAdapterOnClickHandler handler) {
        mContext = context;
        mClickHandler = handler;
    }

    public interface ProductsAdapterOnClickHandler {
        void onClick(Cursor cursor);
    }


    public class ProductsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mProductNameTextView;

        public ProductsAdapterViewHolder(View itemView) {
            super(itemView);

            mProductNameTextView = itemView.findViewById(R.id.tv_product_list_item);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            productsCursor.moveToPosition(getAdapterPosition());
            mClickHandler.onClick(productsCursor);
        }
    }

    @NonNull
    @Override
    public ProductsAdapter.ProductsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list_item, parent, false);
        return new ProductsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ProductsAdapterViewHolder holder, int position) {

        String productName = productsArray.get(position);

        holder.mProductNameTextView.setText(productName);
    }

    @Override
    public int getItemCount() {
        return productCount;
    }

    public void setTransactionsData(Cursor data) {
        productsCursor = removeDuplicateProducts(data);
        notifyDataSetChanged();
    }

    private Cursor removeDuplicateProducts(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) return null;
        productCount = cursor.getCount();
        productsArray = new ArrayList<>();
        while (cursor.moveToNext()) {
            String productName = cursor.getString(cursor.getColumnIndex(TransactionContract.TransactionsEntry.COLUMN_PRODUCT_NAME));
            if (productsArray.contains(productName)) {
                productCount = productCount - 1;
            } else {
                productsArray.add(productName);
            }
        }

        return cursor;
    }
}
