package edu.gcc.comp350.frg;

import java.util.ArrayList;
import java.util.Calendar;

public class API {

    //The brain of the operation, the central container for all the data/methods
    private Main main;
    //Current filter to apply to searches
    // TODO Move to main (Shouldn't really be in API)
    private Filter filter;

    public API(Main main){
        this.main = main;
        this.filter = new Filter();
    }

    /**
     *
     * @return the list of schedules in this account
     */
    public ArrayList<Schedule> getSchedules(){
        return main.getAccount().getSchedules();
    }

    /**
     *  Sets the current schedule equal to the schedule at index i
     *
     * @param i the index of the schedule to load
     * @throws Exception if there is no schedule at the index given
     */
    public void loadSchedule(int i) throws Exception{
        if(getNumSchedules() < i || i < 1){
            throw new Exception("No schedule at index " + i + ", Please try again");
        }
        Schedule sch = main.getAccount().getSchedules().get(i-1);
        main.changeCurrentSchedule(sch);
        filter.setTerm(main.getCurrentSchedule().getTerm());
    }

    /**
     * Creates a schedule and adds it to the account
     *
     * @param name The name for the new schedule
     * @param semester the semester the new schedule will be for
     * @return the schedule created
     * @throws Exception if either the parameters were incorrect (ie. the semester was not one)
     */
    public Schedule createSchedule(String name, String semester) throws Exception {
        try {
            Term t = new Term(semester);
            Schedule sch = new Schedule(name, t, new ArrayList<>());
            main.getAccount().addSchedule(sch);
//            sch.saveSchedule();
            return sch;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     *
      * @return the number of schedules in the account
     */
    public int getNumSchedules(){
        return main.getAccount().getSchedules().size();
    }

    /**
     *
     * @return the current schedule loaded
     */
    public Schedule getCurrentSchedule(){
        return main.getCurrentSchedule();
    }

    /**
     *  Gets the class at position i as a string
     *
     * @param i the position in the class
     * @return the toString of the class
     */
    public String getClassInfo(int i){
        return main.getCurrentSchedule().getClasses().get(i).toString();
    }

    /**
     * Adds the class at position i from the latest search to the current schedule
     *
     * @param i position of the class to be added
     * @throws Exception if there is no class at the position
     */
    public void addClass(int i) throws Exception{
//        System.out.println("**********************************");
//        System.out.println(main.getCurrentSearch().getCurrentResults().get(i));
        if(i > main.getCurrentSearch().getCurrentResults().size()){
            throw new Exception("Index not available, please try again");
        }
        Class c = main.getCurrentSearch().getClass(i);
        main.getCurrentSchedule().addClass(c);
//        main.getCurrentSchedule().saveSchedule();
    }

    /**
     *  Removes the class at position i from the current schedule
     *
     * @param i position of the class in the schedule
     * @throws Exception if the position is not valid
     */
    public void removeClass(int i) throws Exception {
        main.getCurrentSchedule().removeClass(i);
//        main.getCurrentSchedule().saveSchedule();
    }

    /**
     * Creates a new search object with the current filter and search query
     * and sets it as the current search.
     *
     * @param query the string to search by
     * @throws Exception
     */
    public void makeSearch(String query) throws Exception{
        Search s = new Search(query, filter); // Should validate that it Can make a search from that? //TODO
        main.makeNewSearch(s);
    }

    /**
     *
     * @return if there is a current search selected
     */
    public boolean hasCurrentSearch(){
        return main.getCurrentSearch() != null;
    }

    /**
     *  Changes the currentSchedule's name to the newName
     *
     * @param newName
     */
    public void renameCurrentSchedule(String newName){
        getCurrentSchedule().setName(newName);
//        main.getCurrentSchedule().saveSchedule();
    }

    /**
     *
     * @return the results from the latest ("Current") search
     */
    public ArrayList<Class> getSearchResults(){
        return main.getCurrentSearch().getCurrentResults();
    }

    /**
     * Adds a filter of string str of a given type to filter.
     * See addTimeslotFilter to add a timeslot
     *
     * @param type the type of filter to set
     *             1: Name Contains
     *             2: Course Code Contains
     *             4: Professor
     * @param str
     */
    public void addFilter(int type, String str){
        if(type == 1){
            filter.setContains(str);
        }
        else if (type == 2) {
            filter.setCode(str);
        }
        else if (type == 4){
            filter.setProfessor(str);
        }
    }

    /**
     * Removes all of a type of filter
     *
     * @param type the type of filter to remove
     *             1: Name Contains
     *             2: Course Code Contains
     *             3: Time slots
     *             4: Professor
     */
    public void removeFilter(int type){
        if(type == 1){
            filter.removeContains();
        }
        else if (type == 2) {
            filter.removeCode();
        }
        else if (type == 3) {
            filter.removeAllTimeslots();
        }
        else if (type == 4){
            filter.removeProfessor();
        }
        else if (type == 5){
            filter.removeMinCredits();
            filter.removeMaxCredits();
        }
    }

    /**
     * Adds a timeslot filter.
     * See addFilter to add a differnt type of filter
     *
     * @param day the day it applies to, options are: M,T,W,R,F
     * @param start the start time in 24hr time
     * @param end the end time in 24hr time
     * @throws Exception if day is not valid
     */
    public void addTimeslotFilter(String day, String start, String end) throws Exception{
        //There HAS to be a better way to do this
        Day d = Day.NONE;
        if(day.equals("M")){
            d = Day.Monday;
        }
        else if(day.equals("T")){
            d = Day.Tuesday;
        }
        else if(day.equals("W")){
            d = Day.Wednesday;
        }
        else if(day.equals("R")){
            d = Day.Thursday;
        }
        else if(day.equals("F")){
            d = Day.Friday;
        }
        else{
            throw new Exception("Not a day");
        }
        filter.addTimeslot(new Timeslot(start + ":00", end + ":00", d));
    }

    /**
     * Clears all types from a filter
     */
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

    /**
     * Loads the schedules saved in the database for a given account
     */
    public void loadSavedSchedules(){
        System.out.println("WRITE THE LOAD METHOD");
        ArrayList<Integer> scheduleIDs = Schedule.getAllScheduleIDsFromDB();
        for(int i: scheduleIDs){
            main.getAccount().addSchedule(Schedule.getScheduleByIDFromDB(i));
        }
    }

    /**
     * Handles quiting/closing things in the api
     */

    public void quit(boolean testing){
        //TODO Make sure that everything closes nicely?... KEEP Checking
        if(!testing) {
            ArrayList<Schedule> schedules = main.getAccount().getSchedules();
            for (Schedule sch : schedules) {
                sch.saveAndReplaceInDatabase();
//                sch.saveSchedule();
            }
        }
    }

















}
