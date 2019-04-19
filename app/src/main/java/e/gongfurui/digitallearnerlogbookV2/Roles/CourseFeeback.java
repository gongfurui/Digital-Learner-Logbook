package e.gongfurui.digitallearnerlogbookV2.Roles;

public class CourseFeeback {
    public int cID;
    public String learnerMail;
    public String instructorName;
    public String feedback;

    public CourseFeeback(int cID, String learnerMail, String instructorName, String feedback){
        this.cID = cID;
        this.learnerMail = learnerMail;
        this.instructorName = instructorName;
        this.feedback = feedback;
    }
}
