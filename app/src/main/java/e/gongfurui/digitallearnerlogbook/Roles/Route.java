package e.gongfurui.digitallearnerlogbook.Roles;

import java.util.ArrayList;

public class Route {
    int routeID;
    ArrayList<String> traceList;
    double distance;
    double time;
    double avgSpeed;
    Learner learner;
    boolean isApproved;

    public Route(int routeID, ArrayList<String> traceList, double distance, double time,
                 double avgSpeed, Learner learner, boolean isApproved){
        this.routeID = routeID;
        this.traceList = traceList;
        this.distance = distance;
        this.time = time;
        this.avgSpeed = avgSpeed;
        this.learner = learner;
        this.isApproved = isApproved;
    }

}
