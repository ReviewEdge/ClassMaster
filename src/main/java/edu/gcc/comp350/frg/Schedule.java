package edu.gcc.comp350.frg;

import java.util.ArrayList;

public class Schedule {

    private String name;
    private int id;
    private Term term;
    private ArrayList<Class> classes;

    public Schedule(String name, int id, Term term) {
        this.name = name;
        this.id = id;
        this.term = term;
        this.classes = new ArrayList<>();
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

    }
    public void removeClass (Class toRemove) throws Exception{

    }

    public ArrayList<Integer> getRefNums(){
        return null;
    }

    public ArrayList<String> getClassNames(){
        return null;
    }

}

