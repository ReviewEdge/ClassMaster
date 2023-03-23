package edu.gcc.comp350.frg;

import java.sql.*;

public class Class {

    private String name;
//    private int referenceNum;  // right now just putting in the course code, eg. COMP 205 A
    private String courseCode;
    private String time; // *note on time: for now, switching it to a string for simplicity
    private Term term;
    private String professor;
    private String department;  // Department enum needs work. using string for now
    private boolean isFrance; //lol
    private int credits;
    private String location;
    private String description;

    public Class(String name, String courseCode, String time, Term term, String professor, String department, int credits, String location, String description) {
        this.name = name;
        this.courseCode = courseCode;
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

    public static Class getClassFromDBbyCourseCode(String courseCode) {
        try {

            Connection conn = DatabaseConnector.connect();

            String sql = "SELECT * FROM classes20to21v3 WHERE course_code LIKE '" + courseCode + "'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            String timeString = rs.getString("begin_tim") + " - " + rs.getString("end_tim");
            Term classTerm = new Term(rs.getInt("trm_cde"), null);

            Class newClass = new Class(
                    rs.getString("crs_title"),
                    rs.getString("course_code"),
                    timeString,
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


    @Override
    public String toString() {
        return "Class{" +
                "name='" + name + '\'' +
                ", courseCode=" + courseCode +
                ", time='" + time + '\'' +
                ", term=" + term +
                ", professor='" + professor + '\'' +
                ", department=" + department +
                ", credits=" + credits +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                '}';

    }
    
    public void getDescription(){

    }

    public void addClass(){

    }


}
