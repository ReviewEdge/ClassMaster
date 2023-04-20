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


    @Test
    void saveSchedule() throws Exception {

        int randomNum = ThreadLocalRandom.current().nextInt(40, 10000 + 1);


        String schedName = Integer.toString(randomNum);

        ArrayList<Class> testClasses = new ArrayList<>();
        Class acct = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 A");
        Class acct2 = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 B");
        testClasses.add(acct);
        testClasses.add(acct2);

        Schedule schedTest = new Schedule(schedName, new Term("Fall 2020"), testClasses);
        schedTest.saveSchedule();

        assertEquals(schedName, schedTest.getName());
    }

    @Test
    void getScheduleByIDFromDB() throws Exception {
        int randomNum = ThreadLocalRandom.current().nextInt(40, 10000 + 1);
        String schedName = Integer.toString(randomNum);

        ArrayList<Class> testClasses = new ArrayList<>();
        Class acct = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 A");
        Class acct2 = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 B");
        testClasses.add(acct);
        testClasses.add(acct2);

        Schedule schedTest = new Schedule(schedName, new Term("Fall 2020"), testClasses);
        schedTest.saveSchedule();
        try {
            assertEquals(schedName, Schedule.getScheduleByIDFromDB(schedTest.getId()).getName());
        } catch(Exception e){
            System.out.println(e);
            assert false;
        }
    }

    //TODO:
    @Test
    void addClass() throws Exception {
        Class cl1 = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 A");
        // class with conflicting timeslot:
        Class cl2 = Class.getClassFromDBbyCourseCode("2020 10 BIOL 101 A");
        // doesn't conflict, should add normally
        Class cl3 = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 B");

        Schedule schedTest = new Schedule("test", new Term("Fall 2020"), null);


        schedTest.addClass(cl1);
        String v1 = schedTest.toString();

        try {
            // trying to add conflicting class should not work
            schedTest.addClass(cl2);
            String v2 = schedTest.toString();
        } catch (Exception e) {
            assertEquals(e.toString(), "java.lang.Exception: overlaps class in schedule");
        }

        try {
            schedTest.addClass(cl3);
            String v3 = schedTest.toString();

            assertNotEquals(v1, v3);
        } catch (Exception e) {
            // something broke
            System.out.println("E: " + e);
            fail();
        }

    }
}