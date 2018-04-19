package myutil;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by d on 11/22/2017.
 */

public class MyPermission {
    public static boolean isGragUsageAccess(Context context) {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            boolean granted = false;
            AppOpsManager appOps = (AppOpsManager) context
                    .getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), context.getPackageName());
            if (mode == AppOpsManager.MODE_DEFAULT)
                granted = (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
            else granted = (mode == AppOpsManager.MODE_ALLOWED);
            return granted;
        } else return true;
    }

    public static void showDialogRequestPermisstion(final Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        TextView textView = new TextView(context);
        dialogBuilder.setTitle("Please grant application");
        textView.setPadding(20, 20, 20, 20);
        textView.setTextSize(18);
        textView.setText(Html.fromHtml("Application needs <b>Usage data access</b> permission to use in locking your apps."));
        dialogBuilder.setView(textView);
        dialogBuilder.setNegativeButton("Disagree", null);
        dialogBuilder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);

                context.startActivity(intent);
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
