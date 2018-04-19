package myutil;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by D on 3/1/2018.
 */

public class UtilConnect {
    public boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

}
