package edu.gcc.comp350.frg;

import java.sql.*;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Schedule {

    private String name;
    private int id;
    private Term term;
    private ArrayList<Class> classes;
    private int currentcredits;

    public Schedule(String name, Term term, ArrayList<Class> classes) {
        this.name = name;
        this.term = term;
        if (classes == null) {
            this.classes = new ArrayList<>();

        } else {
            this.classes = classes;
        }

        int newID = 0;

        // gets the latest ID in the database
        try {
            Connection conn = DatabaseConnector.connect();
            String sql = "SELECT MAX(ID) AS max_id FROM schedules1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            newID = rs.getInt("max_id") + 1;
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.id = newID;
    }

    public Schedule(String name, int id, Term term) {
        this.name = name;
        this.id = id;
        this.term = term;
        this.classes = new ArrayList<>();
        currentcredits = 0;
    }


    public Schedule(String name, int id, Term term, ArrayList<Class> classes) {
        this.name = name;
        this.id = id;
        this.term = term;
        this.classes = classes;
    }


    //dont worry about this yet pls
//    public Schedule( Schedule other) {
//        this.name = other.name;
//        this.id = other.id;
//        this.term = other.term;
//        this.classes = new ArrayList<>();
//        for(Class c: other.classes){
//            this.classes.add(new Class(c));
//        }
//    }
    public String toString(){
        StringBuilder scheduleString = new StringBuilder();
        scheduleString.append(name).append(" for semester ").append(term).append(" has ").append(currentcredits).append(" credits\n");
        for(int i = 0; i < classes.size(); i++){
            scheduleString.append(i+1).append( ": ");
            scheduleString.append(classes.get(i)).append("\n");
        }
        return scheduleString.toString();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Term getTerm() {
        return term;
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addClass (Class toAdd) throws Exception{
        if (!toAdd.getTerm().equals(this.term)) {
            throw new Exception("class term is different from your schedule's term");
        }

        if (currentcredits + toAdd.getCredits() >= 21){
            System.out.println("Your schedule has over 20 credits!");
//            throw new Exception("too many credits!");
        }

        ArrayList<Timeslot> toAddSlots = toAdd.getTimeSlots();

        // for every class currently in the schedule...
        for (Class c: classes){
            ArrayList<Timeslot> currClassSlots = c.getTimeSlots();
            // for every timeslot of a current class...
            for (int i = 0; i < currClassSlots.size(); i++) {
                // for every timeslot of the adding class...
                for (int j = 0; j < toAddSlots.size(); j++) {
                    // checks if one of the classes overlaps
                    if (currClassSlots.get(i).overlaps(toAddSlots.get(j))) {
                        //TODO: eventually we'll want this to actually resolve the conflict intelligently, prompt user
//                        Class newClass = ResolveConflict(c,toAdd);
//                        classes.remove(c);
//                        currentcredits-=c.getCredits();
//                        classes.add(newClass);
//                        currentcredits+= newClass.getCredits();
                        throw new Exception("overlaps class in schedule");
                    }
                }
            }
        }

        currentcredits = currentcredits + toAdd.getCredits();
        classes.add(toAdd);
    }


    public void removeClass (int i) {
        Class removedClass = classes.get(i);
        currentcredits = currentcredits - removedClass.getCredits();
        classes.remove(i);

    }

    public void removeClass (Class toRemove) {
        currentcredits = currentcredits - toRemove.getCredits();
       classes.remove(toRemove);
    }

    private Class ResolveConflict(Class preexist, Class newexist){
        System.out.println("Classes Overlap!"); //TODO: this should be throwing an exception (which gets caught in API?)
        //nah we're not gonna let you add that class. this will be changed later
        return preexist;
    }


    //TODO: issues with this!!
    public ArrayList<Integer> getRefNums(){
        int i = 0;
        ArrayList<Integer> refNums= new ArrayList<>();
        for (Class c: classes){
            refNums.set(i, c.getReferenceNum());
            i++;
        }
        return refNums;
    }

    public ArrayList<String> getClassNames(){
        int i = 0;
        ArrayList<String> classNames= new ArrayList<>();
        for (Class c: classes){
            classNames.set(i, c.getTitle());
            i++;
        }
        return classNames;
    }


    // commit schedule to database
    public void saveSchedule() {

        String sql = "REPLACE INTO schedules1(ID,term,name,classes) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.id);
            pstmt.setString(2, this.term.toString());
            pstmt.setString(3, this.name);

            JSONObject json = new JSONObject();
            JSONArray classJSONArray = new JSONArray();

            for (int i = 0; i < this.classes.size(); i++) {
                classJSONArray.put(this.classes.get(i).getCode());

            }
            json.put("classesString", classJSONArray);
            String classesJSON = json.toString();

            pstmt.setString(4, classesJSON);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static ArrayList<Integer> getAllScheduleIDsFromDB() {
        try {
            Connection conn = DatabaseConnector.connect();

            String sql = "SELECT * FROM schedules1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            ArrayList<Integer> schedIDs = new ArrayList<>();
            while (rs.next()) {
                schedIDs.add(rs.getInt("ID"));
            }
            conn.close();
            return schedIDs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public static Schedule getScheduleByIDFromDB(int id) throws Exception{
        String sql = "SELECT * FROM schedules1 WHERE ID = ?";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            String classesString = rs.getString("Classes");
            JSONObject classesJSON = new JSONObject(classesString);
            JSONArray classesJSONArray = classesJSON.optJSONArray("classesString");

            ArrayList<Class> newClasses = new ArrayList<>();
            if (classesJSONArray != null) {
                for (int i=0;i<classesJSONArray.length();i++){
                    newClasses.add(Class.getClassFromDBbyCourseCode((String) classesJSONArray.get(i)));
                }
            }

            //TODO: add parseMe to term so I can get term from DB
            Term schedTerm = new Term(rs.getString("Term"));
            Schedule newSchedule = new Schedule(
                    rs.getString("Name"),
                    rs.getInt("ID"),
                    schedTerm,
                    newClasses
            );

            conn.close();
            return newSchedule;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void deleteScheduleByIDFromDB(int id) {
        try {
            Connection conn = DatabaseConnector.connect();

            String sql = "DELETE FROM schedules1 WHERE ID = '" + id + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
        }
    }

//    @Override
//    public String toJSON(){
//        StringBuilder scheduleString = new StringBuilder();

//        return scheduleString.toString();
//    }


//    public static void main(String[] args){
//        for(int i = 0; i < 100; i++){
//            try{
//                deleteScheduleByIDFromDB(i);
//            } catch (Exception e){
//
//            }
//        }
//    }

}

