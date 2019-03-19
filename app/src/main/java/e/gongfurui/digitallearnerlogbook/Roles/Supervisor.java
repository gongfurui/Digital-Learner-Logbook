package e.gongfurui.digitallearnerlogbook.Roles;

public class Supervisor extends Role {

  public Supervisor(Role role) {
    super(role.driver_id, role.name, role.email, role.psw, role.date_of_birth);
  }
}