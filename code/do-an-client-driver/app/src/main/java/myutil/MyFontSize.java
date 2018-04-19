package myutil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by d on 11/17/2017.
 */

public class MyFontSize {
    private Context mContext;

    public MyFontSize(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isSettingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(mContext);
        }
        return true;
    }

    public float getFontScale() {
        return mContext.getResources().getConfiguration().fontScale;
    }

    public void putSettingsFont(float size) {
        if (isSettingPermission()) {
            Settings.System.putFloat(mContext.getContentResolver(), Settings.System.FONT_SCALE, size);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(mContext)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + mContext.getPackageName()));
                    mContext.startActivity(intent);
                }
            }
        }
    }
}
