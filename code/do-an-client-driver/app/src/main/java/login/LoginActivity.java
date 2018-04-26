package login;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doan.R;

import app.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import core.MainActivity;
import io.socket.emitter.Emitter;
import map.MapsActivity;
import myutil.MyCache;
import myutil.MyLog;
import obj.Driver;
import service.MyService;
import wellcome.WellcomeActivity;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.tv_back)
    ImageView tvBack;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.layout_login)
    LinearLayout layoutLogin;
    @BindView(R.id.pgbar_load)
    ProgressBar pgbarLoad;
    @BindView(R.id.layout_register)
    LinearLayout layoutRegister;
    @BindView(R.id.tv_txt_verify_phonenumber)
    TextView tvTxtVerifyPhonenumber;
    @BindView(R.id.layout_verify_phonenumber)
    LinearLayout layoutVerifyPhonenumber;
    @BindView(R.id.edt_phone_number)
    EditText edtPhoneNumber;
    @BindView(R.id.edt_code_verify)
    EditText edtCodeVerify;
    @BindView(R.id.tv_send_code_verify_again)
    TextView tvSendCodeVerifyAgain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkIsLogin();
                }
            });

        }
    };

    private void showLayoutLogin() {
        pgbarLoad.setVisibility(View.GONE);
        layoutLogin.setVisibility(View.VISIBLE);
    }

    private void startLogin() {
        showRegister();
        indexView = 0;
    }

    int indexView;

    private void showVerifyRegister() {
        indexView = 1;
        tvTxtVerifyPhonenumber.setText(tvTxtVerifyPhonenumber.getText().toString().replace("%d", edtPhoneNumber.getText().toString()));
        layoutVerifyPhonenumber.setVisibility(View.VISIBLE);
        layoutRegister.setVisibility(View.GONE);

    }

    private void showRegister() {
        indexView = 0;
        layoutVerifyPhonenumber.setVisibility(View.GONE);
        layoutRegister.setVisibility(View.VISIBLE);
    }

    private void checkIsLogin() {
        String phoneNumber = MyCache.getStringValueByName(this, Config.MY_CACHE, Config.DRIPER_PHONE_NUMBER);
        if (phoneNumber.equals("")) {
            showLayoutLogin();
            startLogin();
        } else {
            mMyService.checkDriverLogin(phoneNumber, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                MyLog.e(getClass(), args[0].toString());
//                                Customer customer = new Gson().fromJson(args[0].toString(), Customer.class);
//                                login(customer.getSdt(), customer.getName());
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

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    String verifyCode = "null";

    private void connectMyService() {
        Intent intentMyService = new Intent(this, MyService.class);
        bindService(intentMyService, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void loadFirstTime() {
        pgbarLoad.setVisibility(View.VISIBLE);
        layoutLogin.setVisibility(View.GONE);
    }


    private boolean isNullRegister() {
        if (edtPhoneNumber.getText().toString().isEmpty()) return true;
        return false;
    }

    @OnClick({R.id.tv_back, R.id.tv_next, R.id.tv_send_code_verify_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                switch (indexView) {
                    case 0:
                        finish();
                        return;
                    case 1:
                        showRegister();
                        return;
                }
                break;
            case R.id.tv_next:
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
                        tvNext.setClickable(false);
                        if (edtCodeVerify.getText().toString().contains(verifyCode)) {
                            mMyService.checkDriverIsExists(edtPhoneNumber.getText().toString(), new Emitter.Listener() {
                                @Override
                                public void call(final Object... args) {
                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                MyLog.e(getClass(), args[0].toString());
                                                if (Driver.fromJSON(args[0].toString()) instanceof Driver == true) {
                                                    MyCache.putStringValueByName(LoginActivity.this, Config.MY_CACHE, Config.DRIPER_PHONE_NUMBER, edtPhoneNumber.getText().toString());
                                                    startMain();
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "Số điện thoại này chưa đăng kí trờ thành đối tác của Driper", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            } catch (NullPointerException e) {
                                                Toast.makeText(LoginActivity.this, "Số điện thoại này chưa đăng kí trờ thành đối tác của Driper", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                            tvNext.setClickable(true);

                                        }

                                    });
                                }
                            });

                        } else {
                            Toast.makeText(this, "Mã xác nhận không chính xác.", Toast.LENGTH_SHORT).show();
                        }
                        return;
                }
                break;

            case R.id.tv_send_code_verify_again:
                requestVerifyCode();
                break;
        }
    }

    private void startMain() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void requestVerifyCode() {
        tvNext.setClickable(true);
        mMyService.driverVefifyPhonenumber(edtPhoneNumber.getText().toString(), new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        verifyCode = args[0].toString();
                        MyLog.e(getClass(), verifyCode);
                        Toast.makeText(LoginActivity.this, "Mã xác nhận " + verifyCode, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}
