package main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;

import app.Config;
import book.BookActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import done.DoneActivity;
import io.socket.emitter.Emitter;
import map.CustemMaps;
import myutil.MyBitmap;
import myutil.MyCache;
import myutil.MyLog;
import obj.Book;
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
    @BindView(R.id.layout_infor_driver_book)
    LinearLayout layoutInforDriverBook;
    @BindView(R.id.tv_bien_so_xe)
    TextView tvBienSoXe;
    @BindView(R.id.tv_name_driver)
    TextView tvNameDriver;
    @BindView(R.id.ratingBar)
    ScaleRatingBar ratingBar;
    @BindView(R.id.im_btn_call)
    ImageButton imBtnCall;
    @BindView(R.id.im_btn_sms)
    ImageButton imBtnSms;
    @BindView(R.id.tv_time_come)
    TextView tvTimeCome;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    private GoogleMap mMap;
    private Marker mMarkerFrom;
    private Marker mMarkerTo;
    private ArrayList<Driver> mDrivers;
    private ArrayList<Marker> mDriverMarker = new ArrayList<>();
    private int cost;
    private double distance;
    private Book mBook;

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
            mMyService.getSocket().on(Config.HUY_CUOC, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    MapsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Book book = Book.fromJSON(args[0].toString());
                                if (book.getPhoneCutomer().equals(MyCache.getStringValueByName(MapsActivity.this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER))) {
                                    Toast.makeText(MapsActivity.this, "Cuốc xe đã bị hủy", Toast.LENGTH_SHORT).show();
                                    showLayoutBook();
                                }
                            } catch (Exception e) {
                            }
                        }
                    });
                }
            });
            mMyService.getSocket().on(Config.hoanThanh, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    MapsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Book book = Book.fromJSON(args[0].toString());
                                if (book.getPhoneCutomer().equals(MyCache.getStringValueByName(MapsActivity.this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER))) {
                                    showLayoutBook();
                                    startDone(book);
                                }
                            } catch (Exception e) {
                            }
                        }
                    });
                }
            });
        }
    };

    private void startDone(Book book) {
        Intent intent = new Intent(this, DoneActivity.class);
        intent.putExtra(Config.DONE_INTENT, book);
        startActivity(intent);
    }


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
        showLayoutBook();
    }

    private void showLayoutInforTaiXe() {
        layoutInforDriverBook.setVisibility(View.VISIBLE);
        layoutBook.setVisibility(View.GONE);
    }

    private void showLayoutBook() {
        layoutInforDriverBook.setVisibility(View.GONE);
        layoutBook.setVisibility(View.VISIBLE);
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
        mPlaceAutocompleteFragmentTo.setHint("Bạn muốn đến đâu?");
        ImageView searchIconFrom = (ImageView) ((LinearLayout) mPlaceAutocompleteFragmentFrom.getView()).getChildAt(0);
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
            distance = mCustemMaps.calculationByDistance(mLatLngFrom, mLatLngTo);
            cost = (int) (distance * 6000);
            tvCost.setText(getString(R.string.cost).replace("%d", "" + Config.formatNumber(cost)));
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

    public void showDialogInforTaiXe(Book mBook) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_infor_taixe, null);
        ViewHolder viewHolder = new ViewHolder(dialogView);
//        Glide.with(this).load(MyBitmap.Base64ToByte(mBook.getDriver().getImChanDung())).into(viewHolder.profileImage);
        viewHolder.tvNameDriver.setText(mBook.getDriver().getName());
        viewHolder.tvInforXe.setText(mBook.getDriver().getInforXe());
        viewHolder.tvInforXe.setText(mBook.getDriver().getInforXe());
        viewHolder.ratingBarDriver.setRating((float) mBook.getDriver().getRateStar());
        dialogBuilder.setView(dialogView);
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            MyLog.e(getClass(), data.getStringExtra(Config.BOOK_CHAP_NHAN));
//            Snackbar.make(layoutBook, "Tìm thấy một tài xế", Snackbar.LENGTH_SHORT).show();
            showLayoutInforTaiXe();
            mBook = Book.fromJSON(data.getStringExtra(Config.BOOK_CHAP_NHAN));
            tvBienSoXe.setText(mBook.getDriver().getInforXe());
            tvTimeCome.setText(getString(R.string.for_minute).replace("%d", (int) (mBook.getDistance() * 30 / 60) + ""));
            tvNameDriver.setText(mBook.getDriver().getName());
            ratingBar.setRating((float) mBook.getDriver().getRateStar());
//            Glide.with(this).load(MyBitmap.Base64ToByte(mBook.getDriver().getImChanDung())).into(profileImage);
            showDialogInforTaiXe(mBook);
        }
        if (resultCode == RESULT_CANCELED
//                && requestCode == Config.RESULT_CODE_TAI_XE
                ) {
            Snackbar.make(layoutBook, "Không tìm thấy tài xế nào. Vui lòng thử lại sau ít phút!", Snackbar.LENGTH_SHORT).show();
            MyLog.e(getClass(), "RESULT_CANCELED");
        }
    }

    @OnClick(R.id.tv_book)
    public void onBookClicked() {
        if (mLatLngFrom != null && mLatLngTo != null) {
            Intent intent = new Intent(this, BookActivity.class);
            intent.putExtra(Config.BOOK_KEY, new Book(
                    MyCache.getStringValueByName(MapsActivity.this, Config.MY_CACHE, Config.ACCOUNT_PHONE_NUMBER),
                    mLatLngFrom.latitude,
                    mLatLngTo.latitude,
                    mLatLngFrom.longitude,
                    mLatLngTo.longitude,
                    mCustemMaps.getNameByLocation(mLatLngFrom.latitude, mLatLngFrom.longitude),
                    mCustemMaps.getNameByLocation(mLatLngTo.latitude, mLatLngTo.longitude),
                    cost,
                    distance
            ));
            startActivityForResult(intent, Config.RESULT_CODE_TAI_XE);
        } else
            Snackbar.make(layoutBook, "Bạn cần lựa chọn điểm đi và điểm đến trước khi đặt xe!", Snackbar.LENGTH_SHORT).show();
    }

    @OnClick({R.id.im_btn_call, R.id.im_btn_sms})
    public void onViewClicked1(View view) {
        switch (view.getId()) {
            case R.id.im_btn_call:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + mBook.getPhoneDriver()));
                startActivity(intent);
                break;
            case R.id.im_btn_sms:
                sendSMS();
                break;
        }
    }

    public void sendSMS() {
        String number = mBook.getDriver().getPhoneNumber();  // The number on which you want to send SMS
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
    }

    @OnClick(R.id.ic_huy_cuoc)
    public void onViewClicked2() {
        mMyService.huyCuocXe();
    }


    static class ViewHolder {
        @BindView(R.id.profile_image)
        CircleImageView profileImage;
        @BindView(R.id.tv_name_driver)
        TextView tvNameDriver;
        @BindView(R.id.ratingBarDriver)
        ScaleRatingBar ratingBarDriver;
        @BindView(R.id.tv_infor_xe)
        TextView tvInforXe;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
