package edu.gcc.comp350.frg;

import java.sql.*;
import java.util.ArrayList;

public class Class {

    public String getCode() {
        return code;
    }

    private String code;
    private String title;
    private int referenceNum;
    private Timeslot time; //come back here later
    private Term term;
    private String professor;
    private String department;  //come back here later
    private boolean isFrance; //lol
    private int credits;
    private String location;
    private String description;


    public Class(Class cl) {
    }

    public Class(String code, String title, int referenceNum, Timeslot time, Term term, String professor, String department, int credits, String location, String description) {
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

    public static Class getClassFromDBbyCourseCode(String courseCode) {
        try {

            Connection conn = DatabaseConnector.connect();

            String sql = "SELECT * FROM classes20to21v3 WHERE course_code LIKE '" + courseCode + "'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            String timeString = rs.getString("begin_tim") + " - " + rs.getString("end_tim");  //TODO: needs to make a timeslot object
            Term classTerm = new Term(rs.getInt("trm_cde"), null);


            Class newClass = new Class(
                    rs.getString("course_code"),
                    rs.getString("crs_title"),
                    0,  // we don't ac
                    null,
                    classTerm,
                    rs.getString("first_name") + " " + rs.getString("last_name"),
                    rs.getString("crs_comp1"),
                    rs.getInt("credit_hrs"),
                    null,
                    rs.getString("comment_txt")
            );

            conn.close();

            return newClass;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //stings will be printed out as :
    //Code, Title, Time



    @Override
    public String toString(){ //add professor and time
        StringBuilder classString = new StringBuilder("");
        classString.append(this.code+", ");
        classString.append(this.title+", ");
        if(this.time != null) {
            classString.append(this.time.toString());
        }
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

    public String getDepartment() {
        return department;
    }
}
