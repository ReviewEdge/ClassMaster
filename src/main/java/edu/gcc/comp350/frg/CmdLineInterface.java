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
        System.out.println("Printing Schedule List here");


        boolean notQuit = true;
        while(notQuit){
            String cmd = getInput();
            String[] cmdSplit = cmd.split(" ");

            // Quits
            if(cmdSplit[0].toLowerCase().contains("quit")){
                notQuit = false;
                continue;
            }

            // Handles the Help Cmd for each screen
            if(cmdSplit[0].toLowerCase().contains("help")) {
                switch (screen) {
                    case SCHEDULE_LIST -> {
                        System.out.println("Schedule Help");
                    }
                    case CALENDAR -> {
                        System.out.println("Calendar Help");
                    }
                    case SEARCH -> {
                        System.out.println("Search Help");
                    }
                }
            }

            // Handles the rest of the Commands
            switch (screen) {
                case SCHEDULE_LIST -> {
                    if (cmdSplit[0].equals("Load")){
                         screen = Screen.CALENDAR;
                    }
                }
                case CALENDAR -> {
                    if (cmdSplit[0].equals("Search")){
                        screen = Screen.SEARCH;
                    }

                }
                case SEARCH -> {

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
 **/