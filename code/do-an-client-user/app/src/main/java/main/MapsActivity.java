package main;

import android.content.Intent;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import profile.ProfileActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.view_profile)
    ImageView viewProfile;
    @BindView(R.id.tv_category)
    TextView tvCategory;

    @BindView(R.id.tv_book)
    TextView tvBook;
    @BindView(R.id.layout_book)
    LinearLayout layoutBook;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


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
}
