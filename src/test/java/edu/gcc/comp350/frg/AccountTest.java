package edu.gcc.comp350.frg;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        Account.deleteAccountByIDFromDB(newAccount.getId());

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

        Account.deleteAccountByIDFromDB(newAccount.getId());
    }

    @Test
    void getAccountsByUsernameFromDBTest() throws Exception {
//        System.out.println("Running UsernameTest");
        Account newAccount1 = new Account("1", "userX@gcc.edu", "pw", "user");
        newAccount1.saveOrUpdateAccount();
        Account newAccount2 = new Account("2", "userY@gcc.edu", "pswd", "users");
        newAccount2.saveOrUpdateAccount();
        Account newAccount3 = new Account("3", "userZ@gcc.edu", "Pass", "notusername");
        newAccount3.saveOrUpdateAccount();

        ArrayList<Account> accountsFromDB = Account.getAccountsByUsernameFromDB("user");

//        System.out.println(accountsFromDB);
        assertEquals(1,accountsFromDB.size() );
        assertTrue(accountsFromDB.get(0).validatePassword("pw"));
        assertFalse(accountsFromDB.get(0).validatePassword("pswd"));

        Account.deleteAccountByIDFromDB(newAccount1.getId());
        Account.deleteAccountByIDFromDB(newAccount2.getId());
        Account.deleteAccountByIDFromDB(newAccount3.getId());
    }

}