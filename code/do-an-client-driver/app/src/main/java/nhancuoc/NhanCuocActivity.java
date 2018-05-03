package nhancuoc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.doan.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;

import app.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import core.MainActivity;
import map.CustemMaps;
import myutil.MyLog;
import newbook.NewBookActivity;
import obj.Book;
import service.MyService;

public class NhanCuocActivity extends AppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.tv_name_customer)
    TextView tvNameCustomer;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.tv_from)
    TextView tvFrom;
    @BindView(R.id.ic_from)
    ImageView icFrom;
    @BindView(R.id.tv_to)
    TextView tvTo;
    @BindView(R.id.ic_to)
    ImageView icTo;
    @BindView(R.id.fab_matdo)
    FloatingActionButton fabMatdo;
    @BindView(R.id.ic_call)
    ImageButton icCall;
    @BindView(R.id.ic_message)
    ImageButton icMessage;
    @BindView(R.id.ic_huy_cuoc)
    ImageButton icHuyCuoc;
    private Book mBook;
    private GoogleMap mMap;
    private CustemMaps mCustemMaps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sau_khi_nhan_quoc);
        ButterKnife.bind(this);
        mBook = (Book) Book.fromJSON(getIntent().getStringExtra(Config.NEW_BOOK));
        ;
        initView();
        connectMyService();
        initMap();
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }


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
            NhanCuocActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });

        }
    };

    private void initView() {
        tvNameCustomer.setText(mBook.getCustomer().getName());
        tvCost.setText(getString(R.string.cost).replace("%d", Config.formatNumber(mBook.getCost()) + ""));
        tvFrom.setText(mBook.getNameFrom() + "");
        tvTo.setText(mBook.getNameTo() + "");
    }

    @OnClick(R.id.ic_from)
    public void onIcFromClicked() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + mCustemMaps.getMyLocation().getLatitude() + "," + mCustemMaps.getMyLocation().getLongitude() + "&daddr=" + mBook.getLatFrom() + "," + mBook.getLngFrom()));
        startActivity(intent);
    }

    @OnClick(R.id.ic_to)
    public void onIcToClicked() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + mCustemMaps.getMyLocation().getLatitude() + "," + mCustemMaps.getMyLocation().getLongitude() + "&daddr=" + mBook.getLatTo() + "," + mBook.getLngTo()));
        startActivity(intent);
    }

    @OnClick(R.id.fab_matdo)
    public void onFabMatdoClicked() {
        mCustemMaps.moveToMyLocation();

    }

    @OnClick(R.id.ic_call)
    public void onIcCallClicked() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mBook.getPhoneCutomer()));
        startActivity(intent);
    }

    public void sendSMS() {
        String number = "12346556";  // The number on which you want to send SMS
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
    }

    @OnClick(R.id.ic_message)
    public void onIcMessageClicked() {
        sendSMS();
    }

    @OnClick(R.id.ic_huy_cuoc)
    public void onIcHuyCuocClicked() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mCustemMaps = new CustemMaps(mMap, this);
        mCustemMaps.moveToMyLocation();
        Marker from = mCustemMaps.drawMarker(mBook.getLatFrom(), mBook.getLngFrom(), BitmapDescriptorFactory.fromResource(R.drawable.ic_circle), "Điểm đón", mCustemMaps.getNameByLocation(mBook.getLatFrom(), mBook.getLngFrom()));
        from.setRotation(0);
        from.setFlat(false);
        Marker to = mCustemMaps.drawMarker(mBook.getLatTo(), mBook.getLngTo(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED), "Điểm đến", mCustemMaps.getNameByLocation(mBook.getLatTo(), mBook.getLngTo()));
        to.setRotation(0);
        from.setFlat(false);
    }
}
