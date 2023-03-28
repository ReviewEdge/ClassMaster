package edu.gcc.comp350.frg;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCode() {
    }

    @Test
    void getClassFromDBbyCourseCode() {
        String acctCode = "ACCT 201 A";
        String acct2Code = "ACCT 201 A";

        Class acct = Class.getClassFromDBbyCourseCode(acctCode);
        Class acct2 = Class.getClassFromDBbyCourseCode(acct2Code);

        assertEquals(acctCode, acct.getCode());
        assertEquals(acct2Code, acct2.getCode());
    }

    @Test
    void testToString() {
    }

    @Test
    void testToString1() {
    }

    @Test
    void getCredits() {
    }

    @Test
    void getLocation() {
    }

    @Test
    void getDescription() {
    }

    @Test
    void getTitle() {
    }

    @Test
    void getReferenceNum() {
    }

    @Test
    void getTime() {
    }

    @Test
    void getTerm() {
    }

    @Test
    void getProfessor() {
    }

    @Test
    void getDepartment() {
    }
}