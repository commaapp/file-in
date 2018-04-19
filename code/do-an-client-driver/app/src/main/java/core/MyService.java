package core;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.doan.R;

import java.net.URISyntaxException;

import app.Config;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import myutil.MyLog;

/**
 * Created by D on 4/13/2018.
 */

public class MyService extends Service {


    private Socket mSocket;
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MyLog.e(getClass(), args[0].toString());
        }
    };

    {
        try {
            mSocket = IO.socket(Config.SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Driper ")
                .setContentText("Đang hoạt động")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .build();
        startForeground(101, notification);
        mSocket.on("news", onNewMessage);

        return super.onStartCommand(intent, flags, startId);
    }

    private IBinder binder;

    @Override
    public void onCreate() {
        Log.e("ServiceDemo", "Đã gọi onCreate()");
        binder = new MyBinder();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("ServiceDemo", "Đã gọi onBind()");
        return binder;
    }

    // Kết thúc một Service
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("ServiceDemo", "Đã gọi onUnbind()");
        return super.onUnbind(intent);
    }


    public class MyBinder extends Binder {
        public MyService getMyService() {
            return MyService.this;
        }
    }

    @Override
    public void onDestroy() {
        Log.e("ServiceDemo", "Đã gọi onDestroy()");
        super.onDestroy();
    }
}
