package e.gongfurui.digitallearnerlogbook.GPSActivities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Route;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final float ZOOM_LEVEL = 18f;



    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment != null) mapFragment.getMapAsync(this);
        String routeJson = getIntent().getStringExtra("route");
        route = new Gson().fromJson(routeJson, Route.class);
        initViews();
    }

    private void initViews() {
        TextView distanceCoveredTextView;
        TextView timeCoveredTextView;
        TextView speedCoveredTextView;
        distanceCoveredTextView = findViewById(R.id.distanceCoveredTextViewShow);
        timeCoveredTextView = findViewById(R.id.timeCoveredTextViewShow);
        speedCoveredTextView = findViewById(R.id.speedCoveredTextViewShow);

        distanceCoveredTextView.setText(String.valueOf(route.distance) + " KM");
        timeCoveredTextView.setText(String.valueOf(route.time) + " h");
        speedCoveredTextView.setText(String.valueOf(route.avgSpeed) + " km/h");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        int i = 0;
        for (LatLng ll : route.traceSet) {
            googleMap.addMarker(new MarkerOptions().position(ll));
            if(i == 0) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, ZOOM_LEVEL));
            }
            i++;
        }

    }
}
