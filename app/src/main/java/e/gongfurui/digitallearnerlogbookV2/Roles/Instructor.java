package e.gongfurui.digitallearnerlogbookV2.Roles;

public class Instructor extends Role {
    public int ADI;

    public Instructor(Role role, int ADI) {
      super(role.name, role.email, role.psw);
      this.ADI = ADI;
    }

}
