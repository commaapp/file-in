package map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

import com.doan.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Duong on 11/1/2016.
 */

public class CustemMaps implements GoogleMap.OnMyLocationChangeListener {
    protected GoogleMap googleMap;
    protected Context context;
    protected Geocoder geocoder;
    protected Activity activity;
    protected Location myLocation;
    protected String en = "en";
    protected String vi = "vi";
    protected Polyline polylineChiDuong;
    protected ArrayList<Marker> arrMarkerFlags;

    //    private String link="https://maps.googleapis.com/maps/api/directions/json?origin="+diemDau+"&destination="+diemCuoi+"&avoid=tolls|highways|ferries&mode="+driving+"&language="+vi;
    public CustemMaps(GoogleMap googleMap, Context context) {
        this.googleMap = googleMap;
        this.context = context;
        checkLocationIsEnable();
        activity = (Activity) context;
        geocoder = new Geocoder(activity, Locale.getDefault());
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false); // không sử dụng buttom my location mặc định của máy
        uiSettings.setMapToolbarEnabled(false); // không sử dụng toolbar hỗ trợ của gg map để bật ứng dụng bản đò
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(this);
        googleMap.getUiSettings().setCompassEnabled(true);


    }

    public CustemMaps(Context context) {
        this.context = context;
        checkLocationIsEnable();
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public String getNameByLocation(double lat, double lng) {
        //tìm kiếm vị trí
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);// getfromlocation trả vể list nên cần tạo 1 list
            if (addresses.size() == 0) {
                return "";
            }
            String name = addresses.get(0).getAddressLine(0);
            if (addresses.get(0).getAddressLine(1) instanceof String) {
                name += " - " + addresses.get(0).getAddressLine(1);
            }
            if (addresses.get(0).getAddressLine(2) instanceof String) {
                name += " - " + addresses.get(0).getAddressLine(2);
            }
            return name;
        } catch (IOException e) {
            return "";
        }
    }

    public void checkLocationIsEnable() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user

//            final AlertDialog.Builder dialog = new AlertDialog.Builder(context,R.style.AppTheme);
//            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
//            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
            //get gps
