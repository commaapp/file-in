package profile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doan.R;
import com.willy.ratingbar.ScaleRatingBar;

import app.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import core.MainActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;
import myutil.MyBitmap;
import myutil.MyCache;
import myutil.MyLog;
import obj.Driver;
import service.MyService;
import wellcome.WellcomeActivity;

public class ProfileActivity extends AppCompatActivity {


    @BindView(R.id.layout_profile)
    LinearLayout layoutProfile;
    @BindView(R.id.pgbar_load)
    ProgressBar pgbarLoad;
    @BindView(R.id.im_back)
    ImageView imBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ratingBar)
    ScaleRatingBar ratingBar;
    @BindView(R.id.tv_infor_xe)
    TextView tvInforXe;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.tv_number_rate_rate)
    TextView tvNumberRateRate;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_infor);
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

            mMyService.getDriverProfile(MyCache.getStringValueByName(ProfileActivity.this, Config.MY_CACHE, Config.DRIPER_PHONE_NUMBER),
                    new Emitter.Listener() {
                        @Override
                        public void call(final Object... args) {
                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showLayoutProfile();
                                    MyLog.e(getClass(), args[0].toString());
                                    Driver driver = Driver.fromJSON(args[0].toString());
                                    tvName.setText(driver.getName());
                                    tvInforXe.setText(driver.getInforXe());
//                                    tvPhoneNumber.setText(customer.getSdt());
                                    tvNumberRateRate.setText(driver.getRateNumber1() + "");
                                    ratingBar.setRating((float) driver.getRateStar());
//                                    edtName.setText(customer.getName());
                                    Glide.with(ProfileActivity.this).load(MyBitmap.Base64ToByte(driver.getImChanDung())).into(profileImage);
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

    @OnClick(R.id.tv_logout)
    public void onViewClicked() {
        MyCache.putStringValueByName(this, Config.MY_CACHE, Config.DRIPER_PHONE_NUMBER, "");
        Intent intent = new Intent(this, WellcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.im_back)
    public void onViewClicked2() {
        finish();
    }

//    @OnClick({R.id.im_back, R.id.tv_save, R.id.logout})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.im_back:
//                finish();
//                break;
//            case R.id.tv_save:
//                Customer customer = new Customer(edtName.getText().toString(), MyCache.getStringValueByName(ProfileActivity.this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER));
//                mMyService.updateCustomProfile(customer, new Emitter.Listener() {
//                    @Override
//                    public void call(final Object... args) {
//                        ProfileActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Snackbar.make(layoutProfile, "Đã cập nhật thông tin của bạn", Snackbar.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//                break;
//            case R.id.logout:
//                MyCache.putStringValueByName(this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER, "");
//                Intent intent = new Intent(this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                break;
//        }
//    }
}
