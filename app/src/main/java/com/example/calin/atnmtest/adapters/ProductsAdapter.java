package com.example.calin.atnmtest.adapters;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.calin.atnmtest.R;
import com.example.calin.atnmtest.data.TransactionContract;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsAdapterViewHolder> {

    private Cursor transactionsCursor;
    private final ProductsAdapterOnClickHandler mClickHandler;

    public ProductsAdapter(ProductsAdapterOnClickHandler handler){
        mClickHandler = handler;
    }

    public interface ProductsAdapterOnClickHandler{
        void onClick(Cursor cursor);
    }


    public class ProductsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mProductNameTextView;

        public ProductsAdapterViewHolder(View itemView) {
            super(itemView);

            mProductNameTextView = itemView.findViewById(R.id.tv_product_list_item);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            transactionsCursor.moveToPosition(getAdapterPosition());
            mClickHandler.onClick(transactionsCursor);
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
        transactionsCursor.moveToPosition(position);

        String productName = transactionsCursor.getString(transactionsCursor.getColumnIndex(TransactionContract.TransactionsEntry.COLUMN_PRODUCT_NAME));

        holder.mProductNameTextView.setText(productName);
    }

    @Override
    public int getItemCount() {
        if(transactionsCursor == null) return 0;
        return transactionsCursor.getCount();
    }

    public void setTransactionsData(Cursor data){
        transactionsCursor = data;
        notifyDataSetChanged();
    }
}
