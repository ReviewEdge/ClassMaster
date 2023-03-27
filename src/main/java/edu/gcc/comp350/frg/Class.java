package edu.gcc.comp350.frg;

import java.sql.Time;

public class Class {

    private String name;
    private int referenceNum;
    private Timeslot time; //come back here later
    private Term term;
    private String professor;
    private Department department;
    private boolean isFrance; //lol
    private int credits;
    private String location;
    private String description;


    public Class(String name, int referenceNum, Timeslot time, Term term, String professor, Department department, int credits, String location, String description) {
        this.name = name;
        this.referenceNum = referenceNum;
        this.time = time;
        this.term = term;
        this.professor = professor;
        this.department = department;
        this.credits = credits;
        this.location = location;
        this.description = description;
    }

    public Class(String parseMe){

    }

    public Class(Class c) {

    }

    public String getName() {
        return name;
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

    public int getCredits() {
        return credits;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}
