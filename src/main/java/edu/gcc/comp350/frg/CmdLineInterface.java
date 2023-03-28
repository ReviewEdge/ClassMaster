package edu.gcc.comp350.frg;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CmdLineInterface {

    private static Scanner scn;
//    private


    enum Screen {
        SCHEDULE_LIST,
        CALENDAR,
        SEARCH
    }

    public CmdLineInterface(){



    }

    public static void runInterface(API api){
        runInterface(api, false, "");
    }

    public static void runInterface(API api, boolean testing, String testFile){
        if(testing){
            try {
                scn = new Scanner(new File(testFile));
            } catch(Exception e){
                System.out.println(e);
//                System.out.println(new File("").getAbsolutePath());
                System.out.println("Test File not found");
                return;
            }
        }
        System.out.println("Welcome to ClassMaster!");
        System.out.println("Your Journey starts at edu.gcc.comp350.frg");
        System.out.println("And Its COMPLETELY FREE, until you bribe us");

        // Load Data TODO


        Screen screen = Screen.SCHEDULE_LIST;

        displayScheduleList(api.getSchedules());


        boolean notQuit = true;
        while(notQuit){
            String cmd;
            String inputSymbol = "\n";
            if(screen == Screen.SCHEDULE_LIST) {
                inputSymbol = "Schedule List";
            }
            else if (screen == Screen.CALENDAR) {
                inputSymbol = "Calendar View";
            }
            else if (screen == Screen.SEARCH){
                inputSymbol = "Search Command Bar";
            }
            System.out.print(inputSymbol + ": ");
            if(!testing) {
                // Sets the correct location for before the command entry point
                cmd = getInput("");

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
                if(testing){
                    scn.close();
                }
                continue;
            }

            // Handles the Help Cmd for each screen
            if(cmdSplit[0].toLowerCase().contains("help")) {
                if(screen == Screen.SCHEDULE_LIST) {
                    System.out.println("Schedule Page Help:");
                    System.out.println("- CreateSchedule \"Name\": Creates a schedule with the given name, and semester: (in the form: spring 2023)");
                    System.out.println("- ViewSchedules: Shows the list of all your schedules");
                    System.out.println("- Load \"number\": Loads the schedule of the given corresponding number from the list");
                }
                else if(screen == Screen.CALENDAR) {
                    System.out.println("Calendar Page Help:");
                    System.out.println("- ViewCalendar: Displays the list of classes");
                    System.out.println("- GetDescription \"number\": Displays the description of the class at position \"number\"");
                    System.out.println("- RenameSchedule \"NewName\": Changes the current schedule's name to \"NewName\"");
//                        System.out.println("- changeSemester \"NewSemester\": Changes the current schedule's year to \"NewSemester\""); // Shouldn't really be possible
                    System.out.println("- Remove \"number\": Removes the class at position \"number\" from the schedule");
                    System.out.println("- Search: Opens a search bar");
                    System.out.println("- Back: Returns to the list fo schedules");
                }
                else if(screen == Screen.SEARCH) {
                    System.out.println("Search Page Help:"); //TODO
                    System.out.println("- Search \"SearchTerms\": Makes a search for the \"SearchTerms\"");
                    System.out.println("- AddClass \"number\": Adds the class at position \"number\" to the Schedule");
                    System.out.println("- AddFilter \"hypothetical Parameters\": WIP");
                    System.out.println("- RemoveFilter \"hypothetical Parameters\": WIP");
                    System.out.println("- ClearFilter \"hypothetical Parameters\": WIP");
                    System.out.println("- Back: Returns to the Calendar");
                }
            }
            //Handles all the schedule list commands
            else if(screen == Screen.SCHEDULE_LIST){
                // Loads the selected Schedule and activates calendar view
                if (cmdSplit[0].toLowerCase().contains("load")){
                    //Errors if the command wasn't format correctly, then loops
                    if(cmdSplit.length > 1) {
                        int i = Integer.parseInt(cmdSplit[1]);
                        try {
                            api.loadSchedule(i);
                            screen = Screen.CALENDAR;
                            displayCalendar(api.getCurrentSchedule());
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    }
                    else {
                        System.out.println("Invalid Load Parameter, Please use a number");
                    }
                }
                else if (cmdSplit[0].toLowerCase().contains("createschedule")){
                    try { //TODO Change this from a try catch to normal if, Why'd I do that
                        String semester = cmdSplit[2]; //TODO once we have semester
                        try {
                            Schedule sch = api.createSchedule(cmdSplit[1], semester);
                            api.loadSchedule(api.getNumSchedules()); //Could be faster by passing in a Schedule, here at least
                            displayCalendar(sch);
                            screen = Screen.CALENDAR;

                        } catch(Exception e){
                            System.out.println(e.toString());
                        }
                    }
                    catch(Exception e){
                        System.out.println("Invalid createSchedule Parameter, Please include a name and semester");
                    }
                }
                else if (cmdSplit[0].toLowerCase().contains("viewschedules")){
                    displayScheduleList(api.getSchedules());
                }
                else{
                    System.out.println("Unknown Command, Type \"Help\" for a list of commands");
                }
            }
            //Handles all calendar commands
            else if(screen == Screen.CALENDAR){
                if (cmdSplit[0].toLowerCase().contains("search")){
                    screen = Screen.SEARCH;
                }
                else if (cmdSplit[0].toLowerCase().contains("viewcalendar")){
                    displayCalendar(api.getCurrentSchedule());

                }
                else if (cmdSplit[0].toLowerCase().contains("getdescription")){ //TODO Make Exception messages clearer
                    try {

                        System.out.println("Getting Description: ");
                        int i = Integer.parseInt(cmdSplit[1]);
                        System.out.println(api.getClassInfo(i));
                    }catch(Exception e){
                        System.out.println("Invalid getDescription Parameter, Please include a valid index number for the class");
                    }
                }
                else if (cmdSplit[0].toLowerCase().contains("remove")){ //TODO Make Exception messages clearer
                    try {
                        int i = Integer.parseInt(cmdSplit[1]);
                        System.out.println("Removing Class " + i +": ");
                        api.removeClass(i);
                        displayCalendar(api.getCurrentSchedule());
                    }catch(Exception e){
                        System.out.println("Invalid remove Parameter, Please include a valid index number for the class");
                    }
                }
                else if (cmdSplit[0].toLowerCase().contains("renameschedule")){
                    try {
                        String name = cmdSplit[1];
                        System.out.println("Renaming schedule to " + name);
                        api.renameCurrentSchedule(name);
                        displayCalendar(api.getCurrentSchedule());
                    }catch(Exception e){
                        System.out.println("Invalid renameSchedule Parameter, Please include a new name");
                    }
                }
                // Shouldn't really be possible
//                else if (cmdSplit[0].toLowerCase().contains("changesemester")){
//                    try {
//                        String semester = cmdSplit[1]; //TODO Interpret as a Semester
//                        System.out.println("Changing semester to " + semester);
//                        api.changeCurrentSCheduleSemester(name);
//                    }catch(Exception e){
//                        System.out.println("Invalid changeSemester Parameter, Please include a new semester");
//                    }
//                }
                else if (cmdSplit[0].toLowerCase().contains("back")){
                    System.out.println("Returning to Schedule List");
                    displayScheduleList(api.getSchedules());
                    screen = Screen.SCHEDULE_LIST;
                }
                else{
                    System.out.println("Unknown Command, Type \"Help\" for a list of commands");
                }
            }
            //Handles all Search commands
            else if(screen == Screen.SEARCH){
                if (cmdSplit[0].toLowerCase().contains("viewschedule")){
                    System.out.println("Returning to Calendar: ");
                    displayCalendar(api.getCurrentSchedule());
                    screen = Screen.CALENDAR;
                }
                else if (cmdSplit[0].toLowerCase().contains("search")){
                    if(cmdSplit.length > 1){
                        StringBuilder s = new StringBuilder();
                        for(int i  = 1; i < cmdSplit.length; i++){
                            s.append(cmdSplit[i]);
                        }
                        try {
                            api.makeSearch(s.toString());
                            displaySearch(api.getSearchResults());
                        } catch (Exception e){
                            System.out.println(e.toString());
                        }
                    }
                    else{
                        System.out.println("Invalid Search parameters, Please try again or use Help");
                    }
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
                    if(!api.hasCurrentSearch()){
                        System.out.println("Make a search first, before trying to add a class");
                    }
                    else {
                        if (cmdSplit.length > 1) {
                            try {
                                int i = Integer.parseInt(cmdSplit[1]);
                                try {
                                    api.addClass(i);
                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                }
                            } catch (Exception e) {
                                System.out.println("Could not interpret second argument as a number, Please try again");
                            }
                        } else {
                            System.out.println("Invalid addClass parameters, Please try again or use Help");
                        }
                    }
                }
                else if (cmdSplit[0].toLowerCase().contains("back")){
                    System.out.println("Returning to Calendar View");
                    displayCalendar(api.getCurrentSchedule());
                    screen = Screen.CALENDAR;
                }
                else{
                    System.out.println("Unknown Command, Type \"Help\" for a list of commands");
                }
            }
        }
    }

    public static String getInput(String s){
        System.out.print(s);
        Scanner scn = new Scanner(System.in);
        return scn.nextLine();
    }

    public static String askTest(){ //TODO when writing tests
        if(scn.hasNextLine()){
            String str = scn.nextLine();
            System.out.println("(Test) -> " + str);
            return str;
        }
        System.out.println("(Test) -> quit");
        return "quit";
    }

    public static void displayCalendar(Schedule schedule){ //TODO
        System.out.println("        *Better Display Needed*");
        System.out.println(schedule.toString());
    }

    public static void displayScheduleList(ArrayList<Schedule> schedules){ //TODO
        System.out.println("        *Better Display Needed*");
        System.out.println(schedules.toString());
    }

    public static void displaySearch(ArrayList<Class> classes){
        System.out.println("        *Better Display Needed*");
        System.out.println("Search Results:");
        System.out.println(classes);
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