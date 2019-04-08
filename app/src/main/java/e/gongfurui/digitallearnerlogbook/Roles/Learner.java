package e.gongfurui.digitallearnerlogbook.Roles;

import java.util.ArrayList;

public class Learner extends Role{
    public int driver_id;
    public String date_of_birth;
    public double time;
    public double distance;

    public ArrayList<Boolean> courseProgressList;
    public ArrayList<String> courseCommentList;

    public Learner(Role role, int driver_id, String date_of_birth, double time, double distance,
                   ArrayList<Boolean> courseProgressList, ArrayList<String> courseCommentList){
        super(role.name, role.email, role.psw);
        this.driver_id = driver_id;
        this.date_of_birth = date_of_birth;
        this.time = time;
        this.distance = distance;
        this.courseProgressList = courseProgressList;
        this.courseCommentList = courseCommentList;
    }
}