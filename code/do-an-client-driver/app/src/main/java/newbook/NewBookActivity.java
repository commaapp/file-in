package newbook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.doan.R;

import app.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import core.MainActivity;
import login.LoginActivity;
import myutil.MyLog;
import obj.Book;
import service.MyService;

public class NewBookActivity extends AppCompatActivity {
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.tv_km)
    TextView tvKm;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_from)
    TextView tvFrom;
    @BindView(R.id.tv_to)
    TextView tvTo;
    @BindView(R.id.tv_chap_nhan)
    TextView tvChapNhan;
    @BindView(R.id.tv_huy)
    TextView tvHuy;
    private Book mBook;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
        ButterKnife.bind(this);
        mBook = (Book) getIntent().getSerializableExtra(Config.NEW_BOOK);
        initView();
        connectMyService();
    }

    @Override
    protected void onDestroy() {
        mMyService.tuChoiCuoc();
        unbindService(serviceConnection);
        super.onDestroy();
    }

    String verifyCode = "null";

    private void connectMyService() {
        Intent intentMyService = new Intent(this, MyService.class);
        bindService(intentMyService, serviceConnection, Context.BIND_AUTO_CREATE);
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
            NewBookActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvHuy.setEnabled(true);
                    tvChapNhan.setEnabled(true);
                }
            });

        }
    };


    private void initView() {
        tvHuy.setEnabled(false);
        tvChapNhan.setEnabled(false);
        mCountDownTimer = new CountDownTimer(45000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTime.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
               finish();
            }

        }.start();
        tvCost.setText(getString(R.string.cost).replace("%d", Config.formatNumber(mBook.getCost()) + ""));
        tvKm.setText(getString(R.string.km_book).replace("%f", mBook.getKm() + ""));
        tvFrom.setText(mBook.getNameFrom() + "");
        tvTo.setText(mBook.getNameTo() + "");
    }

    @OnClick(R.id.tv_chap_nhan)
    public void onTvChapNhanClicked() {
        mCountDownTimer.cancel();
    }

    @OnClick(R.id.tv_huy)
    public void onTvHuyClicked() {
       finish();

    }

}
