package core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.doan.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import myutil.MyLog;
import service.MyService;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.switch1)
    Switch switch1;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.switch_stop)
    Switch switchStop;


    private MyService mMyService;
    private boolean isBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        connectMyService();
        startMyService();
        switchStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) stopMyService();
            }
        });
    }

    private void stopMyService() {
        if (mMyService != null)
            mMyService.stopSelf();
        else stopService(new Intent(this, MyService.class));
    }

    private void startMyService() {
        startService(new Intent(this, MyService.class));
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            MyLog.e(MainActivity.class, "onServiceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder binder = (MyService.MyBinder) service;
            mMyService = binder.getMyService();
            isBound = true;
            MyLog.e(MainActivity.class, "onServiceConnected");
            mMyService.getSocket().connect();
            mMyService.getSocket().emit("my other event","day la android");
        }
    };

    private void connectMyService() {
        final Intent intentMyService = new Intent(this, MyService.class);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) bindService(intentMyService, connection, Context.BIND_AUTO_CREATE);
                else {
                    if (isBound) {
                        unbindService(connection);
                        isBound = false;
                    }
                }
            }
        });
    }

    @OnClick(R.id.button)
    public void onViewClicked() {

    }

    @Override
    public void onBackPressed() {
        if (isBound) unbindService(connection);
        super.onBackPressed();
    }
}
