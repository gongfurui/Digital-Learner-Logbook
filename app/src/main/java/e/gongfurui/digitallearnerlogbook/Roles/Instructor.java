package e.gongfurui.digitallearnerlogbook.Roles;

public class Instructor extends Role {
  public int ADI;

  public Instructor(Role role, int ADI) {
    super(role.driver_id, role.name, role.email, role.psw, role.date_of_birth);
    this.ADI = ADI;
  }

}
