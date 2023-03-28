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
        Class testClass = new Class("name", "code", "timeslotstandin", new Term(1, ""), "professor", /*replace when class works*/"departmentstandin", 3, "location", "parallel lines");
        Filter f = new Filter();
        f.addTimeslot(new Timeslot("08:00:00", "09:00:00", Day.Friday));
        f.addTimeslot(new Timeslot("09:30:00", "09:45:00", Day.Friday));
        f.addTimeslot(new Timeslot("10:00:00", "11:00:00", Day.Friday));
        f.addTimeslot(new Timeslot("09:00:00", "10:30:00", Day.Friday));
        assertTrue(f.isValid(testClass, testTimeslot, Department.COMP));
        //test code, no longer needed
//        Timeslot[] arr = f.getTimeslots().get(Day.Friday.ordinal()).toArray(new Timeslot[0]);
//        for(int i=0; i<arr.length; i++){
//            System.out.println(arr[i]);
//        }
    }

    @Test
    void removeTimeslot() {
        Timeslot testTimeslot = new Timeslot("10:00:00", "10:30:00", Day.Friday);
        Class testClass = new Class("name", "code", "timeslotstandin", new Term(1, ""), "professor", /*replace when class works*/"departmentstandin", 3, "location", "parallel lines");
        Filter f = new Filter();
        f.addTimeslot(new Timeslot("08:00:00", "09:00:00", Day.Friday));
        f.addTimeslot(new Timeslot("09:30:00", "09:45:00", Day.Friday));
        f.addTimeslot(new Timeslot("10:00:00", "11:00:00", Day.Friday));
        assertTrue(f.isValid(testClass, testTimeslot, Department.COMP));
        f.removeTimeslot(new Timeslot("08:45:00", "10:30:00", Day.Friday));
        assertFalse(f.isValid(testClass, testTimeslot, Department.COMP));
        //test code, no longer needed
        Timeslot[] arr = f.getTimeslots().get(Day.Friday.ordinal()).toArray(new Timeslot[0]);
        for(int i=0; i<arr.length; i++){
            System.out.println(arr[i]);
        }
        System.out.println("end test 1");
        f.addTimeslot(new Timeslot("08:00:00", "11:00:00", Day.Friday));
        assertTrue(f.isValid(testClass, testTimeslot, Department.COMP));
        f.removeTimeslot(new Timeslot("08:45:00", "10:30:00", Day.Friday));
        assertFalse(f.isValid(testClass, testTimeslot, Department.COMP));
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
        Class[] classes = {
                //0, succeeds all
                new Class("name", code, "timeslotstandin", term, professor, /*replace when class works*/"departmentstandin", 3, "location", "parallel lines"),
                //1, fails professor
                new Class("name", code, "timeslotstandin", term, "aaaaaa", /*replace when class works*/"departmentstandin", 3, "location", "parallel lines"),
                //2, fails timeslot (TODO: make fail)
                new Class("name", code, "timeslotstandin", term, professor, /*replace when class works*/"departmentstandin", 3, "location", "parallel lines"),
                //3, fails code
                new Class("name", "COMP 456", "timeslotstandin", term, professor, /*replace when class works*/"departmentstandin", 3, "location", "parallel processing"),
                //4, fails department (TODO: make fail)
                new Class("name", code, "timeslotstandin", term, professor, /*replace when class works*/"departmentstandin", 3, "location", "parallel planes"),
                //5, fails minimum credits
                new Class("parallel", code, "timeslotstandin", term, professor, /*replace when class works*/"departmentstandin", 1, "location", "check title"),
                //6, fails maximum credits
                new Class("name", code, "timeslotstandin", term, professor, /*replace when class works*/"departmentstandin", 10, "location", "just parallel"),
                //7, fails term
                new Class("name", code, "timeslotstandin", new Term(2, "NO"), professor, /*replace when class works*/"departmentstandin", 3, "France", "french parallel"),
                //8, fails contains
                new Class("name", code, "timeslotstandin", term, professor, /*replace when class works*/"departmentstandin", 3, "location", "")
        };
        Filter f = new Filter();
        f.setContains(contains);
        assertFalse(f.isValid(classes[8], t, department));
        assertTrue(f.isValid(classes[7], t, department));
        assertTrue(f.isValid(classes[6], t, department));
        assertTrue(f.isValid(classes[5], t, department));
        assertTrue(f.isValid(classes[4], t, Department.HUMA));
        assertTrue(f.isValid(classes[3], t, department));
        assertTrue(f.isValid(classes[2], t, department));
        assertTrue(f.isValid(classes[1], t, department));
        assertTrue(f.isValid(classes[0], t, department));
        f.setTerm(term);
        assertFalse(f.isValid(classes[8], t, department));
        assertFalse(f.isValid(classes[7], t, department));
        assertTrue(f.isValid(classes[6], t, department));
        assertTrue(f.isValid(classes[5], t, department));
        assertTrue(f.isValid(classes[4], t, Department.HUMA));
        assertTrue(f.isValid(classes[3], t, department));
        assertTrue(f.isValid(classes[2], t, department));
        assertTrue(f.isValid(classes[1], t, department));
        assertTrue(f.isValid(classes[0], t, department));
        f.setMaxCredits(maxCred);
        assertFalse(f.isValid(classes[8], t, department));
        assertFalse(f.isValid(classes[7], t, department));
        assertFalse(f.isValid(classes[6], t, department));
        assertTrue(f.isValid(classes[5], t, department));
        assertTrue(f.isValid(classes[4], t, Department.HUMA));
        assertTrue(f.isValid(classes[3], t, department));
        assertTrue(f.isValid(classes[2], t, department));
        assertTrue(f.isValid(classes[1], t, department));
        assertTrue(f.isValid(classes[0], t, department));
        f.setMinCredits(minCred);
        assertFalse(f.isValid(classes[8], t, department));
        assertFalse(f.isValid(classes[7], t, department));
        assertFalse(f.isValid(classes[6], t, department));
        assertFalse(f.isValid(classes[5], t, department));
        assertTrue(f.isValid(classes[4], t, Department.HUMA));
        assertTrue(f.isValid(classes[3], t, department));
        assertTrue(f.isValid(classes[2], t, department));
        assertTrue(f.isValid(classes[1], t, department));
        assertTrue(f.isValid(classes[0], t, department));
        f.setMinCredits(minCred);
        assertFalse(f.isValid(classes[8], t, department));
        assertFalse(f.isValid(classes[7], t, department));
        assertFalse(f.isValid(classes[6], t, department));
        assertFalse(f.isValid(classes[5], t, department));
        assertTrue(f.isValid(classes[4], t, Department.HUMA));
        assertTrue(f.isValid(classes[3], t, department));
        assertTrue(f.isValid(classes[2], t, department));
        assertTrue(f.isValid(classes[1], t, department));
        assertTrue(f.isValid(classes[0], t, department));
        f.setDepartment(department);
        assertFalse(f.isValid(classes[8], t, department));
        assertFalse(f.isValid(classes[7], t, department));
        assertFalse(f.isValid(classes[6], t, department));
        assertFalse(f.isValid(classes[5], t, department));
        assertFalse(f.isValid(classes[4], t, Department.HUMA));
        assertTrue(f.isValid(classes[3], t, department));
        assertTrue(f.isValid(classes[2], t, department));
        assertTrue(f.isValid(classes[1], t, department));
        assertTrue(f.isValid(classes[0], t, department));
        f.setCode(code);
        assertFalse(f.isValid(classes[8], t, department));
        assertFalse(f.isValid(classes[7], t, department));
        assertFalse(f.isValid(classes[6], t, department));
        assertFalse(f.isValid(classes[5], t, department));
        assertFalse(f.isValid(classes[4], t, Department.HUMA));
        assertFalse(f.isValid(classes[3], t, department));
        assertTrue(f.isValid(classes[2], t, department));
        assertTrue(f.isValid(classes[1], t, department));
        assertTrue(f.isValid(classes[0], t, department));
        f.setTimeslots(timeslots);
        assertFalse(f.isValid(classes[8], t, department));
        assertFalse(f.isValid(classes[7], t, department));
        assertFalse(f.isValid(classes[6], t, department));
        assertFalse(f.isValid(classes[5], t, department));
        assertFalse(f.isValid(classes[4], t, Department.HUMA));
        assertFalse(f.isValid(classes[3], t, department));
        assertFalse(f.isValid(classes[2], new Timeslot("09:00:00", "10:00:00", Day.Saturday), department));
        assertTrue(f.isValid(classes[1], t, department));
        assertTrue(f.isValid(classes[0], t, department));
        f.setProfessor(professor);
        assertFalse(f.isValid(classes[8], t, department));
        assertFalse(f.isValid(classes[7], t, department));
        assertFalse(f.isValid(classes[6], t, department));
        assertFalse(f.isValid(classes[5], t, department));
        assertFalse(f.isValid(classes[4], t, Department.HUMA));
        assertFalse(f.isValid(classes[3], t, department));
        assertFalse(f.isValid(classes[2], new Timeslot("09:00:00", "10:01:00", Day.Friday), department));
        assertFalse(f.isValid(classes[1], t, department));
        assertTrue(f.isValid(classes[0], t, department));
    }
}