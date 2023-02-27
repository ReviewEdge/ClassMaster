package edu.gcc.comp350.frg;

import java.sql.Time;

public class Class {

    private String name;
    private int referenceNum;
    private Time time; //come back here later
    private Term term;
    private String professor;
    private Enum department;
    private boolean isFrance; //lol
    private int credits;
    private String location;
    private String description;


    public Class(String name, int referenceNum, Time time, Term term, String professor, Enum department, int credits, String location, String description) {
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


}
