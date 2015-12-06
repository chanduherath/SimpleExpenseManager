package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Chandu Herath on 12/6/2015.
 */
public class persistentAcountDAO extends SQLiteOpenHelper implements AccountDAO {
    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static String DBNAME = "130201V.db";

    // Table names
    private static String TBLACCOUNT = "account";

    // Table columns name
    private static final String ACCOUNTNO = "account_no";
    private static final String BANKNAME = "bank_name";
    private static final String ACCOUNTHOLDERNAME = "holder_name";
    private static final String BALANCE = "balance";

    //create table query for table account
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + TBLACCOUNT + "(" + ACCOUNTNO + " TEXT NOT NULL, " + BANKNAME + " TEXT NOT NULL, " + ACCOUNTHOLDERNAME + " TEXT NOT NULL, " + BALANCE + " REAL NOT NULL)";

    public persistentAcountDAO(Context context){
        //super(context, DATABASE_NAME, null, DATABASE_VERSION);
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ACCOUNT);
    }

    public void addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACCOUNTNO, account.getAccountNo());
        values.put(BANKNAME, account.getBankName());
        values.put(ACCOUNTHOLDERNAME, account.getAccountHolderName());
        values.put(BALANCE, account.getBalance());

        db.insert(TBLACCOUNT, null, values);
        db.close();
    }

    public void removeAccount(String accountNo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBLACCOUNT, ACCOUNTNO + "='" + accountNo + "'", null);
        db.close();
    }

    public List<Account> getAccountsList(){
        List<Account> accountList = new ArrayList<Account>();
        String selectQuery="SELECT * FROM " + TBLACCOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
                accountList.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return accountList;
    }

    public List<String> getAccountNumbersList(){
        List<String> accountNoList = new ArrayList<String>();
        String selectQuery="SELECT " + ACCOUNTNO + " FROM " + TBLACCOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                accountNoList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return accountNoList;
    }

    public Account getAccount(String accountNo)throws InvalidAccountException{
        Account account=null;
        String selectQuery="SELECT * FROM " + TBLACCOUNT + " WHERE " + ACCOUNTNO + "='" + accountNo + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if(account!=null){
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                updateBalancetemp(accountNo, account.getBalance() - amount);
                break;
            case INCOME:
                updateBalancetemp(accountNo, account.getBalance() + amount);
                break;
        }
    }

    public void updateBalancetemp(String accountNo, double newAmount){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BALANCE, newAmount);

        db.update(TBLACCOUNT, values, ACCOUNTNO + "='" + accountNo + "'", null);
        db.close();
    }






}
