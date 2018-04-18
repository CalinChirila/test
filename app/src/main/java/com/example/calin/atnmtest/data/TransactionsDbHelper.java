package com.example.calin.atnmtest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.calin.atnmtest.data.TransactionContract.CurrencyEntry;
import com.example.calin.atnmtest.data.TransactionContract.TransactionsEntry;

public class TransactionsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "transactions.db";
    private static final int DATABASE_VERSION = 1;

    // SQL statement for creating the currency table
    private static final String SQL_CREATE_CURRENCY_TABLE =
            "CREATE TABLE " + CurrencyEntry.TABLE_NAME + " ("
                    + CurrencyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CurrencyEntry.COLUMN_CURRENCY_FROM + " TEXT, "
                    + CurrencyEntry.COLUMN_CURRENCY_TO + " TEXT, "
                    + CurrencyEntry.COLUMN_CURRENCY_RATE + " TEXT);";


    // SQL statement for creating the transactions table
    private static final String SQL_CREATE_TRANSACTIONS_TABLE =
            "CREATE TABLE " + TransactionsEntry.TABLE_NAME + " ("
                    + TransactionsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TransactionsEntry.COLUMN_PRODUCT_NAME + " TEXT, "
                    + TransactionsEntry.COLUMN_PRODUCT_AMOUNT + " TEXT, "
                    + TransactionsEntry.COLUMN_CURRENCY + " TEXT);";

    // SQL statement for deleting the currency table
    public static final String SQL_DELETE_CURRENCY_TABLE = "DROP TABLE IF EXISTS " + CurrencyEntry.TABLE_NAME;

    // SQL statement for deleting the transactions table
    private static final String SQL_DELETE_TRANSACTIONS_TABLE = "DROP TABLE IF EXISTS " + TransactionsEntry.TABLE_NAME;

    public TransactionsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CURRENCY_TABLE);
        Log.v("IMPORTANT", SQL_CREATE_CURRENCY_TABLE);
        db.execSQL(SQL_CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CURRENCY_TABLE);
        db.execSQL(SQL_DELETE_TRANSACTIONS_TABLE);

        onCreate(db);
    }
}
