package e.gongfurui.digitallearnerlogbook.Roles;

import java.util.ArrayList;
import java.util.HashSet;

public class Route {
    public int routeID;
    public HashSet<String> traceMap;
    public double distance;
    public double time;
    public double avgSpeed;
    public Learner learner;
    public boolean isApproved;

    public Route(int routeID, HashSet<String> traceMap, double distance, double time,
                 double avgSpeed, Learner learner, boolean isApproved){
        this.routeID = routeID;
        this.traceMap = traceMap;
        this.distance = distance;
        this.time = time;
        this.avgSpeed = avgSpeed;
        this.learner = learner;
        this.isApproved = isApproved;
    }

}
