package edu.gcc.comp350.frg;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScheduleForm {

    private String scheduleID;
    private String courseCode;


    public ScheduleForm(String scheduleID, String courseCode) {
        this.scheduleID = scheduleID;
        this.courseCode = courseCode;
    }

    public String getScheduleID() {
        return scheduleID;
    }

    public String getCourseCode() {
        return courseCode;
    }

}
