package edu.gcc.comp350.frg;

import groovyjarjarantlr4.runtime.tree.Tree;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class FilterTest {

    @Test
    void addTimeslot() {
        Timeslot testTimeslot = new Timeslot("08:00:00", "10:30:00", Day.Friday);
        Class testClass = new Class("code", "name", 1, testTimeslot, new Term(1, ""), "professor", Department.COMP.name(), 3, "location", "parallel lines");
        Filter f = new Filter();
        f.addTimeslot(new Timeslot("08:00:00", "09:00:00", Day.Friday));
        f.addTimeslot(new Timeslot("09:30:00", "09:45:00", Day.Friday));
        f.addTimeslot(new Timeslot("10:00:00", "11:00:00", Day.Friday));
        f.addTimeslot(new Timeslot("09:00:00", "10:30:00", Day.Friday));
        assertTrue(f.isValid(testClass));
        //test code, no longer needed
//        Timeslot[] arr = f.getTimeslots().get(Day.Friday.ordinal()).toArray(new Timeslot[0]);
//        for(int i=0; i<arr.length; i++){
//            System.out.println(arr[i]);
//        }
    }

    @Test
    void removeTimeslot() {
        Timeslot testTimeslot = new Timeslot("10:00:00", "10:30:00", Day.Friday);
        Class testClass = new Class("code", "name",  1, testTimeslot, new Term(1, ""), "professor", Department.COMP.name(), 3, "location", "parallel lines");
        Filter f = new Filter();
        f.addTimeslot(new Timeslot("08:00:00", "09:00:00", Day.Friday));
        f.addTimeslot(new Timeslot("09:30:00", "09:45:00", Day.Friday));
        f.addTimeslot(new Timeslot("10:00:00", "11:00:00", Day.Friday));
        assertTrue(f.isValid(testClass));
        f.removeTimeslot(new Timeslot("08:45:00", "10:30:00", Day.Friday));
        assertFalse(f.isValid(testClass));
        //test code, no longer needed
        Timeslot[] arr = f.getTimeslots().get(Day.Friday.ordinal()).toArray(new Timeslot[0]);
        for(int i=0; i<arr.length; i++){
            System.out.println(arr[i]);
        }
        System.out.println("end test 1");
        f.addTimeslot(new Timeslot("08:00:00", "11:00:00", Day.Friday));
        assertTrue(f.isValid(testClass));
        f.removeTimeslot(new Timeslot("08:45:00", "10:30:00", Day.Friday));
        assertFalse(f.isValid(testClass));
        //test code, no longer needed
        arr = f.getTimeslots().get(Day.Friday.ordinal()).toArray(new Timeslot[0]);
        for(int i=0; i<arr.length; i++){
            System.out.println(arr[i]);
        }
    }

    @Test
    void isValid() {
        ArrayList<TreeSet<Timeslot>> timeslots = new ArrayList<>();
        for(int i=0; i<7; i++) {
            timeslots.add(new TreeSet<Timeslot>());
        }
        String professor = "Dr. Hutchins";
        String code = "COMP 123";
        Department department = Department.COMP;
        int minCred = 2;
        int maxCred = 5;
        Term term = new Term(1, "Spring 2023");
        String contains = "parallel";
        Timeslot t = new Timeslot("09:00:00", "10:00:00", Day.Friday);
        timeslots.get(Day.Friday.ordinal()).add(t);
        int ref = 1;
        Class[] classes = {
                //0, succeeds all
                new Class(code, "name", ref, t, term, professor, department.name(), 3, "location", "parallel lines"),
                //1, fails professor
                new Class(code, "name", ref, t, term, "aaaaaa", department.name(), 3, "location", "parallel lines"),
                //2, fails timeslot
                new Class(code, "name", ref, new Timeslot("09:00:00", "10:00:00", Day.Saturday), term, professor, department.name(), 3, "location", "parallel lines"),
                //3, fails code
                new Class("COMP 456", "name", ref, t, term, professor, department.name(), 3, "location", "parallel processing"),
                //4, fails department
                new Class(code, "name", ref, t, term, professor, Department.HUMA.name(), 3, "location", "parallel planes"),
                //5, fails minimum credits
                new Class("parallel", code, ref, t, term, professor, department.name(), 1, "location", "check title"),
                //6, fails maximum credits
                new Class(code, "name", ref, t, term, professor, department.name(), 10, "location", "just parallel"),
                //7, fails term
                new Class(code, "name", ref, t, new Term(2, "NO"), professor, department.name(), 3, "France", "french parallel"),
                //8, fails contains
                new Class(code, "name", ref, t, term, professor, department.name(), 3, "location", "")
        };
        Filter f = new Filter();
        f.setContains(contains);
        assertFalse(f.isValid(classes[8]));
        assertTrue(f.isValid(classes[0]));
        f.removeContains();
        f.setTerm(term);
        assertFalse(f.isValid(classes[7]));
        assertTrue(f.isValid(classes[0]));
        f.removeTerm();
        f.setMaxCredits(maxCred);
        assertFalse(f.isValid(classes[6]));
        assertTrue(f.isValid(classes[0]));
        f.removeMaxCredits();
        f.setMinCredits(minCred);
        assertFalse(f.isValid(classes[5]));
        assertTrue(f.isValid(classes[0]));
        f.removeMinCredits();
        f.setDepartment(department.name());
        assertFalse(f.isValid(classes[4]));
        assertTrue(f.isValid(classes[0]));
        f.removeDepartment();
        f.setCode(code);
        assertFalse(f.isValid(classes[3]));
        assertTrue(f.isValid(classes[0]));
        f.removeCode();
        f.setTimeslots(timeslots);
        assertFalse(f.isValid(classes[2]));
        assertTrue(f.isValid(classes[0]));
        f.removeAllTimeslots();
        f.setProfessor(professor);
        assertFalse(f.isValid(classes[1]));
        assertTrue(f.isValid(classes[0]));
        f.removeProfessor();
    }
}