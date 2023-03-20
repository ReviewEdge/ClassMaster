package edu.gcc.comp350.frg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeslotTest {

    @Test
    void compareTo() {
        Timeslot earlier = new Timeslot("08:00:00", "09:00:00", Day.Friday);
        Timeslot now = new Timeslot("09:00:00", "9:50:00", Day.Friday);
        Timeslot later = new Timeslot("13:00:00", "13:50:00", Day.Friday);
        assertTrue(earlier.compareTo(now) < 0);
        assertTrue(later.compareTo(now) > 0);
    }

    @Test
    void testToString() {
        Timeslot now = new Timeslot("09:00:00", "20:50:00", Day.Friday);
        assertEquals("Friday from 9:00 to 20:50.", now.toString());
    }

    @Test
    void overlaps() {
        //end of earlier is now's start
        Timeslot earlier = new Timeslot("08:00:00", "09:00:00", Day.Friday);
        Timeslot now = new Timeslot("09:00:00", "20:50:00", Day.Friday);
        Timeslot later = new Timeslot("13:00:00", "13:50:00", Day.Friday);
        assertFalse(earlier.overlaps(now));
        assertFalse(now.overlaps(earlier));
        assertTrue(now.overlaps(later));
        assertTrue(later.overlaps(now));
    }
}