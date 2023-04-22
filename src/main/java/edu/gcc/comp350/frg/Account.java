package edu.gcc.comp350.frg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;

public class Account {

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", eMail='" + eMail + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", mySchedules=" + mySchedules +
                '}';
    }

    private int id;
    public String getName() {
        return name;
    }

    private String name;
    private String eMail;
    private String password;
    private String username;
    private ArrayList<Schedule> mySchedules;

    /**
     *  Constructor used for initial account creation, Hashes password and generates a new ID
     *
     * @param name
     * @param eMail
     * @param password
     * @param username
     */
    public Account(String name, String eMail, String password, String username) {
        this.name = name;
        this.eMail = eMail;
        this.password = Account.to_SHA256(password);
        this.username = username;
        this.mySchedules = new ArrayList<>();

        int newID = 0;

        // gets the latest ID in the database
        try {
            Connection conn = DatabaseConnector.connect();
            String sql = "SELECT MAX(ID) AS max_id FROM accounts1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            newID = rs.getInt("max_id") + 1;
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.id = newID;
    }

    /**
     * Constructor that takes every variable as input (Primarily used for database retrievals)
     *
     * @param id
     * @param name
     * @param eMail
     * @param password
     * @param username
     * @param mySchedules
     */
    public Account(int id, String name, String eMail, String password, String username, ArrayList<Schedule> mySchedules) {
        this.id = id;
        this.name = name;
        this.eMail = eMail;
        this.password = password;
        this.username = username;
        this.mySchedules = mySchedules;
    }

    public boolean validatePassword(String checkPassword){

        String checkPasswordEncoding = Account.to_SHA256(checkPassword);

        return password.equals(checkPasswordEncoding);
    }

    /**
     * Converts the str to be hashed using SHA-256, and then converted
     * back to a string with Base64 Encoding
     *
     * @param str String to be converted
     * @return the str after hashing and encoding
     */

    public static String to_SHA256(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] strHash = digest.digest(str.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(strHash);
        } catch(Exception e){
            System.out.println(e.toString());
        }
        return "";
    }

    public ArrayList<Schedule> getSchedules(){
        return mySchedules;
    }

    public void addSchedule(Schedule sch){
        mySchedules.add(sch);
    }

    public void removeSchedule(int i){
        mySchedules.remove(i);
    }

    // commit account to database
    public void saveOrUpdateAccount() throws SQLException {

        String sql = "REPLACE INTO accounts1(ID,name,email,password,username,schedules) VALUES(?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, this.id);
            pstmt.setString(2, this.name);
            pstmt.setString(3, this.eMail);
            pstmt.setString(4, this.password);
            pstmt.setString(5, this.username);

            JSONObject json = new JSONObject();
            JSONArray scheduleJSONArray = new JSONArray();

            if (this.mySchedules != null) {
                for (int i = 0; i < this.mySchedules.size(); i++) {
                    scheduleJSONArray.put(this.mySchedules.get(i).getId());
                }
            }

            json.put("schedulesString", scheduleJSONArray);
            String schedulesJson = json.toString();

            pstmt.setString(6, schedulesJson);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public static Account getAccountByIdFromDB(int id) throws SQLException {
        String sql = "SELECT * FROM accounts1 WHERE ID = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            String schedulesString = rs.getString("schedules");

            JSONObject schedulesJSON = new JSONObject(schedulesString);
            JSONArray schedulesJSONArray = schedulesJSON.optJSONArray("schedulesString");

            ArrayList<Schedule> newSchedules = new ArrayList<>();
            if (schedulesJSONArray != null) {
                for (int i=0;i<schedulesJSONArray.length();i++){
                    newSchedules.add(Schedule.getScheduleByIDFromDB((Integer) schedulesJSONArray.get(i)));
                }
            }

            Account newAccount = new Account(
                    rs.getInt("ID"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("username"),
                    newSchedules
            );

            conn.close();
            return newAccount;

        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return null;
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Account> getAccountsByUsernameFromDB(String username) throws Exception{
        String sql = "SELECT * FROM accounts1 WHERE username = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Account> accounts = new ArrayList<>();

            while(rs.next()) {
                String schedulesString = rs.getString("schedules");

                JSONObject schedulesJSON = new JSONObject(schedulesString);
                JSONArray schedulesJSONArray = schedulesJSON.optJSONArray("schedulesString");

                ArrayList<Schedule> newSchedules = new ArrayList<>();
                if (schedulesJSONArray != null) {
                    for (int i = 0; i < schedulesJSONArray.length(); i++) {
                        newSchedules.add(Schedule.getScheduleByIDFromDB((Integer) schedulesJSONArray.get(i)));
                    }
                }

                accounts.add(new Account(
                        rs.getInt("ID"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("username"),
                        newSchedules
                ));
            }

            conn.close();
            return accounts;

        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return null;
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteAccountByIDFromDB(int id) {
        try {
            //Deletes associated Schedules
            Account account = getAccountByIdFromDB(id);
            if(account == null){
                return;
            }
            for(Schedule sch: account.getSchedules()){
                Schedule.deleteScheduleByIDFromDB(sch.getId());
            }

            //Deletes account
            Connection conn = DatabaseConnector.connect();
            String sql = "DELETE FROM accounts1 WHERE ID = '" + id + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
        }
    }

//    public static void main(String[] args){
//        for(int i = 0; i < 100; i++){
//            try{
//                deleteAccountByIDFromDB(i);
//            } catch (Exception e){
//
//            }
//        }
//    }
}
