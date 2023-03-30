package edu.gcc.comp350.frg;

import java.util.ArrayList;
import java.util.Calendar;

public class API {

    private Main main;
    private Filter filter;

    public API(Main main){
        this.main = main;
        this.filter = new Filter();
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
        Schedule sch = new Schedule(name, t, new ArrayList<>());
//        Schedule sch = new Schedule(name, scheduleId, t);
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
//        System.out.println("**********************************");
//        System.out.println(main.getCurrentSearch().getCurrentResults().get(i));
        Class c = main.getCurrentSearch().getClass(i);
        main.getCurrentSchedule().addClass(c);
    }

    public void removeClass(int i) throws Exception {
        main.getCurrentSchedule().removeClass(i);
    }

    public void makeSearch(String search) throws Exception{
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

    public void addFilter(int type, String filter){

    }

    public void addTimeslotFilter(String day, String start, String end) throws Exception{

    }

    public void clearFilters(){
        filter.removeCode();
        filter.removeContains();
        filter.removeAllTimeslots();
        filter.removeDepartment();
        filter.removeProfessor();
        filter.removeTerm();
        filter.removeMaxCredits();
        filter.removeMinCredits();
    }

    public void loadSavedSchedules(){
        System.out.println("WRITE THE LOAD METHOD");
    }

    public void quit(){
        //TODO Make sure that everything closes nicely?
    }

















}
