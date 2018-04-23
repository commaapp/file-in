package main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import myutil.MyLog;
import service.MyService;

public class TemplateActivity extends AppCompatActivity {


    private MyService mMyService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectMyService();
    }

    private void stopMyService() {
        if (mMyService != null)
            mMyService.stopSelf();
        else stopService(new Intent(this, MyService.class));
    }

    private void startMyService() {
        startService(new Intent(this, MyService.class));
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            MyLog.e(MainActivity.class, "onServiceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder binder = (MyService.MyBinder) service;
            mMyService = binder.getMyService();
        }
    };

    private void connectMyService() {
        Intent intentMyService = new Intent(this, MyService.class);
        bindService(intentMyService, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onBackPressed() {
        unbindService(serviceConnection);
        super.onBackPressed();
    }
}
