package book;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doan.R;
import com.willy.ratingbar.ScaleRatingBar;

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
            mMyService.getSocket().on(Config.NEW_OK_BOOK_CUSTOMER, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    BookActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MyLog.e(getClass(), "đã về");
                                MyLog.e(getClass(), args[0].toString());
                                Intent data = new Intent();
                                data.putExtra(Config.BOOK_CHAP_NHAN, args[0].toString());
                                setResult(RESULT_OK, data);
                                finish();
                            } catch (Exception e) {
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        }
                    });
                }
            });
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
//                        try {
//                            Driver driver = Driver.fromJSON(args[0].toString());
//                            if (driver instanceof Driver) {
//                                setResult(RESULT_OK);
//                                finish();
//                            } else {
                                MyLog.e(getClass(), "bookFindDriver  RESULT_CANCELED");
                                setResult(RESULT_CANCELED);
                                finish();
//                            }
//                        } catch (Exception e) {
//                            MyLog.e(getClass(), "bookFindDriver  RESULT_CANCELED_Exception");
//                            setResult(RESULT_CANCELED);
//                            finish();
//                        }

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
        mMyService.getSocket().off(Config.NEW_OK_BOOK_CUSTOMER);
//        mMyService.huyCuocXe();
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
