package edu.gcc.comp350.frg;

public class AddDropForm {

    private String scheduleID;
    private String courseCode;
    private String addDrop;


    public AddDropForm(String scheduleID, String courseCode, String addDrop) {
        this.scheduleID = scheduleID;
        this.courseCode = courseCode;
        this.addDrop = addDrop;
    }

    public String getScheduleID() {
        return scheduleID;
    }

    public String getCourseCode() {
        return courseCode;
    }
    public String getAddDrop() {
        return addDrop;
    }

}
