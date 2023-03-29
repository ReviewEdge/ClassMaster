package edu.gcc.comp350.frg;

import java.sql.Time;

/*
 * TOMMY: using java.sql.time, have day be an enum
 * use Time.valueOf("HH:MM:SS") to get the start and end for each timeslot
 * keep timeslots occupied already into a sorted list with no conflicts sorted by start time
 *      (probably doubly linked list for easy insert and access previous/after)
 * proof by induction that can add timeslot (and procedure of how to do it):
 * base case of empty list can have one added with method (no slots to conflict)
 * when trying to add a timeslot, first find where it would be put in the list
 * then check if the timeslot before it has an ending after this timeslot's beginning (if so, conflict)
 * then check if the timeslot after it has a beginning before this timeslot's ending (if so, conflict)
 * if neither of the slots have a conflict,
 * then all previous timeslots won't overlap with the new one (since that would require overlapping previous)
 * and all later timeslots won't overlap with the new one (see above)
 * therefore the list will still have no overlaps by induction
 */

/**
 * represents one period of time covered by a class
 */
public class Timeslot implements Comparable<Timeslot>{
    /**
     * should Timeslot have a reference to its class?
     * I feel like that would be useful for many reasons
     */
    private Time start;
    private Time end;
    private Day day;

    /**
     * @param o the timeslot to be compared, must be on the same day
     * @return the comparison between start times: less than 0 if earlier start, 0 if equal, and greater if later
     */
    @Override
    public int compareTo(Timeslot o) {
        if(this.day != o.day) throw new IllegalArgumentException();
        return this.start.compareTo(o.start);
    }
    /**
     * @param start the start time
     * @param end the end time
     * @param day the day of the week
     */
    public Timeslot(Time start, Time end, Day day) {
        this.start = start;
        this.end = end;
        this.day = day;
    }

    /**
     * @param start the start time in a 24h HH:MM:SS formatted string
     * @param end the end time in a 24h HH:MM:SS formatted string
     * @param day the day of the week
     */
    public Timeslot(String start, String end, Day day) {
        this.start = Time.valueOf(start);
        this.end = Time.valueOf(end);
        this.day = day;
    }

    @Override
    public String toString(){
        String startTime = start.toString().substring(0, 5);//HH:MM
        if(startTime.charAt(0) == '0') startTime = startTime.substring(1);//remove leading 0

        String endTime = end.toString().substring(0, 5);//HH:MM
        if(endTime.charAt(0) == '0') endTime = endTime.substring(1);//remove leading 0

        return day.name()+" "+startTime+" - "+endTime+".";
    }

    public Time getStart() {
        return start;
    }

    public void setStart(Time start) {
        this.start = start;
    }

    /**
     * @param start the start time in a 24h HH:MM:SS formatted string
     */
    public void setStart(String start) {
        this.start = Time.valueOf(start);
    }

    public Time getEnd() {
        return end;
    }

    public void setEnd(Time end) {
        this.end = end;
    }

    /**
     * @param end the end time in a 24h HH:MM:SS formatted string
     */
    public void setEnd(String end) {
        this.end = Time.valueOf(end);
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    /**
     * @param other the other timeslot to compare to
     * @param equalOverlaps whether an equal start:end counts as an overlap, defaults to false
     * @return true if they overlap, false otherwise
     */
    public boolean overlaps(Timeslot other, boolean equalOverlaps){
        if(this.day != other.day) return false;
        int comp = this.compareTo(other);
        if(comp < 0){// this starts before the other starts, check if this ends in time for the other
            if(end.before(other.start) || (end.equals(other.start) && !equalOverlaps)) return false; // this ends before the other timeslot starts, it can't overlap
            else return true; // this ends after the other starts, it overlaps
        }
        if(comp > 0){// this starts after the other starts, check if the other ends in time for this
            if(other.end.before(start) || (other.end.equals(start) && !equalOverlaps)) return false; // this starts after the other timeslot finishes, it doesn't overlap
            else return true;
        }
        return true;
    }
    /**
     * NOTE: this does NOT treat slots with equal start:end as overlapping
     * (after all, maybe they are in the same room, like, say, a scrum meeting after class)
     * @param other the other timeslot to compare to
     * @return true if they overlap, false otherwise
     */
    public boolean overlaps(Timeslot other){
        return overlaps(other, false);
    }
    /**
     * used for filtering by time, checks if the timeslot can fit inside another
     * (allows equal starting and ending times)
     * @param other the other timeslot to compare to
     * @return true if this time is contained in the other, false otherwise
     */
    public boolean isIn(Timeslot other){
        if(this.day != other.day) return false;
        if(start.after(other.start) || start.equals(other.start)) {
            if (end.before(other.end) || end.equals(other.end)) return true;
        }
        return false;
    }
    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if (!(other instanceof Timeslot))return false;
        Timeslot o = (Timeslot) other;
        if(start.equals(o.start) && end.equals(o.end) && day == o.day) return true;
        return false;
    }
}
