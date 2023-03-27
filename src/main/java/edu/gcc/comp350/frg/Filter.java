package edu.gcc.comp350.frg;

import java.sql.Time;
import java.util.ArrayList;

public class Filter {
    // characters that must be in the class name somewhere
    private String contains = null;
    // course code
    private String code = null;
    // the timeslots the class can be in
    private ArrayList<Timeslot> timeslots = null;//TODO: figure out how to do timeslots
    // the professor teaching this class
    private String professor = null;
    // the department this class is in
    // TODO: decide if code should be just the number and have department take up the rest
    private Department department = Department.NONE;
    // determines number of credits wanted, if searching for a specific amount set min and max to same
    private int minCredits = -1;
    private int maxCredits = -1;

    /**
     * creates a filter with the parameters (probably not going to be used)
     * @param contains if the course name contains this string, set to null if unused
     * @param code the course code, set to null if unused
     * @param timeslots TODO: fix this
     * @param professor the professor's name (case insensitive), set to null if unused
     * @param department the department the class is offered by, set to Department.NONE if unused
     * @param minCredits the minimum number of credits the class takes, set to -1 if unused
     * @param maxCredits the maximum number of credits the class takes, set to -1 if unused
     */
    public Filter(String contains, String code, ArrayList<Timeslot> timeslots, String professor, Department department, int minCredits, int maxCredits) {
        this.contains = contains;
        this.code = code;
        this.timeslots = timeslots;
        this.professor = professor;
        this.department = department;
        this.minCredits = minCredits;
        this.maxCredits = maxCredits;
    }

    /**
     * creates an empty filter
     */
    public Filter(){
    }

    public void setContains(String contains) {
        this.contains = contains;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTimeslots(ArrayList<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setMinCredits(int minCredits) {
        this.minCredits = minCredits;
    }

    public void setMaxCredits(int maxCredits) {
        this.maxCredits = maxCredits;
    }

    public String getContains() {
        return contains;
    }

    public String getCode() {
        return code;
    }

    public ArrayList<Timeslot> getTimeslots() {
        return timeslots;
    }

    public String getProfessor() {
        return professor;
    }

    public Department getDepartment() {
        return department;
    }

    public int getMinCredits() {
        return minCredits;
    }

    public int getMaxCredits() {
        return maxCredits;
    }

    public boolean isValid(Class test){
        if(contains != null){
            return false;//TODO: have only return false if fails test for all, this is temporary
        }
        if(code != null){
            return false;
        }
        if(timeslots != null){
            return false;
        }
        if (professor != null){
            return false;
        }
        if (department != Department.NONE){
            return false;
        }
        if (minCredits != -1){
            return false;
        }
        if (maxCredits != -1){
            return false;
        }
        return true;
    }
}
