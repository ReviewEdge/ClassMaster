package edu.gcc.comp350.frg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Time;

class TimeslotTest {

    @Test
    void compareTo() {
        Timeslot earlier = new Timeslot(Time.valueOf("08:00:00"), Time.valueOf("09:00:00"), Day.Friday);
        Timeslot now = new Timeslot("09:00:00", "9:50:00", Day.Friday);
        Timeslot later = new Timeslot("13:00:00", "13:50:00", Day.Friday);
        assertTrue(earlier.compareTo(now) < 0);
        assertTrue(later.compareTo(now) > 0);
    }

    @Test
    void testToString() {
        Timeslot now = new Timeslot("09:00:00", "20:50:00", Day.Friday);
        assertEquals("Friday 9:00 - 20:50", now.toString());
    }

    @Test
    void testOverlaps() {
        //end of earlier is now's start
        Timeslot earlier = new Timeslot("08:00:00", "09:00:00", Day.Friday);
        Timeslot now = new Timeslot("09:00:00", "20:50:00", Day.Friday);
        Timeslot later = new Timeslot("13:00:00", "13:50:00", Day.Friday);
        Timeslot day = new Timeslot("13:00:00", "13:50:00", Day.Tuesday);
        assertFalse(earlier.overlaps(now));
        assertFalse(now.overlaps(earlier));
        assertTrue(now.overlaps(later));
        assertTrue(later.overlaps(now));
        assertTrue(now.overlaps(now));
        assertFalse(later.overlaps(day));
    }
    @Test
    void testIsIn() {
        Timeslot now = new Timeslot("10:00:00", "20:00:00", Day.Friday);
        Timeslot smaller = new Timeslot("15:00:00", "16:00:00", Day.Friday);
        Timeslot outside = new Timeslot("19:00:00", "21:00:00", Day.Friday);
        Timeslot day = new Timeslot("15:00:00", "16:00:00", Day.Sunday);
        assertTrue(smaller.isIn(now));
        assertTrue(now.isIn(now));
        assertFalse(outside.isIn(now));
        assertFalse(day.isIn(now));
    }
    @Test
    void testGetSet(){
        //for code coverage, not needed in terms of testing but is necessary for 70%+
        Timeslot now = new Timeslot("09:00:00", "09:50:00", Day.Friday);
        now.setStart("05:30:00");
        assertEquals("05:30:00", now.getStart().toString());
        now.setEnd("20:30:00");
        assertEquals("20:30:00", now.getEnd().toString());
        now.setDay(Day.Sunday);
        assertEquals(Day.Sunday, now.getDay());
        now.setStart(Time.valueOf("07:30:00"));
        now.setEnd(Time.valueOf("09:00:00"));
        assertEquals("Sunday 7:30 - 9:00", now.toString());
    }

    @Test
    void testEquals(){
        String no = "no";
        Timeslot now = new Timeslot("09:00:00", "09:50:00", Day.Friday);
        Timeslot day = new Timeslot("09:00:00", "09:50:00", Day.Sunday);
        assertFalse(now.equals(no));
        assertFalse(now.equals(day));
        assertTrue(now.equals(now));
    }
}