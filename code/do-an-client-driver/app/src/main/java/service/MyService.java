package service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
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
import obj.Customer;

/**
 * Created by D on 4/13/2018.
 */

public class MyService extends Service {
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(Config.SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Emitter.Listener CLIENT_CUSTEMER_CONNECT = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MyLog.e(getClass(), args[0].toString());
        }
    };

    public Socket getSocket() {
        return mSocket;
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MyLog.e(getClass(), "onConnect ");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MyLog.e(getClass(), "onDisconnect " + args[0].toString());
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MyLog.e(getClass(), "onConnectError ");
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.setShow(true);
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Config.CLIENT_CUSTEMER_CONNECT, CLIENT_CUSTEMER_CONNECT);


        mSocket.connect();
        return START_NOT_STICKY;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void runForeground() {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Driver ")
                .setContentText("Đang hoạt động")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .build();
        startForeground(101, notification);
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


    public void checkDriverLogin(String phoneNumber, Emitter.Listener listener) {
        mSocket.on(Config.Custemer_Check_Login_Res, listener);
        mSocket.emit(Config.Custemer_Check_Login, phoneNumber);
    }

    public void registerDriver(String json, Emitter.Listener listener) {
        mSocket.on(Config.Driver_Regitor_Res, listener);
        mSocket.emit(Config.Driver_Regitor, json);
    }

    public void driverVefifyPhonenumber(String phoneNumber, Emitter.Listener listener) {
        mSocket.on(Config.Driper_Vefify_Code_Res, listener);
        mSocket.emit(Config.Driper_Vefify_Phonenumber, phoneNumber);
    }

    public void checkDriverIsExists(String phoneNumber, Emitter.Listener listener) {
        mSocket.on(Config.checkDriverIsExists_Res, listener);
        mSocket.emit(Config.checkDriverIsExists, phoneNumber);
    }

    public void getDriverProfile(String stringValueByName, Emitter.Listener listener) {
        mSocket.on(Config.getDriverProfile_Res, listener);
        mSocket.emit(Config.getDriverProfile, stringValueByName);
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
