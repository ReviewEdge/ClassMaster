package edu.gcc.comp350.frg;

import java.util.Scanner;

public class CmdLineInterface {

//    private Scanner scn;
//    private


    enum Screen {
        SCHEDULE_LIST,
        CALENDAR,
        SEARCH
    }

    public CmdLineInterface(){



    }


    public static void runInterface(){
        System.out.println("Welcome to ClassMaster!");
        System.out.println("Your Journey starts at edu.gcc.comp350.frg");
        System.out.println("And Its COMPLETELY FREE, until you bribe us");

        // Load Data TODO


        Screen screen = Screen.SCHEDULE_LIST;
        API api = new API();

        System.out.println("Printing Schedule List here");


        boolean notQuit = true;
        while(notQuit){
            String cmd = getInput();
            String[] cmdSplit = cmd.split(" ");

            // Loop if there was no command entered
            if(cmdSplit.length <= 0){
                continue;
            }

            // Quits
            if(cmdSplit[0].toLowerCase().contains("quit")){
                notQuit = false;
                System.out.println("Thank you for using ClassMaster");
                api.quit();
                continue;
            }

            // Handles the Help Cmd for each screen
            if(cmdSplit[0].toLowerCase().contains("help")) {
                switch (screen) {
                    case SCHEDULE_LIST -> {
                        System.out.println("Schedule Help"); //TODO
                    }
                    case CALENDAR -> {
                        System.out.println("Calendar Help"); //TODO
                    }
                    case SEARCH -> {
                        System.out.println("Search Help"); //TODO
                    }
                }
            }

            // Handles the rest of the Commands
            switch (screen) {
                case SCHEDULE_LIST -> {

                    // Loads the selected Schedule and activates calendar view
                    if (cmdSplit[0].toLowerCase().contains("load")){
                         //Errors if the command wasn't format correctly, then loops
                         try {
                             int i = Integer.parseInt(cmdSplit[1]);
                             screen = Screen.CALENDAR;
                             api.loadSchedule(i);
                             System.out.println("Show actual display here"); //TODO
                         }
                         catch(Exception e){
                             System.out.println("Invalid Load Parameter, Please use a number");
                         }
                    }
                    else if (cmdSplit[0].toLowerCase().contains("createSchedule")){
                        System.out.println("createSchedule: WIP"); //TODO
                    }
                    else if (cmdSplit[0].toLowerCase().contains("viewschedule")){
                        System.out.println("viewSchedule: WIP"); //TODO
                    }
                    else{
                        System.out.println("Unknown Command, Type \"Help\" for a list of commands");
                    }
                }
                case CALENDAR -> {
                    if (cmdSplit[0].toLowerCase().contains("search")){
                        screen = Screen.SEARCH;
                    }
                    else if (cmdSplit[0].toLowerCase().contains("viewcalendar")){
                        System.out.println("ViewingCal: WIP");  //TODO
                    }
                    else if (cmdSplit[0].toLowerCase().contains("getdescription")){
                        System.out.println("GettingDesc: WIP"); //TODO
                    }
                    else if (cmdSplit[0].toLowerCase().contains("remove")){
                        System.out.println("RemovingClass: WIP"); //TODO
                    }
                    else{
                        System.out.println("Unknown Command, Type \"Help\" for a list of commands");
                    }

                }
                case SEARCH -> {
                    if (cmdSplit[0].toLowerCase().contains("viewschedule")){
                        System.out.println("returning to SChedule: WI{"); //TODO
                        screen = Screen.CALENDAR;
                    }
                    else if (cmdSplit[0].toLowerCase().contains("search")){
                        System.out.println("SearchUnavailible: WIP"); //TODO
                    }
                    else if (cmdSplit[0].toLowerCase().contains("addfilter")){
                        System.out.println("FilterSystem WIP");             //TODO
                    }
                    else if (cmdSplit[0].toLowerCase().contains("clearfilter")){
                        System.out.println("ClearFilter: WIP");     //TODO
                    }
                    else if (cmdSplit[0].toLowerCase().contains("removefilter")){
                        System.out.println("RemoveFilter Not Supported Yet");   //TODO
                    }
                    else if (cmdSplit[0].toLowerCase().contains("addclass")){
                        System.out.println("AddClass System: WIP");     //TODO
                    }
                    else{
                        System.out.println("Unknown Command, Type \"Help\" for a list of commands");
                    }
                }
            }
        }
    }

    public static String getInput(){
        System.out.print(":");
        Scanner scn = new Scanner(System.in);
        return scn.nextLine();
    }





}


/**
 * Commands to allow for each "screen"
 *
 * All:
 *  - Quit
 *  - Help
 * Schedule List:
 *      - CreateSchedule "Name (if none: default)"
 *      - ViewSchedule
 *      - Load 1,2,3...  == Loads given schedule from the list
 *
 * Calendar:
 *      - Search
 *      - Remove 1,2...n
 *      - ViewCalendar
 *      - getDescription 1,2...n == Gets description of class
 *      - ChangeSchedule
 *      - renameSchedule newName
 *
 * Search:
 *      - Search afojbsojdfbssf
 *      - addFilter _______________
 *      - clearFilter ____________
 *      - (removeFilter ________)
 *      - addClass #
 *      - ViewSchedule
 **/