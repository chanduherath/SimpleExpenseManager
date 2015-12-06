package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.app.Application;
import android.content.Context;

/**
 * Created by Chandu Herath on 12/6/2015.
 */
public class CustomApplication extends Application{
    private static Context context;
    public void onCreate(){
        context=getApplicationContext();
    }

    public static Context getCustomAppContext(){
        return context;
    }
}

