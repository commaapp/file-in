package map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Duong on 10/16/2016.
 */

public class MapManager extends CustemMaps {
    public MapManager(GoogleMap googleMap, Context context) {
        super(googleMap, context);
    }
//        implements
//        GoogleMap.OnInfoWindowClickListener, PlaceSelectionListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
//    private NavigationActivity navigationActivity;
//    private Marker markerSearch;
//    private Marker makerSelect;
//
//    public MapManager(GoogleMap googleMap, Context context) {
//        super(googleMap, context);
//        navigationActivity = (NavigationActivity) context;
//        navigationActivity.registerForContextMenu(navigationActivity.getViewMapMenu());
//        googleMap.setInfoWindowAdapter(new com.haui.map.CusteamInForWindow(context));
//        googleMap.setOnInfoWindowClickListener(this);
//        googleMap.setOnMapLongClickListener(this);
//        googleMap.setOnMarkerClickListener(this);
//        PlaceAutocompleteFragment autocompleteFragment =
//                (PlaceAutocompleteFragment) navigationActivity.getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//        autocompleteFragment.setOnPlaceSelectedListener(this);
//
//        autocompleteFragment.setHint("Tìm kiếm");
//        createSlidePanel();
//
//    }
//
//    private void createSlidePanel() {
////        navigationActivity.getSlidingUpPanelLayout().setAnchorPoint(navigationActivity.getCardViewHeard().getHeight());
////        navigationActivity.getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
////        navigationActivity.getSlidingUpPanelLayout().setAnchorPoint(0.4f);
//
////        android.util.Log.e("faker",""+navigationActivity.getCardViewHeard().getHeight());
//    }
//
//
//    public void setHienXe() {
//        getGoogleMap().clear();
//        for (XeTimNguoi xeTimNguoi : navigationActivity.getArrXeTimNguois()) {
//            double a = Double.parseDouble(xeTimNguoi.getLocation().getLat());
//            double b = Double.parseDouble(xeTimNguoi.getLocation().getLng());
//            if (a != 0.0 || b != 0.0) {
//                drawMarker(a, b, BitmapDescriptorFactory.fromResource(R.drawable.ic_driver), xeTimNguoi.getThongDiep() + "\n" + xeTimNguoi.getMaSV(),
//                        xeTimNguoi.getViTri()).setTag(xeTimNguoi);
//
//            }
//        }
//    }
//
//    public void setHienNguoi() {
//        getGoogleMap().clear();
//        for (NguoiTimXe nguoiTimXe : navigationActivity.getArrNguoiTimXes()) {
//            double a = Double.parseDouble(nguoiTimXe.getLocation().getLat());
//            double b = Double.parseDouble(nguoiTimXe.getLocation().getLng());
//            if (a != 0.0 || b != 0.0) {
//                drawMarker(a, b, BitmapDescriptorFactory.fromResource(R.drawable.ic_user), nguoiTimXe.getThongDiep() + "\n" +
//                        nguoiTimXe.getMaSV() + "\n" + nguoiTimXe.getDiemDen(), nguoiTimXe.getViTri()).setTag(nguoiTimXe);
//
//            }
//
//        }
//    }
//
//    private Marker markerCick;
//
//    public void setMarkerFlag(Marker markerFlag) {
//        this.markerFlag = markerFlag;
//    }
//
//    public Marker getMarkerFlag() {
//
//        return markerFlag;
//    }
//
//    private Marker markerFlag;
//
//    public Marker getMarkerCick() {
//        return markerCick;
//    }
//
//    public void setMarkerCick(Marker markerCick) {
//        this.markerCick = markerCick;
//    }
//
//    @Override
//    public void onInfoWindowClick(Marker marker) {
//        setMarkerCick(marker);
//        navigationActivity.openContextMenu(navigationActivity.getViewMapMenu());
//    }
//
//    @Override
//    public void onPlaceSelected(Place place) {
//        if (place.getLatLng() != null) {
//            if (markerSearch != null) {
//                markerSearch.remove();
//            }
//            if (polylineChiDuong != null) {
//                polylineChiDuong.remove();
//            }
//            markerSearch = drawMarker(place.getLatLng().latitude, place.getLatLng().longitude, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN), place.getName().toString(), place.getAddress().toString());
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerSearch.getPosition(), 13);
//            getGoogleMap().animateCamera(cameraUpdate);
//        }
//
//    }
//
//    @Override
//    public void onError(Status status) {
//
//    }
//
//    @Override
//    public void onMapLongClick(LatLng latLng) {
//        if (makerSelect != null) {
//            makerSelect.remove();
//        }
//        makerSelect = drawMarker(latLng.latitude, latLng.longitude,
//                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
//                , "Điểm chọn", getNameByLocation(latLng.latitude, latLng.longitude));
//    }
//
//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        setMarkerCick(marker);
//        return false;
//    }
}
