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
        this.account = new Account(-1, "User", "userX@gcc.edu", "password", "username");
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

    public void setCurrentSearch(Search currentSearch) {
        this.currentSearch = currentSearch;
    }

    public void makeNewSearch(Search s){
        searchHistory.add(currentSearch);
        setCurrentSearch(s);
    }

    public void loadSchedule(int scheduleIndex){

    }


    public static void main(String[] args) {
        System.out.println("hello there");

        // SAMPLE CODE
//        System.out.println(Class.getClassFromDBbyCourseCode("ACCT 201 A"));

        System.out.println("It's scheduling time!\n");
        Main main = new Main();
        API api = new API(main);

        CmdLineInterface.runInterface(api, false);
    }
}
