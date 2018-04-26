package map;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.doan.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import profile.ProfileActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.view_profile)
    ImageView viewProfile;
    @BindView(R.id.sw_ready)
    Switch swReady;
    @BindView(R.id.fab_matdo)
    FloatingActionButton fabMatdo;
    @BindView(R.id.fab_location)
    FloatingActionButton fabLocation;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        initMap();
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    CustemMaps mCustemMaps;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mCustemMaps = new CustemMaps(mMap, this);
        mCustemMaps.moveToMyLocation();
        swReady.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Snackbar.make(swReady, "Sẵn sàng nhận cuốc xe", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(swReady, "Không sẵn sàng nhận cuốc xe", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    @OnClick({R.id.view_profile, R.id.fab_matdo, R.id.fab_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_matdo:
                break;
            case R.id.fab_location:
                mCustemMaps.moveToMyLocation();
                break;
        }
    }
}
