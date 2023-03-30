package edu.gcc.comp350.frg;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Filter {
    // characters that must be in the class name somewhere
    private String contains = null;
    // course code
    private String code = null;
    // the timeslots the class can be in
    private ArrayList<TreeSet<Timeslot>> timeslots = null;
    // the professor teaching this class
    private String professor = null;
    // the department this class is in
    private String department = null;
    // determines number of credits wanted, if searching for a specific amount set min and max to same
    private int minCredits = -1;
    private int maxCredits = -1;

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public void removeTerm(){
        term = null;
    }

    private Term term = null;

    /**
     * creates a filter with the parameters (probably not going to be used)
     * @param contains if the course name contains this string, set to null if unused
     * @param code the course code, set to null if unused
     * @param timeslots an arraylist of days of timeslots
     * @param professor the professor's name (case insensitive), set to null if unused
     * @param department the department the class is offered by, set to Department.NONE if unused
     * @param minCredits the minimum number of credits the class takes, set to -1 if unused
     * @param maxCredits the maximum number of credits the class takes, set to -1 if unused
     */
    public Filter(String contains, String code, ArrayList<TreeSet<Timeslot>> timeslots, String professor, String department, int minCredits, int maxCredits) {
        this.contains = contains;
        this.code = code;
        this.timeslots = timeslots;
        this.professor = professor;
        this.department = department;
        this.minCredits = minCredits;
        this.maxCredits = maxCredits;
    }

    /**
     * creates an empty filter
     */
    public Filter(){
    }

    public void setContains(String contains) {
        this.contains = contains;
    }
    public void removeContains() {
        this.contains = null;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void removeCode() {
        this.code = null;
    }

    public void setTimeslots(ArrayList<TreeSet<Timeslot>> timeslots) {
        if(timeslots.size() != 7){
            throw new IllegalArgumentException();
        }
        this.timeslots = timeslots;
    }

    /**
     * adds a timeslot to the possible timeslots to choose from
     * @param timeslot the timeslot to add
     * @return true if the timeslot wasn't already added
     */
    public void addTimeslot(Timeslot timeslot) {
        if(this.timeslots == null){
            this.timeslots = new ArrayList<>();
            for(int i=0; i<7; i++){
                this.timeslots.add(new TreeSet<Timeslot>());
            }
        }
        /**
         * the goal of the upcoming part is this:
         * if you insert this:
         *          |       timeslot        |
         * into this:
         * |  ts    |   |  ts   |       |  ts   |
         * you should get this for ease of filtering:
         * |              timeslot              |
         * to do this, I check the slot strictly before the input to see if it overlaps
         *      if so, use its start as the start of the combined timeslot, and remove it
         *      otherwise just set the start as the input's start
         * then check all timeslots that start after the input
         *      if they overlap, add them to the merged slot and remove them from the day's slots
         *          if they have a later end than the input, use that as the end of the new timeslot
         * at the end I have a timeslot with the correct start and end
         * and have removed all superfluous timeslots that would be inside of it
         */
        // get all timeslots this day
        TreeSet<Timeslot> day = this.timeslots.get(timeslot.getDay().ordinal());
        // find all timeslots that start later
        Timeslot[] mayMerge = day.tailSet(timeslot).toArray(new Timeslot[0]);
        // set up some variables for the end result
        Time start = timeslot.getStart();
        Time end = timeslot.getEnd();
        // find the timeslot that starts before
        Timeslot temp = day.lower(timeslot);
        // if it overlaps with the new timeslot, merge it in
        if (temp != null && timeslot.overlaps(temp, true)) {
            start = temp.getStart();
            day.remove(temp);
        }
        // if there are any timeslots starting later...
        if(mayMerge.length > 0) {
            for(int i=0; i<mayMerge.length; i++){
                temp = mayMerge[i];
                // check if this slot also needs to be absorbed
                if(timeslot.overlaps(temp, true)){
                    day.remove(temp);
                    // if this one ends after the input's end, then that will be the end of the new slot
                    if(end.before(temp.getEnd())){
                        end = temp.getEnd();
                        day.remove(temp);
                        break;
                    }
                } else {
                    //doesn't overlap anymore, end
                    break;
                }
            }
        }else {
            end = timeslot.getEnd();
        }
        day.add(new Timeslot(start, end, timeslot.getDay()));
    }
    public void removeAllTimeslots() {
        timeslots = null;
    }
    /**
     * removes a timeslot from the possible ones
     * @param timeslot the timeslot to remove
     * @return true if the timeslot was in the day's set
     */
    public void removeTimeslot(Timeslot timeslot) {
        TreeSet<Timeslot> day = this.timeslots.get(timeslot.getDay().ordinal());
        if (day.remove(timeslot)){
            return;
        }
        /**
         * the goal of the upcoming part is this:
         * what if you remove this:
         *          |       timeslot   |
         * from this:
         * |  ts        |       |          ts   |
         * or this:
         * |               timeslot             |
         * you should get this:
         * |  ts    |                  |   ts   |
         * to do this, I check the slot strictly before the input to see if the input is in it
         *    - if so, split it up into 2 chunks without the middle area
         *    - otherwise, check if it overlaps
         *          if so, set its end time to be equal to the removed slot's start
         * then check all timeslots that start after the input for being contained in the removed time
         *    - if so, remove them from the day's slots
         *    - otherwise, check to see if they overlap
         *        - if so, set their start to be the end of the removed time
         * at the end I have removed all time that ovelaps with the input
         */
        // find all timeslots that start later
        Timeslot[] mayRem = day.tailSet(timeslot).toArray(new Timeslot[0]);
        // find the timeslot that starts before
        Timeslot temp = day.lower(timeslot);
        // if the input timeslot is contained within it, split the timeslot before
        if (temp != null && timeslot.isIn(temp)) {
            day.add(new Timeslot(timeslot.getEnd(), temp.getEnd(), timeslot.getDay()));
            temp.setEnd(timeslot.getStart());
            return;
        }else if (temp != null && timeslot.overlaps(temp)){
            // it starts before and overlaps, therefore the end time must be the overlapped part
            temp.setEnd(timeslot.getStart());
        }
        // if there are any timeslots starting later...
        if(mayRem.length > 0) {
            for(int i=0; i<mayRem.length; i++){
                temp = mayRem[i];
                // check if this slot needs to be removed entirely
                if(temp.isIn(timeslot)){
                    day.remove(temp);
                }
                //otherwise, if it needs to be removed partially
                else if (temp.overlaps(timeslot)) {
                    temp.setStart(timeslot.getEnd());
                    break;
                }
                //doesn't overlap anymore, end
                else {
                    break;
                }
            }
        }
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
    public void removeProfessor() {
        this.professor = null;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * basically the same as setDepartment(Department.NONE)
     */
    public void removeDepartment() {
        this.department = null;
    }

    public void setMinCredits(int minCredits) {
        this.minCredits = minCredits;
    }
    public void removeMinCredits() {
        this.minCredits = -1;
    }

    public void setMaxCredits(int maxCredits) {
        this.maxCredits = maxCredits;
    }
    public void removeMaxCredits() {
        this.maxCredits = -1;
    }

    public String getContains() {
        return contains;
    }

    public String getCode() {
        return code;
    }

    public ArrayList<TreeSet<Timeslot>> getTimeslots() {
        return timeslots;
    }

    public String getProfessor() {
        return professor;
    }

    public String getDepartment() {
        return department;
    }

    public int getMinCredits() {
        return minCredits;
    }

    public int getMaxCredits() {
        return maxCredits;
    }
    public boolean isValid(Class test){
        if(term != null && !term.equals(test.getTerm())) return false;
        if(contains != null){
            //if the contains string isn't in the class anywhere, it doesn't match
            if(!test.getTitle().toLowerCase().contains(contains.toLowerCase())) return false;
        }
        if(code != null){
            if(!test.getCode().toLowerCase().equals(code.toLowerCase())) return false;
        }
        if(timeslots != null) {
            ArrayList<Timeslot> ts = test.getTimeSlots();
            for (Timeslot t : ts){
                // the day's timeslots
                TreeSet<Timeslot> day = timeslots.get(t.getDay().ordinal());
                // if no limitations have been put on this day, don't check
                if(day.size() == 0) continue;
                // all timeslots before, including ones that start at the same time as t
                NavigableSet<Timeslot> b = day.headSet(t, true);
                if (b.size() == 0) return false;// there are no valid timeslots on that day
                // the timeslot that starts just before the class' one
                Timeslot before = b.last();
                // check if this time fits within the allotted time
                if (!t.isIn(before)) return false;
            }
        }
        if (professor != null){
            if(!test.getProfessor().toLowerCase().contains(professor.toLowerCase())) return false;
        }
        if (department != null){
            if(!test.getDepartment().equals(department)) return false;
        }
        if (minCredits != -1){
            if(test.getCredits() < minCredits) return false;
        }
        if (maxCredits != -1){
            if(test.getCredits() > maxCredits) return false;
        }
        return true;
    }
}
