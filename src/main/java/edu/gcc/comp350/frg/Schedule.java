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
        for (Class c: classes){
            if (c.getTime().overlaps(toAdd.getTime())){
                Class newClass = ResolveConflict(c,toAdd);
                classes.add(newClass);
                return;
            }
        }
        classes.add(toAdd);
    }
    public void removeClass (int i) throws Exception{

    }

    public void removeClass (Class toRemove) throws Exception{
        for (){
            if (c =)
        }
    }

    private Class ResolveConflict(Class preexist, Class newexist){
        //nah we're not gonna let you add that class. this will be changed later
        return preexist;
    }

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
            classNames.set(i, c.getName());
            i++;
        }
        return classNames;
    }

}

