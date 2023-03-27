package edu.gcc.comp350.frg;

import java.util.ArrayList;

public class Account {

    private int id;
    private String name;
    private String eMail;
    private String password;
    private String username;
    private ArrayList<Schedule> mySchedules;

    public Account(int id, String name, String eMail, String password, String username) {
        this.id = id;
        this.name = name;
        this.eMail = eMail;
        this.password = password;
        this.username = username;
        this.mySchedules = new ArrayList<Schedule>();
    }

    public boolean login(String checkPassword){
        return false;
    }
    public boolean logout(){
        return false;
    }

    public ArrayList<Schedule> getSchedules(){
        return mySchedules;
    }

    public void addSchedule(Schedule sch){
        mySchedules.add(sch);
    }


}
