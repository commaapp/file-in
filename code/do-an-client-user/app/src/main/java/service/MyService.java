package service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.doan.R;

import java.net.URISyntaxException;

import app.Config;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import myutil.MyCache;
import myutil.MyLog;
import obj.Book;
import obj.Customer;
import obj.Driver;

/**
 * Created by D on 4/13/2018.
 */

public class MyService extends Service {
    private Socket mSocket;
    private Emitter.Listener onNewOkBook = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
        }
    };
    private Emitter.Listener onNewOkBookCustomer = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };

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
    private Emitter.Listener onNotify = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MyLog.e(getClass(), args[0].toString());
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
        mSocket.on(Config.NOTIFY, onNotify);
        mSocket.on(Config.NEW_OK_BOOK, onNewOkBook);
        mSocket.on(Config.NEW_OK_BOOK_CUSTOMER, onNewOkBookCustomer);
        mSocket.connect();
        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    private void runForeground() {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Driper ")
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

    public void custemerRequestLogin(String sdt, Emitter.Listener onRequestRegister) {
        mSocket.on(Config.ON_REQUEST_LOGIN, onRequestRegister);
        mSocket.emit(Config.EMIT_REQUEST_LOGIN, sdt);
        MyLog.e(getClass(), sdt);

    }

    public void custemerVefifyLoginSucces(String s, Emitter.Listener listener) {
        mSocket.emit(Config.EMIT_REQUEST_LOGIN, s);
    }

    public void custemerVefifyPhonenumber(String phoneNumber, Emitter.Listener onVefify_Code_Res) {
        mSocket.on(Config.Vefify_Code_Res, onVefify_Code_Res);
        mSocket.emit(Config.Vefify_Phonenumber, phoneNumber);
    }

    public void custemerLogin(String jsonCustemer, Emitter.Listener listener) {
        mSocket.on(Config.Custemer_Login_Res, listener);
        mSocket.emit(Config.Custemer_Login, jsonCustemer);

    }

    public void checkLogin(String phoneNumber, Emitter.Listener listener) {
        mSocket.on(Config.Custemer_Check_Login_Res, listener);
        mSocket.emit(Config.Custemer_Check_Login, phoneNumber);
    }

    public void getCustomProfile(String phoneNumber, Emitter.Listener listener) {
        mSocket.on(Config.Custemer_View_Profile_Res, listener);
        mSocket.emit(Config.Custemer_View_Profile, phoneNumber);
    }

    public void updateCustomProfile(Customer customer, Emitter.Listener listener) {
        mSocket.on(Config.Custemer_Update_Profile_Res, listener);
        mSocket.emit(Config.Custemer_Update_Profile, customer.toJSON());
    }

    public void updateStateOnlineProfile(Customer customer) {
        mSocket.emit(Config.updateStateOnlineProfile, customer.toJSON());

    }

    public void updateCustomLocation(Customer customer) {
        mSocket.emit(Config.updateCustomLocation, customer.toJSON());
    }

    public void findDriperInLocation(Customer customer, Emitter.Listener listener) {
        mSocket.on(Config.findDriperInLocation_res, listener);
        mSocket.emit(Config.findDriperInLocation, customer.toJSON());
    }

    public void bookFindDriver(Book book, Emitter.Listener listener) {
        mSocket.on(Config.bookFindDriver_res, listener);
        mSocket.emit(Config.bookFindDriver, book.toJSON());
    }

    public void huyCuocXe() {
        mSocket.emit(Config.huyCuocXe, MyCache.getStringValueByName(this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER));
    }

    public void updateStarDriver(Driver driver) {
        mSocket.emit(Config.updateStarDriver, driver.toJSON());
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
