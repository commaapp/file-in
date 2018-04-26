package wellcome;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.doan.R;
import com.google.gson.Gson;

import app.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import core.MainActivity;
import io.socket.emitter.Emitter;
import login.LoginActivity;
import map.MapsActivity;
import myutil.MyCache;
import myutil.MyLog;
import obj.Customer;
import obj.Driver;
import registor.RegistorActivity;
import service.MyService;

/**
 * Created by D on 4/19/2018.
 */

public class WellcomeActivity extends AppCompatActivity {
    @BindView(R.id.layout_wellcome)
    LinearLayout layoutWellcome;
    @BindView(R.id.pgbar_load)
    ProgressBar pgbarLoad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        ButterKnife.bind(this);
        loadFirstTime();
        connectMyService();


    }

    private MyService mMyService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            MyLog.e(MainActivity.class, "onServiceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder binder = (MyService.MyBinder) service;
            mMyService = binder.getMyService();
            MyLog.e(getClass(), "onServiceConnected");
            WellcomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkIsLogin();
                }
            });

        }
    };

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private void startMain() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void checkIsLogin() {
        String phoneNumber = MyCache.getStringValueByName(this, Config.MY_CACHE, Config.DRIPER_PHONE_NUMBER);
        if (phoneNumber.equals("")) {
            MyLog.e(getClass(), "showLayoutLogin");
            showLayoutWellcome();
        } else {
            mMyService.checkDriverIsExists(phoneNumber, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    WellcomeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MyLog.e(getClass(), args[0].toString());
                                if (Driver.fromJSON(args[0].toString()) instanceof Driver == true) {
                                    startMain();
                                }
                            } catch (NullPointerException e) {
                                showLayoutWellcome();
                            }
                        }

                    });
                }
            });
        }


        // login(phoneNumber);
    }

    private void showLayoutWellcome() {
        pgbarLoad.setVisibility(View.GONE);
        layoutWellcome.setVisibility(View.VISIBLE);
    }


    private void connectMyService() {
        Intent intentMyService = new Intent(this, MyService.class);
        bindService(intentMyService, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void loadFirstTime() {
        pgbarLoad.setVisibility(View.VISIBLE);
        layoutWellcome.setVisibility(View.GONE);
    }

    @OnClick({R.id.tv_driver_login, R.id.tv_driver_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_driver_login:
                Intent intent2 = new Intent(this, LoginActivity.class);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
//                finish();
                break;
            case R.id.tv_driver_register:
                Intent intent = new Intent(this, RegistorActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//                finish();
                break;
        }
    }
}
