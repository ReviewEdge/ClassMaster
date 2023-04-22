package edu.gcc.comp350.frg;

import java.util.ArrayList;

public class Main {

    private Account account;
    private Schedule currentSchedule;
    private Search currentSearch;  //What's this? the current search
    private ArrayList<Search> searchHistory;


    public Main(Account account, Schedule currentSchedule, Search currentSearch, ArrayList<Search> searchHistory) {
        this.account = account;
        this.currentSchedule = currentSchedule;
        this.currentSearch = currentSearch;
        this.searchHistory = searchHistory;
    }

    public Main() {
        this.account = new Account("User", "userX@gcc.edu", "password", "username");
        this.currentSchedule = null;
        this.currentSearch = null;
        this.searchHistory = new ArrayList<>();
    }

    public Account getAccount() {
        return account;
    }

    public Schedule getCurrentSchedule() {
        return currentSchedule;
    }

    public Search getCurrentSearch() {
        return currentSearch;
    }

    public ArrayList<Search> getSearchHistory() {
        return searchHistory;
    }

    public void changeAccount(Account account) {
        this.account = account;
    }

    public void changeCurrentSchedule(Schedule currentSchedule) {
        this.currentSchedule = currentSchedule;
    }

    public void makeNewSearch(Search s){
        searchHistory.add(currentSearch);
        this.currentSearch = s;
    }

    public void loadSchedule(int scheduleIndex){

    }


    public static void main(String[] args) {
//        System.out.println("hello there");

        // DB SAMPLE CODE
//        ArrayList<Class> testClasses = new ArrayList<Class>();
//        Class acct = Class.getClassFromDBbyCourseCode("ACCT 201 A");
//        Class acct2 = Class.getClassFromDBbyCourseCode("ACCT 201 B");
//        testClasses.add(acct);
//        testClasses.add(acct2);
//
//        Schedule schedTest = new Schedule("test", new Term(0, "testTerm"), testClasses);
//        schedTest.saveSchedule();
//        System.out.println("Schedule:\n" + Schedule.getScheduleByIDFromDB(schedTest.getId()));

//        System.out.println("It's scheduling time!\n");
        Main main = new Main();
        API api = new API(main);

        CmdLineInterface CLI = new CmdLineInterface(api);
        CLI.runInterface();
    }
}
