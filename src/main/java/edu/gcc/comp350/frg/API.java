package edu.gcc.comp350.frg;

import java.util.ArrayList;
import java.util.Calendar;

public class API {

    private Main main;
    private Filter filter;

    public API(Main main){
        this.main = main;
    }

    public ArrayList<Schedule> getSchedules(){
        return main.getAccount().getSchedules();
    }

    public void loadSchedule(int i) throws Exception{
        if(getNumSchedules() < i || i < 1){
            throw new Exception("No schedule at index " + i + ", Please try again");
        }
        Schedule sch = main.getAccount().getSchedules().get(i-1);
        main.changeCurrentSchedule(sch);
    }

    public Schedule createSchedule(String name, String semester) throws Exception{
        Term t = new Term(0, semester); //TODO Handle Term Creation; Throws exception if invalid input, Make Term have the correct ID
        int scheduleId = 0; //TODO Make Schedule ID's unique

        Schedule sch = new Schedule(name, scheduleId, t);
        main.getAccount().addSchedule(sch);
        return sch;
    }

    public int getNumSchedules(){
        return main.getAccount().getSchedules().size();
    }

    public Schedule getCurrentSchedule(){
        return main.getCurrentSchedule();
    }

    public String getClassInfo(int i){
        return main.getCurrentSchedule().getClasses().get(i).toString();
    }

    public void addClass(int i) throws Exception{
        Class c = main.getCurrentSearch().getClass(i);
        main.getCurrentSchedule().addClass(c);
    }

    public void removeClass(int i) throws Exception {
        main.getCurrentSchedule().removeClass(i);
    }

    public void makeSearch(String search) throws Exception{
        Filter filter = new Filter();
        filter.setCode(search);  // assumes search is for a class code
        Search s = new Search(search, filter); // Should validate that it Can make a search from that? //TODO
        main.makeNewSearch(s);
    }

    public boolean hasCurrentSearch(){
        return main.getCurrentSearch() != null;
    }

    public void renameCurrentSchedule(String name){
        getCurrentSchedule().setName(name);
    }

    public ArrayList<Class> getSearchResults(){
        return main.getCurrentSearch().getCurrentResults();
    }

    public void quit(){
        //TODO Make sure that everything closes nicely?
    }

















}
