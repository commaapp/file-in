package app;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import myutil.MyLog;

/**
 * Created by D on 4/16/2018.
 */

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.setShow(true);
    }
}