//                }
//            });
//            dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//
//                    activity.finish();
//                }
//            });
//            dialog.show();
        }
    }

    public Marker drawMarker(double lat, double lng, BitmapDescriptor hue, String title, String snippet) {
        //định nghĩa điểm ảnh
        // mỗi maker chỉ hiện thị một điểm ảnh
        LatLng latLng = new LatLng(lat, lng);//tạo kinh vĩ
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(hue);
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setFlat(true);
        marker.setRotation(45);
        marker.setAnchor(0.5f, 0.5f);
        return marker;
    }

    public Activity getActivity() {
        return activity;

    }

    public GoogleMap getGoogleMap() {

        return googleMap;
    }

    public void setMapVeTinh() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public void setMapGiaoThong() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onMyLocationChange(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (myLocation == null) {
            myLocation = location;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
            googleMap.animateCamera(cameraUpdate);
        } else {
            myLocation = location;
        }
    }

    public void moveToMyLocation() {
        checkLocationIsEnable();
        if (myLocation != null) {
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mapMoveTo(latLng, 13);

        }
    }

    public void mapMoveTo(LatLng latLng, int i) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, i);
        googleMap.animateCamera(cameraUpdate);
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    public void drawRoadByAddress(String addressStart, String addressEnd, int color, int width) {

    }

    public void parserGoogleMapAPI(Location locationStart, Location locationEnd, String mode, Handler handler) {
        AsynGetLatLng asynGetLatLng = new AsynGetLatLng(handler, mode);
        asynGetLatLng.execute(locationStart, locationEnd);
    }


    public void drawRoad(ArrayList<ItemStep> itemSteps, int color, int width) {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        if (arrMarkerFlags != null) {
            for (Marker marker : arrMarkerFlags) {
                marker.remove();
            }
        }
        arrMarkerFlags = new ArrayList<>();
        for (ItemStep itemStep : itemSteps) {
            latLngs.add(new LatLng(Double.parseDouble(itemStep.getStart_locationLatSteps()),
                    Double.parseDouble(itemStep.getStart_locationLngSteps())));
            Marker marker =
                    drawMarker(Double.parseDouble(itemStep.getStart_locationLatSteps()),
                            Double.parseDouble(itemStep.getStart_locationLngSteps()),
                            BitmapDescriptorFactory.fromResource(R.drawable.ic_person),
                            itemStep.getHtml_instructions(),
                            itemStep.getTravel_mode());
            marker.setTag(itemStep);
            arrMarkerFlags.add(marker);
        }
        if (polylineChiDuong != null) {
            polylineChiDuong.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(color);
        polylineOptions.width(width);
        polylineOptions.addAll(latLngs);
        polylineChiDuong = googleMap.addPolyline(polylineOptions);
    }

    public void searchLocationByName(String start, String end) {
        Location locationStart = null, locationEnd = null;
        try {
            List<Address> addressesStart = geocoder.getFromLocationName(start, 1);
            List<Address> addressesEnd = geocoder.getFromLocationName(end, 1);
            if (addressesStart.size() > 0) {
                Address addStart = addressesStart.get(0);
                locationStart = new Location("");
                locationStart.setLatitude(addStart.getLatitude());
                locationStart.setLongitude(addStart.getLongitude());
            }
            if (addressesEnd.size() > 0) {
                Address addEnd = addressesEnd.get(0);
                locationEnd = new Location("");
                locationEnd.setLatitude(addEnd.getLatitude());
                locationEnd.setLongitude(addEnd.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (locationStart == null || locationEnd == null) {
            Toast.makeText(context, "Vị trí không thoả mã", Toast.LENGTH_LONG).show();
            return;
        } else {
            Toast.makeText(context, "Vị trí  thoả mã", Toast.LENGTH_LONG).show();
        }
    }

    public class GoogleMapAPI {
        @Override
        public String toString() {
            return "GoogleMapAPI{" +
                    "start_address='" + start_address + '\'' +
                    '}';
        }

        String status;
        String place_id = "place_id:";
        String copyrights;
        String summary;
        String distanceText;
        String distanceValue;
        String durationText;
        String durationValue;
        String end_address;
        String start_address;
        String start_locationLat;
        String start_locationLng;
        String end_locationLat;
        String end_locationLng;
        ArrayList<ItemStep> itemSteps;

        public void setItemSteps(ArrayList<ItemStep> itemSteps) {
            this.itemSteps = itemSteps;
        }

        public ArrayList<ItemStep> getItemSteps() {
            return itemSteps;
        }

        public GoogleMapAPI(String status, String copyrights, String summary, String distanceText, String distanceValue, String durationText, String durationValue, String end_address, String start_address, String start_locationLat, String start_locationLng, String end_locationLat, String end_locationLng, ArrayList<ItemStep> itemSteps) {

            this.status = status;
            this.copyrights = copyrights;
            this.summary = summary;
            this.distanceText = distanceText;
            this.distanceValue = distanceValue;
            this.durationText = durationText;
            this.durationValue = durationValue;
            this.end_address = end_address;
            this.start_address = start_address;
            this.start_locationLat = start_locationLat;
            this.start_locationLng = start_locationLng;
            this.end_locationLat = end_locationLat;
            this.end_locationLng = end_locationLng;
            this.itemSteps = itemSteps;
        }

        public String getStatus() {
            return status;
        }

        public String getCopyrights() {
            return copyrights;
        }

        public String getSummary() {
            return summary;
        }

        public String getDistanceText() {
            return distanceText;
        }

        public String getDistanceValue() {
            return distanceValue;
        }

        public String getDurationText() {
            return durationText;
        }

        public String getDurationValue() {
            return durationValue;
        }

        public String getEnd_address() {
            return end_address;
        }

        public String getStart_address() {
            return start_address;
        }

        public String getStart_locationLat() {
            return start_locationLat;
        }

        public String getStart_locationLng() {
            return start_locationLng;
        }

        public String getEnd_locationLat() {
            return end_locationLat;
        }

        public String getEnd_locationLng() {
            return end_locationLng;
        }
    }

    public class ItemStep {
        String distanceTextSteps;
        String distanceValueSteps;
        String durationTextSteps;
        String durationValueSteps;
        String start_locationLatSteps;
        String start_locationLngSteps;
        String end_locationLatSteps;
        String end_locationLngSteps;
        String html_instructions;
        String travel_mode;

        public Spanned getString() {
            return Html.fromHtml("<strong>"
                    + getHtml_instructions()
                    + "</strong><br>Đi: " +
                    "<em>" +
                    getDistanceTextSteps()
                    + "</em><br> Thời gian đi: <em>" +
                    getDurationTextSteps() + "</em></font> " +
                    " <i>" +
                    getTravel_mode()
                    + "</i>");
        }

        public String getDistanceTextSteps() {
            return distanceTextSteps;
        }

        public String getDistanceValueSteps() {
            return distanceValueSteps;
        }

        public String getDurationTextSteps() {
            return durationTextSteps;
        }

        public String getDurationValueSteps() {
            return durationValueSteps;
        }

        public String getStart_locationLatSteps() {
            return start_locationLatSteps;
        }

        public String getStart_locationLngSteps() {
            return start_locationLngSteps;
        }

        public String getEnd_locationLatSteps() {
            return end_locationLatSteps;
        }

        public String getEnd_locationLngSteps() {
            return end_locationLngSteps;
        }

        public String getHtml_instructions() {
            return html_instructions;
        }

        public String getTravel_mode() {
            return travel_mode;
        }

        public ItemStep(String distanceTextSteps,
                        String distanceValueSteps,
                        String durationTextSteps,
                        String durationValueSteps,
                        String start_locationLatSteps,
                        String start_locationLngSteps,
                        String end_locationLatSteps,
                        String end_locationLngSteps,
                        String html_instructions,
                        String travel_mode) {
            this.distanceTextSteps = distanceTextSteps;
            this.distanceValueSteps = distanceValueSteps;
            this.durationTextSteps = durationTextSteps;
            this.durationValueSteps = durationValueSteps;
            this.start_locationLatSteps = start_locationLatSteps;
            this.start_locationLngSteps = start_locationLngSteps;
            this.end_locationLatSteps = end_locationLatSteps;
            this.end_locationLngSteps = end_locationLngSteps;
            this.html_instructions = html_instructions;
            this.travel_mode = travel_mode;

        }
    }

    public class AsynGetLatLng extends AsyncTask<Location, Void, String> {
        private Handler handler;
        private String mode;

        public AsynGetLatLng(Handler handler, String mode) {
            this.handler = handler;
            this.mode = mode;
        }

        @Override
        protected void onPostExecute(String result) {
            GoogleMapAPI googleMapAPI = null;
            try {
                JSONObject json = new JSONObject(result);
                String status = null;
                status = json.getString("status");
                if (status.equals("OK")) {
                    JSONArray object = json.getJSONArray("routes");
                    JSONObject item = object.getJSONObject(0);
                    String copyrights = item.getString("copyrights");

                    String summary = item.getString("summary");
//                Log.e("faker", summary);
                    JSONArray legs = item.getJSONArray("legs");

                    JSONObject objectLegs = legs.getJSONObject(0);

                    JSONObject distance = objectLegs.getJSONObject("distance");
                    String distanceText = distance.getString("text");
                    String distanceValue = distance.getString("value");
//                Log.e("faker", distanceText + " " + distanceValue);

                    JSONObject duration = objectLegs.getJSONObject("duration");
                    String durationText = duration.getString("text");
                    String durationValue = duration.getString("value");
//                Log.e("faker", durationText + " " + durationValue);

                    String end_address = objectLegs.getString("end_address");
                    String start_address = objectLegs.getString("start_address");
//                Log.e("faker", end_address + " " + start_address);

                    JSONObject start_location = objectLegs.getJSONObject("start_location");
                    String start_locationLat = start_location.getString("lat");
                    String start_locationLng = start_location.getString("lng");
//                Log.e("faker", start_locationLat + " " + start_locationLng);

                    JSONObject end_location = objectLegs.getJSONObject("end_location");
                    String end_locationLat = end_location.getString("lat");
                    String end_locationLng = end_location.getString("lng");
//                Log.e("faker", end_locationLat + " " + end_locationLng);

                    JSONArray steps = objectLegs.getJSONArray("steps");

                    ArrayList<ItemStep> arrItemSteps = new ArrayList<>();
                    for (int i = 0; i < steps.length(); i++) {
                        JSONObject itemSteps = steps.getJSONObject(i);
                        JSONObject distanceSteps = itemSteps.getJSONObject("distance");
                        String distanceTextSteps = distanceSteps.getString("text");
                        String distanceValueSteps = distanceSteps.getString("value");
//                    Log.e("faker", distanceTextSteps + " " + distanceValueSteps);

                        JSONObject durationSteps = itemSteps.getJSONObject("duration");
                        String durationTextSteps = durationSteps.getString("text");
                        String durationValueSteps = durationSteps.getString("value");
//                    Log.e("faker", durationTextSteps + " " + durationValueSteps);

                        JSONObject start_locationSteps = itemSteps.getJSONObject("start_location");
                        String start_locationLatSteps = start_locationSteps.getString("lat");
                        String start_locationLngSteps = start_locationSteps.getString("lng");
//                    Log.e("faker", start_locationLatSteps + " " + start_locationLngSteps);

                        JSONObject end_locationSteps = itemSteps.getJSONObject("end_location");
                        String end_locationLatSteps = end_locationSteps.getString("lat");
                        String end_locationLngSteps = end_locationSteps.getString("lng");
//                    Log.e("faker", end_locationLatSteps + " " + end_locationLngSteps);

                        String html_instructions = Html.fromHtml((String) itemSteps.getString("html_instructions")).toString();
                        html_instructions = html_instructions.replace("\n", " ");
//                    Log.e("faker", html_instructions);

                        String travel_mode = itemSteps.getString("travel_mode");
//                    Log.e("faker", travel_mode);
                        arrItemSteps.add(new ItemStep(distanceTextSteps, distanceValueSteps, durationTextSteps, durationValueSteps,
                                start_locationLatSteps, start_locationLngSteps, end_locationLatSteps, end_locationLngSteps, html_instructions, travel_mode));
                    }
                    googleMapAPI = new GoogleMapAPI(status, copyrights,
                            summary, distanceText, distanceValue, durationText, durationValue,
                            end_address, start_address, start_locationLat, start_locationLng, end_locationLat, end_locationLng, arrItemSteps);
                    Message message = new Message();
                    message.obj = googleMapAPI;
                    handler.sendMessage(message);
                } else {
                    handler.sendEmptyMessage(0);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            PolylineOptions options = new PolylineOptions();
//            options.color(Color.RED);;
//            options.width(10);
//            options.addAll(latLngs);
//            if (poLyLine!=null){
//                poLyLine.remove();
//            }
//            poLyLine = mGoogleMap.addPolyline(options);
        }

        @Override
        protected String doInBackground(Location... params) {

            String diemDau = "" + params[0].getLatitude() + "," + params[0].getLongitude();
            diemDau = diemDau.replace(" ", "+");
            String diemCuoi = "" + params[1].getLatitude() + "," + params[1].getLongitude();
            diemCuoi = diemCuoi.replace(" ", "+");
            String vi = "vi";
            String link = "https://maps.googleapis.com/maps/api/directions/json?origin="
                    + diemDau + "&destination="
                    + diemCuoi + "&avoid=tolls|highways|ferries&mode="
                    + mode + "&language="
                    + vi;
            String result = "";
            try {
                // Create a URL for the desired page
//                Log.e("faker","Rẽ \u003cb\u003etrái\u003c/b\u003e tại Công Ty Tnhh Nano An Phát vào \u003cb\u003eNgô Quyền\u003c/b\u003e");
                URL url = new URL(link);
                Log.e("faker", link);
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str = null;
                while ((str = in.readLine()) != null) {
                    // str is one line of text; readLine() strips the newline character(s)
                    result = result + str;

                }
                in.close();
                return result;

            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
