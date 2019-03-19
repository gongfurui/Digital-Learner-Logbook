package e.gongfurui.digitallearnerlogbook.Roles;

import java.util.ArrayList;

public class Learner extends Role{
  public int super_id;
  public int adi;
  public int time;
  public ArrayList<Boolean> courseProgressList;
  public ArrayList<String> courseCommentList;

  public Learner(Role role, int super_id, int adi, int time, ArrayList<Boolean> courseProgressList,
                 ArrayList<String> courseCommentList){
    super(role.driver_id, role.name, role.email, role.psw, role.date_of_birth);
    this.super_id = super_id;
    this.adi = adi;
    this.time = time;
    this.courseProgressList = courseProgressList;
    this.courseCommentList = courseCommentList;
  }

}