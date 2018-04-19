package myutil;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by d on 11/22/2017.
 */

public class MyService {
    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (MyService.class.getName().equals(
                    service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
