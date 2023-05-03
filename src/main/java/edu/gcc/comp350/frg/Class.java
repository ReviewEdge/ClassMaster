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
    private ArrayList<Timeslot> timeSlots;
    private Term term;
    private String professor;
    private String department;
    private int credits;
    private String location;
    private String description;

    //TODO: is this doing a proper deep copy?
    // @Person: I think so, I changed to loop through timeslots -Casey
    public Class(Class c) {
        this.code = c.getCode();
        this.title = c.getTitle();
        this.referenceNum = c.getReferenceNum();
        timeSlots = new ArrayList<>();
        for(Timeslot t: c.getTimeSlots()){
            timeSlots.add(new Timeslot(t));
        }
        this.term = c.getTerm();
        this.professor = c.getProfessor();
        this.department = c.getDepartment();
        this.credits = c.getCredits();
        this.location = c.getLocation();
        this.description = c.getDescription();
    }

    public Class(String code, String title, int referenceNum, ArrayList<Timeslot> timeSlots, Term term, String professor, String department, int credits, String location, String description) {
        this.code = code;
        this.title = title;
        this.referenceNum = referenceNum;
        this.timeSlots = timeSlots;
        this.term = term;
        this.professor = professor;
        this.department = department;
        this.credits = credits;
        this.location = location;
        this.description = description;
    }


    public static ArrayList<Class> getClassesFromDBbySearchTerm(String searchTerm) {
        try {

            Connection conn = DatabaseConnector.connect();

            String sql = "SELECT * FROM classes20to21v4 WHERE search_terms LIKE '%" + searchTerm + "%'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // returns null if no classes were found
            if (rs.getString("unique_code") == null) {
                return null;
            }

            ArrayList<Class> classesResults = new ArrayList<>();
            while (rs.next()) {
                Class newClass = makeNewClassFromRS(rs);
                if (newClass != null) {
                    classesResults.add(newClass);
                }
            }

            conn.close();

            return classesResults;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public static Class getClassFromDBbyCourseCode(String courseCode) {
        try {
            Connection conn = DatabaseConnector.connect();

            String sql = "SELECT * FROM classes20to21v4 WHERE unique_code LIKE '" + courseCode + "'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            Class newClass = makeNewClassFromRS(rs);

            conn.close();

            return newClass;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ArrayList<Class> getAllClassesFromDB() {
        try {

            Connection conn = DatabaseConnector.connect();

            String sql = "SELECT * FROM classes20to21v4";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // returns null if no classes were found
            if (rs.getString("unique_code") == null) {
                return null;
            }

            ArrayList<Class> classesResults = new ArrayList<>();
            while (rs.next()) {
                Class newClass = makeNewClassFromRS(rs);
                if (newClass != null) {
                    classesResults.add(newClass);
                }
            }

            conn.close();

            return classesResults;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    private static Class makeNewClassFromRS(ResultSet rs) throws SQLException {
        // returns null if no class was found
        if (rs.getString("unique_code") == null) {
            return null;
        }
        Term classTerm = null;
        // checks if the class has a term
        if (rs.getInt("trm_cde") != 0) {
            classTerm = new Term(rs.getInt("trm_cde"));
        }
        Class newClass = new Class(
                rs.getString("unique_code"),
                rs.getString("crs_title"),
                0,  // we don't ac
                getTimeslotsFromClassRow(rs),
                classTerm,
                rs.getString("first_name") + " " + rs.getString("last_name"),
                rs.getString("crs_comp1"),
                rs.getInt("credit_hrs"),
                null,
                rs.getString("comment_txt")
        );

        return newClass;
    }


    private static ArrayList<Timeslot> getTimeslotsFromClassRow(ResultSet rs) throws SQLException {
        ArrayList<Day> days = new ArrayList<>();

        if(rs.getString("monday_cde") != null) {
            days.add(Day.Monday);
        }
        if(rs.getString("tuesday_cde") != null) {
            days.add(Day.Tuesday);
        }
        if(rs.getString("wednesday_cde") != null) {
            days.add(Day.Wednesday);
        }
        if(rs.getString("thursday_cde") != null) {
            days.add(Day.Thursday);
        }
        if(rs.getString("friday_cde") != null) {
            days.add(Day.Friday);
        }

        String start12hr = rs.getString("begin_tim");
        // returns a null timeslot if the time is null in the database
        if (start12hr == null) {
            return null;
        }
        String start = convert12to24hr(start12hr);
        String end = convert12to24hr(rs.getString("end_tim"));

        ArrayList<Timeslot> slots = new ArrayList<>();

        for (int i = 0; i < days.size(); i++) {
            slots.add(new Timeslot(start, end, days.get(i)));
        }

        return slots;
    }

    private static String convert12to24hr(String dbTimeStr) {
        String[] timeAndAMorPm = dbTimeStr.split(" ");
        if (timeAndAMorPm[1].equals("PM")) {
            String[] timeNums = timeAndAMorPm[0].split(":");
            int tfHour = Integer.parseInt(timeNums[0]) + 12;
            if(tfHour == 24){
                tfHour -= 12;
            }
            return tfHour + ":" + timeNums[1] + ":" + timeNums[2];
        }

        return timeAndAMorPm[0];
    }


    public String getCourseCodeWithoutTerm() {
        String[] partsOfCC = this.code.split(" ");
        String courseCodeWithoutTerm;
        try {
            courseCodeWithoutTerm = partsOfCC[2] + " " + partsOfCC[3] + " " + partsOfCC[4];
        } catch (ArrayIndexOutOfBoundsException e) {
            courseCodeWithoutTerm = partsOfCC[2] + " " + partsOfCC[3];
        }
        return courseCodeWithoutTerm;
    }


    @Override
    public String toString(){
        StringBuilder classString = new StringBuilder();
        //classString.append(getCourseCodeWithoutTerm()+", ");
        classString.append(this.title+", ");
        if(this.term != null) {
            classString.append(this.term + ", ");
        } else {
            classString.append("No Term" + ", ");
        }
        if(this.timeSlots != null) {
            classString.append(this.timeSlots + ", ");
        } else {
            classString.append("No Timeslot" + ", ");
        }
        classString.append(this.professor);
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

    public ArrayList<Timeslot> getTimeSlots() {
        return timeSlots;
    }

    public Timeslot getTimeOnDay(int day){
        for(Timeslot t: timeSlots){
            if(t.getDay().ordinal() == day){
                return t;
            }
        }
        return null;
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
