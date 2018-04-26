package login;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doan.R;
import com.google.gson.Gson;

import app.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.emitter.Emitter;
import main.MapsActivity;
import myutil.MyCache;
import myutil.MyLog;
import obj.Customer;
import service.MyService;

/**
 * Created by D on 4/17/2018.
 */

public class LoginActivity extends Activity {

    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.im_back)
    ImageView imBack;
    @BindView(R.id.edt_phone_number)
    EditText edtPhoneNumber;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.layout_register)
    LinearLayout layoutRegister;
    @BindView(R.id.tv_txt_verify_phone_register)
    TextView tvTxtVerifyPhoneRegister;
    @BindView(R.id.edt_code_verify)
    EditText edtCodeVerify;
    @BindView(R.id.tv_send_code_verify_again)
    TextView tvSendCodeVerifyAgain;
    @BindView(R.id.layout_verify_register)
    LinearLayout layoutVerifyRegister;
    int indexView;
    @BindView(R.id.layout_login)
    LinearLayout layoutLogin;
    @BindView(R.id.pgbar_load)
    ProgressBar pgbarLoad;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loadFirstTime();
        connectMyService();
    }

    private void showLayoutLogin() {
        pgbarLoad.setVisibility(View.GONE);
        layoutLogin.setVisibility(View.VISIBLE);
    }

    private void loadFirstTime() {
        pgbarLoad.setVisibility(View.VISIBLE);
        layoutLogin.setVisibility(View.GONE);
    }


    private void checkIsLogin() {
        String phoneNumber = MyCache.getStringValueByName(this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER);
        if (phoneNumber.equals("")) {
            showLayoutLogin();
            startLogin();
        } else {
            mMyService.checkLogin(phoneNumber, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MyLog.e(getClass(), args[0].toString());
                                Customer customer = new Gson().fromJson(args[0].toString(), Customer.class);
                                login(customer.getSdt(), customer.getName());
                            } catch (NullPointerException e) {
                                MyLog.e(getClass(), "NullPointerException checkLogin");
                                showLayoutLogin();
                                startLogin();
                            }


                        }

                    });
                }
            });
        }
        // login(phoneNumber);
    }

    private void startLogin() {
        showRegister();
        indexView = 0;
    }

    private void login(final String phoneNumber, String name) {
        mMyService.custemerLogin(new Customer(name, phoneNumber).toJSON(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyCache.putStringValueByName(LoginActivity.this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER, phoneNumber);
                        startMain();
                    }
                });
            }
        });
    }

    private void startMain() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void showVerifyRegister() {
        indexView = 1;
        tvTxtVerifyPhoneRegister.setText(tvTxtVerifyPhoneRegister.getText().toString().replace("%d", edtPhoneNumber.getText().toString()));
        layoutVerifyRegister.setVisibility(View.VISIBLE);
        layoutRegister.setVisibility(View.GONE);

    }

    private void showRegister() {
        indexView = 0;
        layoutVerifyRegister.setVisibility(View.GONE);
        layoutRegister.setVisibility(View.VISIBLE);
    }

    private MyService mMyService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder binder = (MyService.MyBinder) service;
            mMyService = binder.getMyService();
            MyLog.e(getClass(), "onServiceConnected");
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkIsLogin();
                }
            });

        }
    };

    private void connectMyService() {
        Intent intentMyService = new Intent(this, MyService.class);
        bindService(intentMyService, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private boolean isNullRegister() {
        if (edtName.getText().toString().isEmpty()) return true;
        if (edtPhoneNumber.getText().toString().isEmpty()) return true;
        return false;
    }

    @OnClick(R.id.im_back)
    public void onImBackClicked() {
        switch (indexView) {
            case 0:
                finish();
                return;
            case 1:
                showRegister();
                return;
        }
    }

    String verifyCode = "null";

    @OnClick(R.id.tv_next)
    public void onTvNextClicked() {
        switch (indexView) {
            case 0:
                if (isNullRegister()) {
                    Toast.makeText(this, "Không được để trống các trường", Toast.LENGTH_SHORT).show();
                } else {
                    showVerifyRegister();
                    requestVerifyCode();
                }
                return;
            case 1:
                if (edtCodeVerify.getText().toString().contains(verifyCode)) {
                    Toast.makeText(LoginActivity.this, "Chào mừng đến với " + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
                    login(edtPhoneNumber.getText().toString(), edtName.getText().toString());
                } else {
                    Toast.makeText(this, "Mã xác nhận không chính xác.", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void requestVerifyCode() {
        mMyService.custemerVefifyPhonenumber(edtPhoneNumber.getText().toString(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                verifyCode = args[0].toString();
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Mã xác nhận " + verifyCode, Toast.LENGTH_SHORT).show();
                    }
                });

                MyLog.e(getClass(), verifyCode);
            }
        });
    }


    @OnClick(R.id.tv_send_code_verify_again)
    public void onTvSendCodeVerifyAgainClicked() {
        requestVerifyCode();
    }
}
