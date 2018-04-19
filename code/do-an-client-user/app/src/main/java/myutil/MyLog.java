package myutil;

import android.util.Log;

/**
 * Created by d on 11/15/2017.
 */

public class MyLog {
    private static boolean IS_SHOW = false;

    public static void e(Class t, String log) {
        if (MyLog.IS_SHOW)
            Log.e(MyLog.class.getSimpleName() + " " + t.getSimpleName(), log);
    }

    public static void d(Class t, String log) {
        if (MyLog.IS_SHOW)
            Log.d(MyLog.class.getSimpleName() + " " + t.getSimpleName(), log);
    }

    public static void v(Class t, String log) {
        if (MyLog.IS_SHOW)
            Log.v(MyLog.class.getSimpleName() + " " + t.getSimpleName(), log);
    }

    public static void w(Class t, String log) {
        if (MyLog.IS_SHOW)
            Log.w(MyLog.class.getSimpleName() + " " + t.getSimpleName(), log);
    }

    public static void setShow(boolean isShow) {
        IS_SHOW = isShow;
    }

}
