package main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doan.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import app.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.emitter.Emitter;
import map.CustemMaps;
import myutil.MyCache;
import myutil.MyLog;
import obj.Customer;
import obj.Driver;
import profile.ProfileActivity;
import service.MyService;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.view_profile)
    ImageView viewProfile;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.layout_book)
    LinearLayout layoutBook;
    @BindView(R.id.layout_item_book)
    LinearLayout layoutItemBook;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    private GoogleMap mMap;
    private Marker mMarkerFrom;
    private Marker mMarkerTo;
    private ArrayList<Driver> mDrivers;
    private ArrayList<Marker> mDriverMarker = new ArrayList<>();

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
            MyLog.e(getClass(), "onServiceConnected");
            MapsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initMap();
                }
            });

        }
    };

    @Override
    protected void onDestroy() {
        mMyService.updateStateOnlineProfile(new Customer(
                MyCache.getStringValueByName(this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER)
                , false));
        unbindService(serviceConnection);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        layoutItemBook.setVisibility(View.GONE);
        connectMyService();
    }

    LatLng mLatLngFrom;
    LatLng mLatLngTo;

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    CustemMaps mCustemMaps;
    PlaceAutocompleteFragment mPlaceAutocompleteFragmentTo;
    PlaceAutocompleteFragment mPlaceAutocompleteFragmentFrom;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mCustemMaps = new CustemMaps(mMap, MapsActivity.this);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                initPlaceAutocompleteFragmentFrom(location);
                mMyService.updateCustomLocation(new Customer(
                        MyCache.getStringValueByName(MapsActivity.this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER),
                        location.getLatitude(), location.getLongitude()));
            }
        });

        mPlaceAutocompleteFragmentFrom =
                (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.find_location_from);
        mPlaceAutocompleteFragmentFrom.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                initLatLngFrom(place.getLatLng());

            }

            @Override
            public void onError(Status status) {

            }
        });
        mPlaceAutocompleteFragmentFrom.setHint("Bạn đang ở đâu?");
        mPlaceAutocompleteFragmentTo =
                (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.find_location_to);


        mPlaceAutocompleteFragmentTo.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                initLatLngTo(place.getLatLng());

            }

            @Override
            public void onError(Status status) {

            }
        });
        mPlaceAutocompleteFragmentTo.setHint("Bạn muốn đi đến đâu?");
        ImageView searchIconFrom = (ImageView) ((LinearLayout) mPlaceAutocompleteFragmentTo.getView()).getChildAt(0);
        ImageView searchIconTo = (ImageView) ((LinearLayout) mPlaceAutocompleteFragmentTo.getView()).getChildAt(0);
        searchIconFrom.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot));
        searchIconTo.setImageDrawable(getResources().getDrawable(R.drawable.ic_location));
    }

    private void initLatLngTo(LatLng latLng) {
        mLatLngTo = latLng;
        if (mMarkerTo != null) mMarkerTo.remove();
        mMarkerTo = mCustemMaps.drawMarker(mLatLngTo.latitude, mLatLngTo.longitude,
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED), "Điểm đến", mCustemMaps.getNameByLocation(latLng.latitude, latLng.longitude));
        mMarkerTo.setRotation(0);
        mMarkerTo.setFlat(false);
        mCustemMaps.mapMoveTo(mLatLngTo, 17);
        checkShowItemBook();
    }

    private void initLatLngFrom(LatLng latLng) {
        mLatLngFrom = latLng;
        if (mMarkerFrom != null)
            mMarkerFrom.remove();
        mMarkerFrom = mCustemMaps.drawMarker(mLatLngFrom.latitude, mLatLngFrom.longitude,
                BitmapDescriptorFactory.fromResource(R.drawable.ic_circle), "Điểm đi", mCustemMaps.getNameByLocation(latLng.latitude, latLng.longitude));
        checkShowItemBook();
        mMarkerFrom.setRotation(0);
        mMarkerFrom.setFlat(false);
        mCustemMaps.mapMoveTo(mLatLngFrom, 17);
        showDriverInLocation(mLatLngFrom);
    }

    private void initPlaceAutocompleteFragmentFrom(Location location) {
        if (mCustemMaps.getMyLocation() == null) {
            mCustemMaps.setMyLocation(location);
            showDriverInLocation(new LatLng(location.getLatitude(), location.getLongitude()));
            mCustemMaps.moveToMyLocation();
            mLatLngFrom = new LatLng(location.getLatitude(), location.getLongitude());
            mPlaceAutocompleteFragmentFrom.setText("Vị trí của bạn");
        }
        mCustemMaps.setMyLocation(location);
    }

    private void checkShowItemBook() {
        if (mLatLngFrom != null && mLatLngTo != null) {
            layoutItemBook.setVisibility(View.VISIBLE);
            double distance = mCustemMaps.calculationByDistance(mLatLngFrom, mLatLngTo);
            int cost = (int) (distance * 6000);
            tvCost.setText(getString(R.string.cost).replace("%d", "" + cost));
            MyLog.e(getClass(), mLatLngFrom.latitude + " " + mLatLngFrom.longitude);
            MyLog.e(getClass(), mLatLngTo.latitude + " " + mLatLngTo.longitude);

        } else layoutItemBook.setVisibility(View.GONE);
    }

    private void showDriverInLocation(LatLng mLatLngFrom) {
        mMyService.findDriperInLocation(new Customer(MyCache.getStringValueByName(MapsActivity.this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER)
                , mLatLngFrom.latitude, mLatLngFrom.longitude), new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyLog.e(getClass(), args[0].toString());
                        mDrivers = new Gson().fromJson(args[0].toString(), new TypeToken<List<Driver>>() {
                        }.getType());
                        if (!mDriverMarker.isEmpty())
                            for (Marker marker : mDriverMarker)
                                marker.remove();
                        for (Driver driver : mDrivers) {
                            Marker m = mCustemMaps.drawMarker(driver.getLat(), driver.getLog(),
                                    BitmapDescriptorFactory.fromResource(R.drawable.ic_bike), driver.getName(), driver.getName());
                            m.setRotation(driver.getDegree());
                            mDriverMarker.add(m);
                            MyLog.e(getClass(), driver.getName());
                        }

                    }
                });
            }
        });
    }


    @OnClick({R.id.view_profile, R.id.tv_category, R.id.tv_book, R.id.layout_book})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_category:
                break;
            case R.id.tv_book:
                break;
            case R.id.layout_book:
                break;
        }
    }

    @OnClick(R.id.fab_my_location)
    public void onViewClicked() {
        mCustemMaps.moveToMyLocation();
    }


    @OnClick(R.id.tv_book)
    public void onBookClicked() {
        if (mLatLngFrom != null && mLatLngTo != null) {

        } else
            Snackbar.make(layoutBook, "Bạn cần lựa chọn điểm đi và điểm đến trước khi đặt xe!", Snackbar.LENGTH_SHORT).show();
    }
}
