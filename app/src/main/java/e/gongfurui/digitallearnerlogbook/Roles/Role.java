package e.gongfurui.digitallearnerlogbook.Roles;

public class Role {
  public int driver_id;
  public String name, email, psw, date_of_birth;

  public Role(int driver_id, String name, String email, String psw, String date_of_birth){
    this.driver_id = driver_id;
    this.name = name;
    this.email = email;
    this.psw = psw;
    this.date_of_birth = date_of_birth;
  }

}