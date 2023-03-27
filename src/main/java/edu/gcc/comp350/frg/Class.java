package edu.gcc.comp350.frg;

import java.sql.Time;
import java.util.ArrayList;

public class Class {

    private String code;
    private String title;
    private int referenceNum;
    private Timeslot time; //come back here later
    private Term term;
    private String professor;
    private Department department;
    private boolean isFrance; //lol
    private int credits;
    private String location;
    private String description;


    public Class(String code, String title, int referenceNum, Timeslot time, Term term, String professor, Department department, int credits, String location, String description) {
        this.code = code;
        this.title = title;
        this.referenceNum = referenceNum;
        this.time = time;
        this.term = term;
        this.professor = professor;
        this.department = department;
        this.credits = credits;
        this.location = location;
        this.description = description;
    }

    //stings will be printed out as :
    //Code, Title, Time

    public String toString(Class c){
        StringBuilder classString = new StringBuilder("");
        classString.append(c.code+", ");
        classString.append(c.title+", ");
        classString.append(c.time.toString());
        return classString.toString();
    }

    public int getCredits() {
        return credits;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String  getTitle() {
        return title;
    }

    public int getReferenceNum() {
        return referenceNum;
    }

    public Timeslot getTime() {
        return time;
    }

    public Term getTerm() {
        return term;
    }

    public String getProfessor() {
        return professor;
    }

    public Department getDepartment() {
        return department;
    }
}
