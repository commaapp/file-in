package book;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.doan.R;

import app.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.emitter.Emitter;
import myutil.MyLog;
import obj.Book;
import obj.Driver;
import service.MyService;

public class BookActivity extends AppCompatActivity {
    @BindView(R.id.tvNameFrom)
    TextView tvNameFrom;
    @BindView(R.id.tvNameTo)
    TextView tvNameTo;
    @BindView(R.id.tvCost)
    TextView tvCost;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    private Intent mIntent;
    private Book mBook;
    private Object data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_driver);
        ButterKnife.bind(this);
        getData();
        initView();
        connectMyService();
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
            BookActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findDriver();
                }
            });

        }
    };

    @Override
    public void onBackPressed() {
    }

    private void findDriver() {
        mMyService.bookFindDriver(mBook, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                BookActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Driver driver = Driver.fromJSON(args[0].toString());
                            if (driver instanceof Driver) {
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        } catch (Exception e) {

                        }

                    }
                });
            }
        });
    }

    private void connectMyService() {
        Intent intentMyService = new Intent(this, MyService.class);
        bindService(intentMyService, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private void initView() {
        tvNameFrom.setText(mBook.getNameFrom());
        tvNameTo.setText(mBook.getNameTo());
        tvCost.setText(getString(R.string.cost).replace("%d", mBook.getCost() + ""));
        tvCost.setText(getString(R.string.cost).replace("%d", Config.formatNumber(mBook.getCost()) + ""));
    }

    public void getData() {
        mIntent = getIntent();
        mBook = (Book) mIntent.getSerializableExtra(Config.BOOK_KEY);
        MyLog.e(getClass(), mBook.toJSON());
    }

    @OnClick(R.id.tvCancel)
    public void onViewClicked() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
