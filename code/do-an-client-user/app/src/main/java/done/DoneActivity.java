package done;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doan.R;
import com.willy.ratingbar.ScaleRatingBar;

import app.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import login.LoginActivity;
import obj.Book;
import obj.Driver;
import service.MyService;

public class DoneActivity extends AppCompatActivity {
    @BindView(R.id.vui)
    LinearLayout vui;
    @BindView(R.id.buon)
    LinearLayout buon;
    @BindView(R.id.layout_emoji)
    LinearLayout layoutEmoji;
    @BindView(R.id.ratingBarDriver)
    ScaleRatingBar ratingBarDriver;
    @BindView(R.id.edt_nhan_xet)
    EditText edtNhanXet;
    @BindView(R.id.btn_gui)
    TextView btnGui;
    @BindView(R.id.layout_danh_gia)
    LinearLayout layoutDanhGia;
    private Book mBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        ButterKnife.bind(this);
        mBook = (Book) getIntent().getSerializableExtra(Config.DONE_INTENT);
        showEmoji();
        ratingBarDriver.setRating(5);
        connectMyService();

    }

    private void connectMyService() {
        Intent intentMyService = new Intent(this, MyService.class);
        bindService(intentMyService, serviceConnection, Context.BIND_AUTO_CREATE);
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
        }
    };

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
    private void showNhanXet() {
        layoutEmoji.setVisibility(View.GONE);
        layoutDanhGia.setVisibility(View.VISIBLE);
    }

    private void showEmoji() {
        layoutEmoji.setVisibility(View.VISIBLE);
        layoutDanhGia.setVisibility(View.GONE);
    }

    @OnClick(R.id.vui)
    public void onVuiClicked() {
        showNhanXet();
    }

    @OnClick(R.id.buon)
    public void onBuonClicked() {
        showNhanXet();
    }

    @OnClick(R.id.btn_gui)
    public void onBtnGuiClicked() {
        mMyService.updateStarDriver(new Driver(mBook.getPhoneDriver(),ratingBarDriver.getRating()));
        finish();

    }
}
