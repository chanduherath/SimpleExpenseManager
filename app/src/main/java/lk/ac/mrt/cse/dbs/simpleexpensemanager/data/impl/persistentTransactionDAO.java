package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Chandu Herath on 12/6/2015.
 */
public class persistentTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    private static final int DATABASE_VERSION = 1;
    private static String DBNAME = "130201V.db";

    // Table names
    private static String TBLTRANSACTION = "transactions";

    // Table columns name
    private static final String ACCOUNTNO = "account_no";
    private static final String DATE = "date";
    private static final String EXPENCETYPE = "expense_type";
    private static final String AMOUNT = "amount";

    //create table query for table transaction
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TBLTRANSACTION + "(" + DATE + " TEXT NOT NULL, " + ACCOUNTNO + " TEXT NOT NULL, " + EXPENCETYPE + " TEXT NOT NULL, " + AMOUNT + " REAL NOT NULL)";


    public persistentTransactionDAO(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TRANSACTION);
    }

    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount){
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = sdf.format(transaction.getDate());

        ContentValues values = new ContentValues();
        values.put(DATE, date1);
        values.put(ACCOUNTNO, transaction.getAccountNo());
        values.put(EXPENCETYPE, transaction.getExpenseType().toString());
        values.put(AMOUNT, transaction.getAmount());

        db.insert(TBLTRANSACTION, null, values);
        db.close();
    }

    public List<Transaction> getAllTransactionLogs(){
        List<Transaction> transactionList = new ArrayList<Transaction>();
        String selectQuery="SELECT * FROM " + TBLTRANSACTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Date date = null;
                String dateString = cursor.getString(0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = sdf.parse(dateString);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    Log.e("Database Handler", "Error converting String to Date. " + e.toString());
                }

                Transaction transaction = new Transaction(date, cursor.getString(1), ExpenseType.valueOf(cursor.getString(2)), cursor.getDouble(3));
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }

    public List<Transaction> getPaginatedTransactionLogs(int limit){
        List<Transaction> transactionList = new ArrayList<Transaction>();
        String selectQuery="SELECT * FROM " + TBLTRANSACTION + " ORDER BY " + DATE + " ASC LIMIT " + limit;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                Date date = null;
                String dateString = cursor.getString(0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = sdf.parse(dateString);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    Log.e("Database Handler", "Error converting String to Date. " + e.toString());
                }

                Transaction transaction = new Transaction(date, cursor.getString(1), ExpenseType.valueOf(cursor.getString(2)), cursor.getDouble(3));
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return transactionList;
    }








}
