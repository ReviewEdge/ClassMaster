package edu.gcc.comp350.frg;

import java.util.ArrayList;

public class Schedule {

    private String name;
    private int id;
    private Term term;
    private ArrayList<Class> classes;
    private int currentcredits;

    public Schedule(String name, int id, Term term) {
        this.name = name;
        this.id = id;
        this.term = term;
        this.classes = new ArrayList<>();
        currentcredits = 0;
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
        for (Class c: classes){
            scheduleString.append(c.toString()).append("\n");
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
        if (currentcredits + toAdd.getCredits() >= 21){
            throw new Exception("too many credits!");
        }
        for (Class c: classes){
            if (c.getTime().overlaps(toAdd.getTime())){
                Class newClass = ResolveConflict(c,toAdd);
                classes.remove(c);
                classes.add(newClass);
                return;
            }
            currentcredits = currentcredits + toAdd.getCredits();
            classes.add(toAdd);
        }
        classes.add(toAdd);
    }
    public void removeClass (int i) throws Exception{
        Class removedClass = classes.get(i);
        currentcredits = currentcredits - removedClass.getCredits();
        classes.remove(i);

    }

    public void removeClass (Class toRemove) throws Exception{
        currentcredits = currentcredits - toRemove.getCredits();
       classes.remove(toRemove);
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
            classNames.set(i, c.getTitle());
            i++;
        }
        return classNames;
    }

}

