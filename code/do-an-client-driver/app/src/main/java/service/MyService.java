package service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.doan.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import app.Config;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import map.CustemMaps;
import myutil.MyCache;
import myutil.MyLog;
import obj.Driver;
import wellcome.WellcomeActivity;

/**
 * Created by D on 4/13/2018.
 */

public class MyService extends Service implements SensorEventListener {
    public static boolean IS_RUNGNING = false;
    private Socket mSocket;
    private CustemMaps mCustemMaps;
    private LocationManager mLocationManager;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    {
        try {
            mSocket = IO.socket(Config.SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Emitter.Listener CLIENT_CUSTEMER_CONNECT = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MyLog.e(getClass(), args[0].toString());
        }
    };

    public Socket getSocket() {
        return mSocket;
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MyLog.e(getClass(), "onConnect ");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MyLog.e(getClass(), "onDisconnect " + args[0].toString());
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MyLog.e(getClass(), "onConnectError ");
        }
    };
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            MyLog.e(getClass(), "onLocationChanged");
            mSocket.emit(Config.Driver_Update_Location,
                    new Driver(
                            MyCache.getStringValueByName(MyService.this, Config.MY_CACHE, Config.DRIPER_PHONE_NUMBER),
                            location.getLatitude(),
                            location.getLongitude(), myDegree).toJSON());
            MyLog.e(getClass(), "Heading: " + Float.toString(myDegree) + " degrees");

//            if (marker == null) {
//                marker = mCustemMaps.drawMarker(location.getLatitude(),
//                        location.getLongitude(), BitmapDescriptorFactory.fromResource(R.drawable.ic_bike), "s", "s");
//                marker.setAnchor(0.5f, 0.5f);
//            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.setShow(true);
        MyLog.e(getClass(), "onStartCommand ");
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Config.CLIENT_CUSTEMER_CONNECT, CLIENT_CUSTEMER_CONNECT);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        mSocket.connect();
        getCurrentLocation();
        return START_NOT_STICKY;
    }

    /*
    phải có cả NETWORK_PROVIDER và GPS_PROVIDER mới chạy dc
     */
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location location = null;
        if (isNetworkEnabled) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1, 1, mLocationListener);
            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (isGPSEnabled) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1, 1, mLocationListener);
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    public void runForeground() {
        IS_RUNGNING = true;
        Intent nextIntent = new Intent(this, WellcomeActivity.class);
        nextIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pnextIntent = PendingIntent.getActivity(this, 0, nextIntent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Driver ")
                .setContentText("Đang hoạt động").setContentIntent(pnextIntent)
                .setSmallIcon(R.mipmap.ic_launcher).setOngoing(true)
                .build();
        startForeground(1011, notification);
    }

    private IBinder binder;

    @Override
    public void onCreate() {
        Log.e("ServiceDemo", "Đã gọi onCreate()");
        binder = new MyBinder();


        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("ServiceDemo", "Đã gọi onBind()");
        return binder;
    }

    // Kết thúc một Service
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("ServiceDemo", "Đã gọi onUnbind()");
        return super.onUnbind(intent);
    }


    public void checkDriverLogin(String phoneNumber, Emitter.Listener listener) {
        mSocket.on(Config.Custemer_Check_Login_Res, listener);
        mSocket.emit(Config.Custemer_Check_Login, phoneNumber);
    }

    public void registerDriver(String json, Emitter.Listener listener) {
        mSocket.on(Config.Driver_Regitor_Res, listener);
        mSocket.emit(Config.Driver_Regitor, json);
    }

    public void driverVefifyPhonenumber(String phoneNumber, Emitter.Listener listener) {
        mSocket.on(Config.Driper_Vefify_Code_Res, listener);
        mSocket.emit(Config.Driper_Vefify_Phonenumber, phoneNumber);
    }

    public void checkDriverIsExists(String phoneNumber, Emitter.Listener listener) {
        mSocket.on(Config.checkDriverIsExists_Res, listener);
        mSocket.emit(Config.checkDriverIsExists, phoneNumber);
    }

    public void getDriverProfile(String stringValueByName, Emitter.Listener listener) {
        mSocket.on(Config.getDriverProfile_Res, listener);
        mSocket.emit(Config.getDriverProfile, stringValueByName);
    }

    Marker marker;

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

    public void setMyCustemMaps(CustemMaps mCustemMaps) throws JSONException {
        this.mCustemMaps = mCustemMaps;
        mProvider = new HeatmapTileProvider.Builder()
                .data(readItems())
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mCustemMaps.getGoogleMap().addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

    }

    private SensorManager mSensorManager;

    public void updateDriverState(boolean b) {
        String json = new Driver(MyCache.getStringValueByName(this, Config.MY_CACHE, Config.DRIPER_PHONE_NUMBER), b).toJSON();
        mSocket.emit(Config.Driver_Update_State, json);
        Log.e("ServiceDemo", "updateDriverState " + json);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        myDegree = Math.round(sensorEvent.values[0]);
        if (marker != null) marker.setRotation(myDegree);
//        Log.e("ServiceDemo", "onSensorChanged");
    }

    float myDegree;

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class MyBinder extends Binder {
        public MyService getMyService() {
            return MyService.this;
        }
    }

    @Override
    public void onDestroy() {
        Log.e("ServiceDemo", "Đã gọi onDestroy()");
        super.onDestroy();
    }
}
