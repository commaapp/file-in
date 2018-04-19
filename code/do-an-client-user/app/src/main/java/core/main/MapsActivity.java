package core.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.doan.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        PlaceAutocompleteFragment autocompleteFragmentFrom =
                (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.find_location_from);
        autocompleteFragmentFrom.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

            }

            @Override
            public void onError(Status status) {

            }
        });

        autocompleteFragmentFrom.setHint("Bạn đang ở đâu?");
        PlaceAutocompleteFragment autocompleteFragmentTo =
                (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.find_location_to);
        autocompleteFragmentTo.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

            }

            @Override
            public void onError(Status status) {

            }
        });
        autocompleteFragmentTo.setHint("Bạn muốn đi đến đâu?");
        ImageView searchIconFrom = (ImageView) ((LinearLayout) autocompleteFragmentFrom.getView()).getChildAt(0);
        ImageView searchIconTo = (ImageView) ((LinearLayout) autocompleteFragmentTo.getView()).getChildAt(0);

        searchIconFrom.setImageDrawable(getResources().getDrawable(R.drawable.ic_dot));
        searchIconTo.setImageDrawable(getResources().getDrawable(R.drawable.ic_location));


    }


}
