package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.CustomApplication;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistentAcountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistentTransactionDAO;

/**
 * Created by Chandu Herath on 12/6/2015.
 */
public class persistentExpenceManager extends ExpenseManager  {

    public persistentExpenceManager() {
        setup();
    }

    public void setup() {
        TransactionDAO pTraDAO = new persistentTransactionDAO(CustomApplication.getCustomAppContext());
        setTransactionsDAO(pTraDAO);

        AccountDAO pAcoDAO = new persistentAcountDAO(CustomApplication.getCustomAppContext());
        setAccountsDAO(pAcoDAO);
    }

}
