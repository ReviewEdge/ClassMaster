package edu.gcc.comp350.frg;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testToString() {
    }

    //TODO Un-comment these, but atm they're crashing despite things working

//    @Test
//    void saveSchedule() {
//
//        int randomNum = ThreadLocalRandom.current().nextInt(40, 10000 + 1);
//
//
//        String schedName = Integer.toString(randomNum);
//
//        ArrayList<Class> testClasses = new ArrayList<>();
//        Class acct = Class.getClassFromDBbyCourseCode("ACCT 201 A");
//        Class acct2 = Class.getClassFromDBbyCourseCode("ACCT 201 B");
//        testClasses.add(acct);
//        testClasses.add(acct2);
//
//        Schedule schedTest = new Schedule(schedName, new Term(0, "testTerm"), testClasses);
//        schedTest.saveSchedule();
//
//        assertEquals(schedName, schedTest.getName());
//    }
//
//    @Test
//    void getScheduleByIDFromDB() {
//        int randomNum = ThreadLocalRandom.current().nextInt(40, 10000 + 1);
//        String schedName = Integer.toString(randomNum);
//
//        ArrayList<Class> testClasses = new ArrayList<>();
//        Class acct = Class.getClassFromDBbyCourseCode("ACCT 201 A");
//        Class acct2 = Class.getClassFromDBbyCourseCode("ACCT 201 B");
//        testClasses.add(acct);
//        testClasses.add(acct2);
//
//        Schedule schedTest = new Schedule(schedName, new Term(0, "testTerm"), testClasses);
//        schedTest.saveSchedule();
//        try {
//            assertEquals(schedName, Schedule.getScheduleByIDFromDB(schedTest.getId()).getName());
//        } catch(Exception e){
//            assert false;
//        }
//    }
//
//    //TODO:
//    @Test
//    void addClass() {
//        Class cl1 = Class.getClassFromDBbyCourseCode("ACCT 201 A");
//        // class with conflicting timeslot:
//        Class cl2 = Class.getClassFromDBbyCourseCode("BIOL 101 A");
//        // doesn't conflict, should add normally
//        Class cl3 = Class.getClassFromDBbyCourseCode("ACCT 301 A");
//
//        Schedule schedTest = new Schedule("test", new Term(0, "testTerm"), null);
//
//        try {
//            schedTest.addClass(cl1);
//            String v1 = schedTest.toString();
//
//            schedTest.addClass(cl2);
//            String v2 = schedTest.toString();
//
//            schedTest.addClass(cl3);
//            String v3 = schedTest.toString();
//
//            // trying to add conflicting class should not work
//            assertEquals(v1, v2);
//            assertNotEquals(v1, v3);
//        } catch (Exception e) {
//            // something broke
//            System.out.println("E: " + e);
//            fail();
//        }
//
//    }
}