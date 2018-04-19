package myutil;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by d on 11/15/2017.
 */

public class MyFile {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void copy(File fileNguon, File fileDich) throws IOException {
        try (InputStream in = new FileInputStream(fileNguon)) {
            try (OutputStream out = new FileOutputStream(fileDich)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }

    }

    public static String readFileFromAssest(Context context, String path) {
        String data = null;
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            data = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return data;

    }
}
