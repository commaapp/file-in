package profile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doan.R;
import com.google.gson.Gson;

import app.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.emitter.Emitter;
import login.LoginActivity;
import main.MainActivity;
import myutil.MyCache;
import myutil.MyLog;
import obj.Customer;
import service.MyService;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.im_back)
    ImageView imBack;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.pgbar_load)
    ProgressBar pgbarLoad;
    @BindView(R.id.layout_profile)
    LinearLayout layoutProfile;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);
        ButterKnife.bind(this);
        loadFirstTime();
        connectMyService();
    }

    private void showLayoutProfile() {
        pgbarLoad.setVisibility(View.GONE);
        layoutProfile.setVisibility(View.VISIBLE);
    }

    private void loadFirstTime() {
        pgbarLoad.setVisibility(View.VISIBLE);
        layoutProfile.setVisibility(View.GONE);
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

            mMyService.getCustomProfile(MyCache.getStringValueByName(ProfileActivity.this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER),
                    new Emitter.Listener() {
                        @Override
                        public void call(final Object... args) {
                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showLayoutProfile();
                                    Customer customer = new Gson().fromJson(args[0].toString(), Customer.class);
                                    tvPhoneNumber.setText(customer.getSdt());
                                    edtName.setText(customer.getName());
                                }
                            });
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

    @OnClick({R.id.im_back, R.id.tv_save, R.id.logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.tv_save:
                Customer customer = new Customer(edtName.getText().toString(), MyCache.getStringValueByName(ProfileActivity.this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER));
                mMyService.updateCustomProfile(customer, new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        ProfileActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(layoutProfile, "Đã cập nhật thông tin của bạn", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
            case R.id.logout:
                MyCache.putStringValueByName(this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER, "");
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}
