package edu.gcc.comp350.frg;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

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
    void saveSchedule() {

        int randomNum = ThreadLocalRandom.current().nextInt(40, 10000 + 1);


        String schedName = Integer.toString(randomNum);

        ArrayList<Class> testClasses = new ArrayList<Class>();
        Class acct = Class.getClassFromDBbyCourseCode("ACCT 201 A");
        Class acct2 = Class.getClassFromDBbyCourseCode("ACCT 201 B");
        testClasses.add(acct);
        testClasses.add(acct2);

        Schedule schedTest = new Schedule(schedName, new Term(0, "testTerm"), testClasses);
        schedTest.saveSchedule();

        assertEquals(schedName, schedTest.getName());
    }

    @Test
    void getScheduleByIDFromDB() {
        int randomNum = ThreadLocalRandom.current().nextInt(40, 10000 + 1);
        String schedName = Integer.toString(randomNum);

        ArrayList<Class> testClasses = new ArrayList<Class>();
        Class acct = Class.getClassFromDBbyCourseCode("ACCT 201 A");
        Class acct2 = Class.getClassFromDBbyCourseCode("ACCT 201 B");
        testClasses.add(acct);
        testClasses.add(acct2);

        Schedule schedTest = new Schedule(schedName, new Term(0, "testTerm"), testClasses);
        schedTest.saveSchedule();
        assertEquals(schedName, Schedule.getScheduleByIDFromDB(schedTest.getId()).getName());
    }
}