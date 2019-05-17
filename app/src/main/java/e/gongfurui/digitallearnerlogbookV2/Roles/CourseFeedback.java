package e.gongfurui.digitallearnerlogbookV2.Roles;

public class CourseFeedback {
    public int cID;
    public String learnerMail;
    public String instructorName;
    public int ADI;
    public String feedback;
    public String date;

    public CourseFeedback(int cID, String learnerMail, String instructorName, int ADI,
                          String feedback, String date){
        this.cID = cID;
        this.learnerMail = learnerMail;
        this.instructorName = instructorName;
        this.ADI = ADI;
        this.feedback = feedback;
        this.date = date;
    }
}
