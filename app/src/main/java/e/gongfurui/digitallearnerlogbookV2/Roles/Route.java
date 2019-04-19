package e.gongfurui.digitallearnerlogbookV2.Roles;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;

public class Route {
    public int routeID;
    public HashSet<LatLng> traceSet;
    public double distance;
    public double time;
    public double avgSpeed;
    public int learnerID;
    public boolean isApproved;

    public Route(int routeID, HashSet<LatLng> traceSet, double distance, double time,
                 double avgSpeed, int learnerID, boolean isApproved){
        this.routeID = routeID;
        this.traceSet = traceSet;
        this.distance = distance;
        this.time = time;
        this.avgSpeed = avgSpeed;
        this.learnerID = learnerID;
        this.isApproved = isApproved;
    }

}
