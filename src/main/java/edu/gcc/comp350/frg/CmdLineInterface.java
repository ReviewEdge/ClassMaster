package edu.gcc.comp350.frg;

import java.util.ArrayList;
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


    public static void runInterface(boolean testing){
        System.out.println("Welcome to ClassMaster!");
        System.out.println("Your Journey starts at edu.gcc.comp350.frg");
        System.out.println("And Its COMPLETELY FREE, until you bribe us");

        // Load Data TODO


        Screen screen = Screen.SCHEDULE_LIST;
        API api = new API();

        System.out.println("Printing Schedule List here");


        boolean notQuit = true;
        while(notQuit){
            String cmd;
            if(!testing) {
                cmd = getInput();
            }
            else{
                cmd = askTest();
            }
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
                        System.out.println("Schedule Page Help:");
                        System.out.println("- CreateSchedule \"Name\": Creates a schedule with the given name, and semester: (in the form: spring 2023)");
                        System.out.println("- ViewSchedules: Shows the list of all your schedules");
                        System.out.println("- Load \"number\": Loads the schedule of the given corresponding number from the list");

                    }
                    case CALENDAR -> {
                        System.out.println("Calendar Page Help:");
                        System.out.println("- ViewCalendar: Displays the list of classes");
                        System.out.println("- GetDescription \"number\": Displays the description of the class at position \"number\"");
                        System.out.println("- RenameSchedule \"NewName\": Changes the current schedule's name to \"NewName\"");
                        System.out.println("- changeSemester \"NewSemester\": Changes the current schedule's year to \"NewSemester\"");
                        System.out.println("- Remove \"number\": Removes the class at position \"number\" from the schedule");
                        System.out.println("- Search: Opens a search bar");
                        System.out.println("- Back: Returns to the list fo schedules");
                    }
                    case SEARCH -> {
                        System.out.println("Search Page Help:"); //TODO
                        System.out.println("WIP");
                        System.out.println("- Back: Returns to the list fo schedules");

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
                             System.out.println("Show actual display here: WIP");
                             System.out.println(displayCalendar(api.getCurrentSchedule()));
                         }
                         catch(Exception e){
                             System.out.println("Invalid Load Parameter, Please use a number");
                         }
                    }
                    else if (cmdSplit[0].toLowerCase().contains("createschedule")){
                        System.out.println("createSchedule: WIP");
                        try {
                            String semester = cmdSplit[2]; //TODO once we have semester
                            api.createSchedule(cmdSplit[1], semester);
                            api.loadSchedule(api.getNumSchedules()-1);
                            System.out.println("Show new Schedule Here");
                            System.out.println(displayCalendar(api.getCurrentSchedule()));
                        }
                        catch(Exception e){
                            System.out.println("Invalid createSchedule Parameter, Please include a name and semester");
                        }
                    }
                    else if (cmdSplit[0].toLowerCase().contains("viewschedules")){
                        System.out.println("viewSchedule: WIP");
                        System.out.println(displayScheduleList(api.getSchedules()));
                    }
                    else{
                        System.out.println("Unknown Command, Type \"Help\" for a list of commands");
                    }
                }
                case CALENDAR -> {
                    if (cmdSplit[0].toLowerCase().contains("search")){
                        screen = Screen.SEARCH;
                        System.out.println("Searching: ");
                    }
                    else if (cmdSplit[0].toLowerCase().contains("viewcalendar")){
                        System.out.println("ViewingCal: WIP");
                        System.out.println(displayCalendar(api.getCurrentSchedule()));

                    }
                    else if (cmdSplit[0].toLowerCase().contains("getdescription")){
                        try {

                            System.out.println("Getting Description: ");
                            int i = Integer.parseInt(cmdSplit[1]);
                            System.out.println(api.getClassDescription(i));
                        }catch(Exception e){
                                System.out.println("Invalid getDescription Parameter, Please include an index number for the class");
                        }
                    }
                    else if (cmdSplit[0].toLowerCase().contains("remove")){
                        try {
                            int i = Integer.parseInt(cmdSplit[1]);
                            System.out.println("Removing Class " + i +": ");
                            api.removeClass(i);
                            System.out.println(displayCalendar(api.getCurrentSchedule()));
                        }catch(Exception e){
                            System.out.println("Invalid remove Parameter, Please include an index number for the class");
                        }
                    }
                    else if (cmdSplit[0].toLowerCase().contains("renameschedule")){
                        try {
                            String name = cmdSplit[1];
                            System.out.println("Renaming schedule to " + name);
                        }catch(Exception e){
                            System.out.println("Invalid renameSchedule Parameter, Please include a new name");
                        }
                    }
                    else if (cmdSplit[0].toLowerCase().contains("changesemester")){
                        try {
                            String semester = cmdSplit[1]; //TODO Interpret as a Semester
                            System.out.println("Changing semester to " + semester);
                        }catch(Exception e){
                            System.out.println("Invalid changeSemester Parameter, Please include a new semester");
                        }
                    }
                    else if (cmdSplit[0].toLowerCase().contains("back")){
                        System.out.println("Returning to Schedule List");
                        System.out.println(displayScheduleList(api.getSchedules()));
                        screen = Screen.SCHEDULE_LIST;
                    }
                    else{
                        System.out.println("Unknown Command, Type \"Help\" for a list of commands");
                    }

                }
                case SEARCH -> {
                    if (cmdSplit[0].toLowerCase().contains("viewschedule")){
                        System.out.println("returning to Schedule: WIP"); //TODO
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
                    else if (cmdSplit[0].toLowerCase().contains("back")){
                        System.out.println("Returning to Calendar View");
                        screen = Screen.CALENDAR;
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

    public static String askTest(){ //TODO when writing tests
        return "";
    }

    public static String displayCalendar(Schedule schedule){ //TODO
        return "yay Schedule";
    }

    public static String displayScheduleList(ArrayList<Schedule> schedules){ //TODO
        return "yay Schedule List";
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