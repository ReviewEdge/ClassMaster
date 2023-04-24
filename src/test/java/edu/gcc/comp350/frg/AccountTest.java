package edu.gcc.comp350.frg;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void getSchedules() {
    }

    @Test
    void addSchedule() {
    }

    @Test
    void saveOrUpdateAccount() throws Exception {
        int randomNum = ThreadLocalRandom.current().nextInt(40, 10000 + 1);
        String accountName = Integer.toString(randomNum);

        Account newAccount = new Account(accountName, "userX@gcc.edu", "password", "username");
        newAccount.saveOrUpdateAccount();

        ArrayList<Class> testClasses = new ArrayList<>();
        Class acct = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 A");
        Class acct2 = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 B");
        testClasses.add(acct);
        testClasses.add(acct2);
        newAccount.addSchedule(new Schedule("testSched", new Term("Fall 2020"), testClasses));

        newAccount.saveOrUpdateAccount();

        assertEquals(accountName, newAccount.getName());
    }

    @Test
    void getAccountByIdFromDB() throws Exception {
        int randomNum = ThreadLocalRandom.current().nextInt(40, 10000 + 1);
        String accountName = Integer.toString(randomNum);

        Account newAccount = new Account(accountName, "userX@gcc.edu", "password", "username");
        newAccount.saveOrUpdateAccount();

        ArrayList<Class> testClasses = new ArrayList<>();
        Class acct = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 A");
        Class acct2 = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 B");
        testClasses.add(acct);
        testClasses.add(acct2);

        Schedule testSched = new Schedule("testSched", new Term("Fall 2020"), testClasses);
        testSched.saveSchedule();

        newAccount.addSchedule(testSched);

        newAccount.saveOrUpdateAccount();

        Account retreiveAccount = Account.getAccountByIdFromDB(newAccount.getId());

        assertEquals(accountName, retreiveAccount.getName());
        assertEquals(retreiveAccount.getSchedules().get(0).toString(), testSched.toString());
    }
}