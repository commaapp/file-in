package myutil;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by d on 11/15/2017.
 */

public class MyBitmap {

    public Bitmap rotateImage(Bitmap myBitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
        return myBitmap;
    }
    public static  byte[]  Base64ToByte(String encodedImage) {
        return Base64.decode(encodedImage, Base64.DEFAULT);
    }
    /**
     * Lưu {@link Bitmap} thành file PNG
     *
     * @param myBitmap
     * @param imagePath
     */
    public void saveImageFromBitmap(Bitmap myBitmap, String imagePath) {
        try {
            File imageFile = new File(imagePath);
            FileOutputStream fos = new FileOutputStream(imageFile);
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
        }
    }

    /**
     * Lật dọc {@link Bitmap} đầu vào
     *
     * @param src
     * @return
     */
    public Bitmap flipVertical(Bitmap src) {
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    /**
     * Lật ngang {@link Bitmap} đầu vào
     *
     * @param src
     * @return
     */
    public Bitmap flipHorizontal(Bitmap src) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public String saveBitmapToPNG(Bitmap mBitmap) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy" + "_" + "MM" + "_" + "dd" + "_" + "HH" + "_" + "mm" + "_" + "ss");
        String bmpName = format.format(date);
        File f = new File(Environment.getExternalStorageDirectory().getPath() + "/cacheGIF/" + bmpName + ".png");
        f.getParentFile().mkdirs();
        if (f.exists())
            f.delete();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getPath();
    }

    public Bitmap createBitmapFromView(View v) {
        v.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.draw(c);
        return b;
    }

    public String saveViewToPNG(View view) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".png";

            // create myBitmap screen capture
//            View v1 = getWindow().getDecorView().getRootView();
//         getWindow().getDecorView().getRootView();
            view.setDrawingCacheEnabled(true);
//            Bitmap myBitmap = Bitmap.createBitmap(v1.getDrawingCache());
            Bitmap myBitmap = Bitmap.createBitmap(createBitmapFromView(view));
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            myBitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

//            openScreenshot(imageFile);
            return mPath;
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        return "";
    }

    public Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap myBitmap = null;
        try {
            istr = assetManager.open(filePath);
            myBitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
        }
        return myBitmap;
    }

    public Bitmap convertByteArrayToBitmap(byte[] byteArrayToBeCOnvertedIntoBitMap) {
        Bitmap bitMapImage;
        bitMapImage = BitmapFactory.decodeByteArray(
                byteArrayToBeCOnvertedIntoBitMap, 0,
                byteArrayToBeCOnvertedIntoBitMap.length);
        return bitMapImage;
    }

    public Bitmap creatBitmapFromPath(String filePath) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmapOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, bitmapOptions);
    }

    public byte[] convertDrawableToByteArray(Drawable iconApp) {
        BitmapDrawable bitDw = ((BitmapDrawable) iconApp);
        Bitmap bitmap = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
        return stream.toByteArray();
    }
}
