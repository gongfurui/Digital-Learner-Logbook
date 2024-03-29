package e.gongfurui.digitallearnerlogbook.GPSActivities;

import android.content.Intent;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import e.gongfurui.digitallearnerlogbook.Activities.ForgetPswActivity;
import e.gongfurui.digitallearnerlogbook.Activities.SupervisorLearnersActivity;
import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;
import e.gongfurui.digitallearnerlogbook.Roles.Route;
import e.gongfurui.digitallearnerlogbook.Roles.Supervisor;
import e.gongfurui.digitallearnerlogbook.Utils.EmailUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final float ZOOM_LEVEL = 18f;

    private Supervisor supervisor;
    private Learner learner;

    private String supervisorMail;
    private int learnerID;


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
        supervisorMail = getIntent().getStringExtra("supervisorMail");
        learnerID = getIntent().getIntExtra("learnerID", 0);
        supervisor = SQLQueryHelper.searchSupervisorTable(this,
                "SELECT * FROM supervisor WHERE email = '"+ supervisorMail + "'");
        learner = SQLQueryHelper.searchLearnerTable(this,
                "SELECT * FROM learner WHERE id = "+ learnerID);
        initViews();
    }

    private void initViews() {
        TextView distanceCoveredTextView;
        TextView timeCoveredTextView;
        TextView speedCoveredTextView;
        Button buttonApproved;
        Button buttonDisapproved;

        distanceCoveredTextView = findViewById(R.id.distanceCoveredTextViewShow);
        timeCoveredTextView = findViewById(R.id.timeCoveredTextViewShow);
        speedCoveredTextView = findViewById(R.id.speedCoveredTextViewShow);
        buttonApproved = findViewById(R.id.btn_approve);
        buttonDisapproved = findViewById(R.id.btn_disapprove);

        distanceCoveredTextView.setText(String.valueOf(route.distance) + " KM");
        timeCoveredTextView.setText(String.valueOf(route.time) + " h");
        speedCoveredTextView.setText(String.valueOf(route.avgSpeed) + " km/h");

        if(supervisorMail == null){
            buttonApproved.setVisibility(View.INVISIBLE);
            buttonDisapproved.setVisibility(View.INVISIBLE);
        }

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

    /**
     * The actions when supervisor approve the practicing route.
     * */
    public void approvePressed(View view) {
        if(supervisorMail != null){
            double updated_distance = learner.distance + route.distance;
            SQLQueryHelper.updateDatabase(this,
                    "UPDATE learner SET distance =" + updated_distance + " WHERE id = " +
                            learnerID);
            double updated_time = learner.time + route.time;
            SQLQueryHelper.updateDatabase(this,
                    "UPDATE learner SET time =" + updated_time + " WHERE id = " +
                            learnerID);
            SQLQueryHelper.updateDatabase(this,
                    "UPDATE route SET isApproved = " + 1 + " WHERE id = " +
                            route.routeID);

            notifyTheApprovement(learner.email, supervisor.name, route.routeID, true);
            Toast.makeText(MapsActivity.this,"Approved the practicing route", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, SupervisorLearnersActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("learnerID", learnerID);
            bundle.putString("supervisorMail", supervisorMail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * The actions when supervisor approve the practicing route.
     * */
    public void disapprovePressed(View view) {
        if(supervisorMail != null){
            SQLQueryHelper.updateDatabase(this,
                    "UPDATE route SET isApproved = " + 1 + " WHERE id = " +
                            route.routeID);
            notifyTheApprovement(learner.email, supervisor.name, route.routeID, false);
            Toast.makeText(MapsActivity.this,"Disapproved the practicing route", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, SupervisorLearnersActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("learnerID", learnerID);
            bundle.putString("supervisorMail", supervisorMail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    private void notifyTheApprovement(String emailTo, String supervisorName, int routeID, boolean isApprove) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                if(isApprove) {
                    EmailUtil.getInstance().sendEmail(emailTo, "Approving of route " + routeID,
                            "Your practicing route ID: " + routeID + " with distance: " +
                                    route.distance + ", driving hours: " + route.time +
                                    " and average speed: " + route.avgSpeed +
                                    " has been approved by your supervisor: " + supervisorName);
                } else {
                    EmailUtil.getInstance().sendEmail(emailTo, "Disapproving of route " + routeID,
                            "Your practicing route ID: " + routeID +
                                    " has been disapproved by your supervisor: " + supervisorName);
                }
                Looper.prepare();
                Looper.loop();
            }
        };
        thread.start();
    }
}
