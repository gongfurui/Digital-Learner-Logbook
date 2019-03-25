package e.gongfurui.digitallearnerlogbook.Roles;

public class Competency {
    public int cID;
    public String title;
    public String performance;
    public String conditions;
    public String requirements;
    public String comment;
    public boolean isFinished;
    public String instructorName;

    public Competency(int cID, String title, String performance, String conditions,
                      String requirements, String comment, boolean isFinished, String instructorName){
        this.cID = cID;
        this.title = title;
        this.performance = performance;
        this.conditions = conditions;
        this.requirements = requirements;
        this.comment = comment;
        this.isFinished = isFinished;
        this.instructorName = instructorName;
    }
}
