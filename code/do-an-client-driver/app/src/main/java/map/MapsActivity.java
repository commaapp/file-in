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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.emitter.Emitter;
import myutil.MyLog;
import obj.Customer;
import obj.Driver;
import profile.ProfileActivity;
import service.MyService;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.view_profile)
    ImageView viewProfile;
    @BindView(R.id.sw_ready)
    Switch swReady;
    private GoogleMap mMap;
    private ArrayList<Customer> mCustomers;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        swReady.setChecked(MyService.IS_RUNGNING);
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
        else mMyService.stopSelf();
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


        mMyService.getCustomerOnline(new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyLog.e(getClass(), args[0].toString());
                        mCustomers = new Gson().fromJson(args[0].toString(), new TypeToken<List<Customer>>() {
                        }.getType());
                        mLatLngMatDos.clear();
                        for (Customer customer : mCustomers)
                            mLatLngMatDos.add(new LatLng(customer.getLat(), customer.getLng()));
                        setMatDo();
                    }
                });
            }
        });


    }

    private ArrayList<LatLng> readItems() throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        String json = "[" +
                "{\"lat\" : 21.0510894, \"lng\" :105.8004779 } ," +
                "{\"lat\" : 21.0510935, \"lng\" : 105.9404933 } ," +
                "{\"lat\" : 21.0510907, \"lng\" : 105.6404626 } ," +
                "{\"lat\" :21.0510928, \"lng\" : 105.7604831} ," +
                "{\"lat\" :21.0710928, \"lng\" : 105.5804831} ," +
                "{\"lat\" :21.0410928, \"lng\" : 105.5404831} ," +
                "{\"lat\" :21.0310928, \"lng\" : 105.5004831} ," +
                "{\"lat\" :21.0810928, \"lng\" : 105.5104831} ," +
                "{\"lat\" :21.0310928, \"lng\" : 105.5404831} ," +
                "{\"lat\" :21.0110928, \"lng\" : 105.5404831} ," +
                "{\"lat\" : 21.0510928, \"lng\" : 105.640494 }" +
                "]";
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }

    private void setMatDo() {
        try {
            mProvider = new HeatmapTileProvider.Builder()
                    .data(mLatLngMatDos)
                    .build();

            if (mOverlay != null) mOverlay.clearTileCache();
            mOverlay = mCustemMaps.getGoogleMap().addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

        } catch (Exception e) {

        }


    }

    ArrayList<LatLng> mLatLngMatDos = new ArrayList<>();

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
