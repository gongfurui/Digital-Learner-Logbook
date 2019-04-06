package e.gongfurui.digitallearnerlogbook.GPSActivities;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import e.gongfurui.digitallearnerlogbook.Activities.LearnerHomeActivity;
import e.gongfurui.digitallearnerlogbook.Helpers.GoogleMapHelper;
import e.gongfurui.digitallearnerlogbook.Helpers.MarkerAnimationHelper;
import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.Helpers.UiHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Route;
import e.gongfurui.digitallearnerlogbook.Utils.AppRxSchedulers;
import e.gongfurui.digitallearnerlogbook.Utils.LatLngInterpolator;

public class MainActivity extends AppCompatActivity implements Runnable {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2161;

    private GoogleMapHelper googleMapHelper;
    private AppRxSchedulers appRxSchedulers = new AppRxSchedulers();
    private UiHelper uiHelper;
    private MainActivityViewModel viewModel;
    private GoogleMap googleMap;

    private TextView distanceCoveredTextView;
    private TextView timeCoveredTextView;
    private TextView addressCoveredTextView;
    private TextView speedCoveredTextView;

    private double sec;
    private double min;
    private double hour;

    private int learnerID;

    private Handler timer;

    private boolean firstTimeFlag = true;

    private Marker marker;

    private HashSet<String> traceSet = new HashSet<>();
    private ArrayList<Double> speedList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        learnerID = getIntent().getIntExtra("learnerID", 0);
        FusedLocationProviderClient providerClient = LocationServices.getFusedLocationProviderClient(this);
        googleMapHelper = new GoogleMapHelper(getResources());
        uiHelper = new UiHelper(this);
        MainActivityViewModelFactory factory = new MainActivityViewModelFactory(appRxSchedulers, uiHelper.getLocationRequest(), providerClient, googleMapHelper);
        viewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        if (!uiHelper.isPlayServicesAvailable()) {
            Toast.makeText(this, "Play Services did not installed!", Toast.LENGTH_SHORT).show();
            finish();
        } else checkLocationPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(googleMap -> {
            googleMapHelper.defaultMapSettings(googleMap);
            this.googleMap = googleMap;
            startListenNewLocation();
        });
        findViewById(R.id.currentLocationImageButton).setOnClickListener(view -> {
            if (marker == null || googleMap == null) return;
            googleMapHelper.animateCamera(marker.getPosition(), googleMap);
        });

        timer = new Handler();
        hour = 0;
        min = 0;
        sec = 0;
        timer.postDelayed(this,1000);
        initViews();

    }

    private void initViews() {
        distanceCoveredTextView = findViewById(R.id.distanceCoveredTextView);
        timeCoveredTextView = findViewById(R.id.timeCoveredTextView);
        addressCoveredTextView = findViewById(R.id.addressCoveredTextView);
        speedCoveredTextView = findViewById(R.id.speedCoveredTextView);
    }

    private void checkLocationPermission() {
        if (!uiHelper.isHaveLocationPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        if (uiHelper.isLocationProviderEnabled())
            uiHelper.showPositiveDialogWithListener(this, getResources().getString(R.string.need_location), getResources().getString(R.string.location_content), () -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)), "Turn On", false);
        viewModel.requestLocationUpdates();
    }

    private void startListenNewLocation() {
        viewModel.currentLocation()
                .observe(this, location -> {
                    assert location != null;
                    Log.e("Location Received -> ", location.getLatitude() + " , " + location.getLongitude());
                    // Add by Furui Gong in 2019/4/5
                    Log.e("Address Received -> ", getAddressName(new LatLng(location.getLatitude(), location.getLongitude())));
                    addressCoveredTextView.setText(getAddressName(new LatLng(location.getLatitude(), location.getLongitude())));
                    speedCoveredTextView.setText((double) location.getSpeed() + " km/h");
                    traceSet.add(getAddressName(new LatLng(location.getLatitude(), location.getLongitude())));
                    speedList.add((double) location.getSpeed());
                    if (firstTimeFlag) {
                        firstTimeFlag = false;
                        googleMapHelper.animateCamera(new LatLng(location.getLatitude(), location.getLongitude()), googleMap);
                        startDistanceTracking();
                    }
                    showOrAnimateMarker(location);
                });
    }

    private void startDistanceTracking() {
        viewModel.startLocationTracking();
        viewModel.distanceTracker()
                .observe(this, distance -> {
                    Log.e("Total distance -> ", distance);
                    distanceCoveredTextView.setText(distance);
                });
    }

    private void showOrAnimateMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (marker == null)
            marker = googleMap.addMarker(googleMapHelper.getCurrentMarkerOptions(latLng));
        else MarkerAnimationHelper.animateMarkerToGB(marker, latLng, new LatLngInterpolator.Spherical());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            int value = grantResults[0];
            if (value == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            } else if (value == PackageManager.PERMISSION_GRANTED) viewModel.requestLocationUpdates();
        }
    }

    //Modified by Furui in 2019/04/04
    @Override
    public void run() {
        sec++;
        if(sec == 60) {
            min++;
            sec = 0;
        }
        if(min == 60) {
            hour ++;
            min = 0;
        }
        timeCoveredTextView.setText(hour + "h " + min + "m " + sec + "s");
        timer.postDelayed(this,1000);
    }

    //Add by Furui Gong in 2019/4/5
    private String getAddressName(LatLng myCoordinate){
        String myAddress = "";
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinate.latitude, myCoordinate.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            myAddress = address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myAddress;
    }

    // Add by Furui Gong in 2019/4/6
    /**
     * The action after press the end practice button
     * */
    public void practiceEndPressed(View view) {
        int routeID = 0;
        double avgSpeed = 0;
        double total_time1 = sec / 3600 + min / 60 + hour;
        BigDecimal bd = new BigDecimal(total_time1);
        double total_time = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        String str = "";
        String s = distanceCoveredTextView.getText().toString();
        if(!s.isEmpty()) str = s.substring(0, s.length() - 2);
        double distance = 0;
        if(!str.isEmpty()) distance = Double.valueOf(str);

        for (Double e : speedList) {
            avgSpeed += e;
        }
        avgSpeed = avgSpeed / speedList.size();

        HashMap<Integer, Route> routeMap = SQLQueryHelper.getRouteMapFromRouteTable(this,
                "SELECT * FROM route");
        if(routeMap != null){
            routeID = routeMap.size();
        }


        String addressStr = "";

        for (String address : traceSet) {
            addressStr += address + " ";
        }

        Toast.makeText(this, "The route ID is: " + routeID +
                "\nThe avg speed is: " + avgSpeed +
                "\nThe total distance is: " + distance +
                "\nThe total time is: " + total_time +
                "\nLearnerID is: " + learnerID +
                "\nAddresses are: " + addressStr, Toast.LENGTH_LONG).show();

        for (String address : traceSet) {
            SQLQueryHelper.insertDatabase(this,"INSERT into route_address " +
                    "(id, address)" +
                    " VALUES (" + routeID + ", '"+address+"')");
        }
        SQLQueryHelper.insertDatabase(this, "INSERT into route" +
                "(id, distance, time, avgSpeed, learnerID)" +
                " VALUES ("+ routeID +", "+ distance +", "+ total_time +", "+ avgSpeed +", "+ learnerID +")");

        Intent intent = new Intent(this, LearnerHomeActivity.class);
        intent.putExtra("learnerID", learnerID);
        startActivity(intent);
    }
}