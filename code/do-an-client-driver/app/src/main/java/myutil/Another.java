package myutil;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.USAGE_STATS_SERVICE;

/**
 * Created by d on 11/15/2017.
 */

public class Another {


    public static void getAllListApp(Context mContext) {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = mContext.getPackageManager();
//        ArrayList<ItemApplication> itemApps = new ArrayList<>();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(i, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            if (!mContext.getPackageName().equals(resolveInfo.activityInfo.packageName)) {
//                ItemApplication itemApp = new ItemApplication();
//                itemApp.setNamePackage(resolveInfo.activityInfo.packageName);
//                itemApp.setNameApp(resolveInfo.loadLabel(packageManager).toString());
//                itemApp.setLock(false);
//                itemApp.setRecommand(0);
//                itemApps.add(itemApp);
            }
        }
//        return itemApps;
    }

    /*
        <uses-permission
            android:name="android.permission.PACKAGE_USAGE_STATS"
            tools:ignore="ProtectedPermissions" />
        <uses-permission android:name="android.permission.GET_TASKS" />
     */
    private String getTopTask(Context context) {
        String topPackageName = "";
        ActivityManager mActivityManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mActivityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats)
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            topPackageName = mActivityManager.getRunningAppProcesses().get(0).processName;
        } else {
            topPackageName = (mActivityManager.getRunningTasks(1).get(0)).topActivity.getPackageName();
        }
        return topPackageName;
    }

    public static void getItemApplicationByPackageName(Context context, String pkg) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos)
            if (pkg.equals(resolveInfo.activityInfo.packageName)) {

            }
//        return itemApp;
    }

    public static Drawable getDrawableIconFromPackageName(Context context, String pkgName) {
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(pkgName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
