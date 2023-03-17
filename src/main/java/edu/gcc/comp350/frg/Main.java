package edu.gcc.comp350.frg;

import java.util.ArrayList;

public class Main {

    private Account account;
    private Schedule currentSchedule;
    private Search currentSearch;  //What's this? the current search
    private ArrayList<Search> searchHistory;
    //private GUI gui


    public Main(Account account, Schedule currentSchedule, Search currentSearch, ArrayList<Search> searchHistory) {
        this.account = account;
        this.currentSchedule = currentSchedule;
        this.currentSearch = currentSearch;
        this.searchHistory = searchHistory;
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

    public void loadSchedule(int scheduleIndex){

    }


    public static void main(String[] args) {
        System.out.println("hello there");
        System.out.println("It's scheduling time!\n");
        CmdLineInterface.runInterface();
    }







}
