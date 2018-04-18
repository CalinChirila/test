package com.example.calin.atnmtest.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.calin.atnmtest.data.TransactionContract;

public class TransactionsContentProvider extends ContentProvider {

    // Codes for the UriMatcher
    public static final int CURRENCY = 100;
    public static final int TRANSACTIONS = 200;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(TransactionContract.AUTHORITY, TransactionContract.PATH_CURRENCY, CURRENCY);
        uriMatcher.addURI(TransactionContract.AUTHORITY, TransactionContract.PATH_TRANSACTIONS, TRANSACTIONS);

        return uriMatcher;
    }

    private TransactionsDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        // Initialize the SQLiteOpenHelper
        mDbHelper = new TransactionsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        switch(match){
            case CURRENCY:
                cursor = db.query(
                        TransactionContract.CurrencyEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRANSACTIONS:
                cursor = db.query(
                        TransactionContract.TransactionsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch(match){
            case CURRENCY:
                long id = db.insert(TransactionContract.CurrencyEntry.TABLE_NAME, null, values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(TransactionContract.CurrencyEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into uri: " + uri);
                }
                break;
            case TRANSACTIONS:
                long id1 = db.insert(TransactionContract.TransactionsEntry.TABLE_NAME, null, values);
                if(id1 > 0){
                    returnUri = ContentUris.withAppendedId(TransactionContract.TransactionsEntry.CONTENT_URI, id1);
                } else {
                    throw new SQLException("Failed to insert row into uri: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unable to insert item with uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int deletedRows;

        switch(match){
            case CURRENCY:
                deletedRows = db.delete(TransactionContract.CurrencyEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRANSACTIONS:
                deletedRows = db.delete(TransactionContract.TransactionsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unable to delete item(s) with uri: " + uri);
        }

        if(deletedRows > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
