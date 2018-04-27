package map;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.doan.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import myutil.MyLog;
import profile.ProfileActivity;
import service.MyService;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.view_profile)
    ImageView viewProfile;
    @BindView(R.id.sw_ready)
    Switch swReady;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);


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
            MyLog.e(getClass(), "onServiceConnected");
            MapsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initMap();
//                    mMyService.updateDriverLocation();

                }
            });

        }
    };


    private void connectMyService() {
        Intent intentMyService = new Intent(this, MyService.class);
        bindService(intentMyService, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    CustemMaps mCustemMaps;

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        if (swReady.isChecked()) mMyService.runForeground();
//        else mMyService.stopSelf();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mCustemMaps = new CustemMaps(mMap, this);

        try {
            mMyService.setMyCustemMaps(mCustemMaps);
        } catch (JSONException e) {
        }
        mCustemMaps.moveToMyLocation();

        swReady.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mMyService.updateDriverState(b);
                if (b) {
                    Snackbar.make(swReady, "Sẵn sàng nhận cuốc xe", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(swReady, "Không sẵn sàng nhận cuốc xe", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    @OnClick({R.id.view_profile, R.id.fab_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_location:
                mCustemMaps.moveToMyLocation();
                break;
        }
    }


}
