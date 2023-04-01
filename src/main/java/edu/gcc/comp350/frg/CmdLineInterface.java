package edu.gcc.comp350.frg;

import java.io.File;
import java.util.*;

public class CmdLineInterface {

    //Scanner for user input
    private static Scanner scn;

    // Enum used to determine which "Screen" the user is looking at
    // This defines what actions they can take and what each action does
    enum Screen {
        SCHEDULE_LIST,
        CALENDAR,
        SEARCH
    }

    /**
     //Used to run the interface without testing
     //Mainly a helper method so other coders don't need to know what to put in for normal use
     * @param api is the interface which allows the front-end to talk to the actual structures
     */
    public static void runInterface(API api){
        runInterface(api, false, "");
    }

    /**
     * This method does all user interaction for ClassMaster from telling them how to format commands
     * to responding to their actions. The interface is broken into 3 main parts or "screens":
     *      The Schedule List Screen is where they can view the list of schedules they've made and (WIP) make changes to their account
     *      The Calendar Screen is where the user can view the classes for a schedule, remove classes and start a search
     *      The Search Screen is where the user can make searches and add filters to their search
     *
     * @param api is the interface which allows the front-end to talk to the actual structures
     * @param testing is a boolean variable to delineate if it should take user input or read from a file
     * @param testFile is the file which has preloaded commands
     */

    public static void runInterface(API api, boolean testing, String testFile){
        //For testing, Initiates a new scanner and sets it to reading from testFile
        if(testing){
            try {
                scn = new Scanner(new File(testFile));
            } catch(Exception e){
                System.out.println(e);
                System.out.println("Test File not found");
                return;
            }
        }

        //Loads schedules which have been saved to the database (ignored if testing)
        if(!testing) {
            api.loadSavedSchedules();
        }

        System.out.println("Welcome to ClassMaster!");
//        System.out.println("Your Journey starts at edu.gcc.comp350.frg");
//        System.out.println("And Its COMPLETELY FREE, until you bribe us");


        //Sets the current Screen, screen to be the list of loaded schedules initially
        Screen screen = Screen.SCHEDULE_LIST;

        displayScheduleList(api.getSchedules());

        //This is the input loop. While the user doesn't quit it will keep looping
        boolean notQuit = true;
        while(notQuit){
            System.out.println();

            // Sets the correct location for before the command entry point ie: Calendar View:
            // Similar to how the command line functions with its document structure
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

            //Prompts the user for their next command
            cmd = getInput("" + inputSymbol + ": ", testing);
            //Parses it in to be read as commands and parameters
            String[] cmdSplit = cmd.split(" ");

            // Loop if there was no command entered
            if(cmdSplit.length == 0){
                continue;
            }

            // Quits if "quit" is entered
            if(cmdSplit[0].toLowerCase().contains("quit")){
                notQuit = false;
                System.out.println("Thank you for using ClassMaster");
                api.quit();
                if(testing){
                    scn.close();
                }
                continue;
            }

            // Handles the Help Cmd for each screen, outputting legal commands for the given screen
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
                    System.out.println("- MakeSearch: Opens a search bar");
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
                // Loads the selected Schedule and activates the calendar view screen
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
                // Handles the createSchedule command: creates a schedule with parameters "name" and "semester"
                else if (cmdSplit[0].toLowerCase().contains("createschedule")){
                    try { // Change this from a try catch to normal if, Why'd I do that
                        String semester = cmdSplit[2];
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
                // Handles the viewSchedule Command: Displaying all schedules for the account
                else if (cmdSplit[0].toLowerCase().contains("viewschedules")){
                    displayScheduleList(api.getSchedules());
                }
                // If the command is unknown, show an error msg
                else{
                    System.out.println("Unknown Command, Type \"Help\" for a list of commands");
                }
            }
            //Handles all calendar commands
            else if(screen == Screen.CALENDAR){
                // Handles the makeSearch command: moves the current screen to search
                if (cmdSplit[0].toLowerCase().contains("makesearch")){
                    screen = Screen.SEARCH;
                }
                //Handles the viewCalendar command: Displays the current calendar again
                else if (cmdSplit[0].toLowerCase().contains("viewcalendar")){
                    displayCalendar(api.getCurrentSchedule());

                }
                //Handles the getDescription # command: Prints the description of the # class
                else if (cmdSplit[0].toLowerCase().contains("getdescription")){ // Make Exception messages clearer
                    try {

                        System.out.println("Getting Description: ");
                        int i = Integer.parseInt(cmdSplit[1]);
                        System.out.println(api.getClassInfo(i));
                    }catch(Exception e){
                        System.out.println("Invalid getDescription Parameter, Please include a valid index number for the class");
                    }
                }
                //Handles the remove # command: removes class # from the schedule
                else if (cmdSplit[0].toLowerCase().contains("remove")){ // Make Exception messages clearer
                    try {
                        int i = Integer.parseInt(cmdSplit[1]);
                        System.out.println("Removing Class " + i +": ");
                        api.removeClass(i-1);
                        displayCalendar(api.getCurrentSchedule());
                    }catch(Exception e){
                        System.out.println("Invalid remove Parameter, Please include a valid index number for the class");
                    }
                }
                //Handles renaming the Schedule
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
                // Shouldn't really be possible, so for now its Not
//                else if (cmdSplit[0].toLowerCase().contains("changesemester")){
//                    try {
//                        String semester = cmdSplit[1]; // Interpret as a Semester
//                        System.out.println("Changing semester to " + semester);
//                        api.changeCurrentSCheduleSemester(name);
//                    }catch(Exception e){
//                        System.out.println("Invalid changeSemester Parameter, Please include a new semester");
//                    }
//                }
                // Handles the back command: Returns to the Schedule List screen
                else if (cmdSplit[0].toLowerCase().contains("back")){
                    System.out.println("Returning to Schedule List");
                    displayScheduleList(api.getSchedules());
                    screen = Screen.SCHEDULE_LIST;
                }
                // If the command is unknown, show an error msg
                else{
                    System.out.println("Unknown Command, Type \"Help\" for a list of commands");
                }
            }
            //Handles all Search commands
            else if(screen == Screen.SEARCH){
                //handles the viewSchedule command: returns to the calendar view and displays the current schedule
                if (cmdSplit[0].toLowerCase().contains("viewschedule")){
                    System.out.println("Returning to Calendar: ");
                    displayCalendar(api.getCurrentSchedule());
                    screen = Screen.CALENDAR;
                }
                //Handles the search command: makes a search for the parameter
                else if (cmdSplit[0].toLowerCase().contains("search")){
                    if(cmdSplit.length > 1){
                        StringBuilder s = new StringBuilder();
                        for(int i  = 1; i < cmdSplit.length; i++){
                            s.append(cmdSplit[i]);
                            if(i < cmdSplit.length - 1){
                                s.append(" ");
                            }
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
                // Handles addFilter command: delegated to the helper method
                else if (cmdSplit[0].toLowerCase().contains("addfilter")){
                    handleAddFilter(api, testing);
                }
                // Handles the clearFilter command: clears all current filters
                else if (cmdSplit[0].toLowerCase().contains("clearfilter")){
                    System.out.println("ClearingFilter");
                    api.clearFilters();
                }
                // Handles the removeFilter command: Removes all filters of a certain type
                else if (cmdSplit[0].toLowerCase().contains("removefilter")){
                    System.out.println("\nWhat type of filter would you like to remove: ");
                    System.out.println("Will Remove ALL filters of that type");
                    System.out.println("1: Course Name (Any fragment)");
                    System.out.println("2: Course Code (Any fragment, ie. ACCT 201, or COMP)");
                    System.out.println("3: Timeslots (ie. Only classes that are held during 10:00am-3:00pm on Monday");
                    System.out.println("4: Professor");
//                    System.out.println("5: Credits (ie. No more than 3, No less than 2)"); //TODO WIP/Future issue
                    System.out.println("Back: return to Search");
                    try {
                        String outputFilterType = getInput("Enter a number: ", testing);
                        System.out.println();
                        if(outputFilterType.equalsIgnoreCase("quit") || outputFilterType.equalsIgnoreCase("back")){
                            return;
                        }
                        int num = Integer.parseInt(outputFilterType);
                        api.removeFilter(num);
                    } catch(Exception e){
                        System.out.println("Please enter a valid number between 1 and 6");

                    }
                }
                // Handles the addClass command: adds the class at position # to the current schedule
                else if (cmdSplit[0].toLowerCase().contains("addclass")){
                    if(!api.hasCurrentSearch()){
                        System.out.println("Make a search first, before trying to add a class");
                    }
                    else {
                        if (cmdSplit.length > 1) {
                            try {
                                int i = Integer.parseInt(cmdSplit[1]) - 1;
                                try {
                                    api.addClass(i);
                                    System.out.println("Class Added");
                                } catch (Exception e) {
                                    System.out.println("Index not available, please try again");
//                                    System.out.println(e.toString());
                                }
                            } catch (Exception e) {
                                System.out.println("Could not interpret second argument as a number, Please try again");
                            }
                        } else {
                            System.out.println("Invalid addClass parameters, Please try again or use Help");
                        }
                    }
                }
                // Handles the back command: returns to the calendar view screen
                else if (cmdSplit[0].toLowerCase().contains("back")){
                    System.out.println("Returning to Calendar View");
                    displayCalendar(api.getCurrentSchedule());
                    screen = Screen.CALENDAR;
                }
                // If the command is unknown, show an error msg
                else{
                    System.out.println("Unknown Command, Type \"Help\" for a list of commands");
                }
            }
        }
    }

    /**
     *  This method prompts the user for input and returns the next line they enter
     *
     * @param s The string to print out as a prompt for user entry
     * @param testing whether or not to read
     * @return the next command to run
     */
    public static String getInput(String s, boolean testing){
        System.out.print(s);
        if(!testing) {
            Scanner scn = new Scanner(System.in);
            return scn.nextLine();
        }
        else{
            return askTest();
        }
    }

    /**
     * This method is only used for testing, It reads in the next line from the test document
     * and prints it as a test call
     *
     * @return the next command to be run
     */
    public static String askTest(){
        if(scn.hasNextLine()){
            String str = scn.nextLine();
            System.out.println("(Test) -> " + str);
            return str;
        }
        System.out.println("(Test) -> quit");
        return "quit";
    }

    /**
     *  This method prints out the schedule so the user can see it in a list format and
     *  the way it would be seen on a calendar
     *
     * @param schedule The schedule to print to the console
     */

    public static void displayCalendar(Schedule schedule){ //TODO make it clearer which classes are which index for remove
        // Prints the header
        System.out.println();
        System.out.println(schedule.toString());
        System.out.println("Weekly View: ");
        StringBuilder str = new StringBuilder();
        str.append("Monday").append(" ".repeat(20));
        str.append("Tuesday").append(" ".repeat(20));
        str.append("Wednesday").append(" ".repeat(20));
        str.append("Thursday").append(" ".repeat(20));
        str.append("Friday").append(" ".repeat(20));
        str.append("\n");
        str.append("-".repeat(130));
        str.append("\n");
        ArrayList<Class> cls = schedule.getClasses();

        // Determines what classes meet on what days of the week
        //TODO Figure out a way to sort classes in the list
        ArrayList<LinkedList<Class>> daysOfTheWeek = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            daysOfTheWeek.add(new LinkedList<>());
        }

        for(Class c: cls){
            for(Timeslot t: c.getTimeSlots()) {
                daysOfTheWeek.get(t.getDay().ordinal() - 1).add(c);
            }
        }

        // Adds the classes in a row based on time
        for(int i = 0; i < cls.size(); i++){
            for (int d = 1; d <= daysOfTheWeek.size(); d++) {
                if (daysOfTheWeek.get(d-1).size() > 0) {
                    Class cl = daysOfTheWeek.get(d-1).poll();
                    str.append(cl.getCourseCodeWithoutTerm());
                    str.append(" @");
                    for(Timeslot t: cl.getTimeSlots()) {
                        if (t.getDay().ordinal()== d) {
                            str.append(t.toString().split(" ")[1]);
                            str.append(" - ");
                            str.append(t.toString().split(" ")[3]);
                            break;
                        }
                    }
                    str.append(" ".repeat(3));
                }
                else {
                    str.append(" ".repeat(28));
                }
            }
        }

        // Prints the string
        System.out.println(str);
    }

    /**
     * Prints the schedules in the list with index markers
     *
     * @param schedules is the list of schedules in the account
     */
    public static void displayScheduleList(ArrayList<Schedule> schedules){
        System.out.println("Schedules:");
        for(int i = 1; i <= schedules.size(); i++){
            System.out.println(i + ": " + schedules.get(i-1).getName());
        }    }

    /**
     *  Displays the list of classes with index markers
     *
     * @param classes a list of classes returned from a search
     */
    public static void displaySearch(ArrayList<Class> classes){
        System.out.println("Search Results:");
        for(int i = 1; i <= classes.size(); i++){
            System.out.println(i + ": " + classes.get(i-1));
        }
//        System.out.println(classes);
    }

    /**
     * Handles adding a filter, including getting the type of filter to add and
     * the arguments for the filter. It loops until "quit" or "back" so that
     * the user can enter multiple filters or correct for poor input
     *
     * @param api is the api which the interface is working through
     * @param testing if the program is being run through an automated test
     */
    public static void handleAddFilter(API api, boolean testing){
        while(true) {
            // Prints out filter options
            System.out.println("\nWhat type of filter would you like to add: ");
            System.out.println("1: Course Name (Any fragment)");
            System.out.println("2: Course Code (Any fragment, ie. ACCT 201, or COMP)");
            System.out.println("3: Timeslots (ie. Only classes that are held during 10:00am-3:00pm on Monday");
            System.out.println("4: Professor");
//            System.out.println("5: Department (ie. COMP)");//TODO Do we need this???
//            System.out.println("5: Credits (ie. No more than 3, No less than 2)"); //TODO WIP/Future issue
            System.out.println("Back: return to Search");
            try {
                //Gets the filter type the user wants to use
                String outputFilterType = getInput("Enter a number: ", testing);
                System.out.println();
                if(outputFilterType.equalsIgnoreCase("quit") || outputFilterType.equalsIgnoreCase("back")){
                    return;
                }
                int num = Integer.parseInt(outputFilterType);

                //These handle each type of filter to be added
                if(num == 1){
                    System.out.println("Please enter the Course Name to filter by: ");
                    String courseName = getInput(": ",testing);
                    api.addFilter(1, courseName);
                }
                else if (num == 2) {
                    System.out.println("Please enter the Course code to filter by: ");
                    String courseCode = getInput(": ",testing);
                    api.addFilter(2, courseCode);
                }
                else if (num == 3) {
                    System.out.println("Please enter the Day for the Timeslot in the form");
                    System.out.println("DayLetter startHour:Mins-endHour:Mins -> Hours should be in 24hr time and dayLetters corrospond below");
                    System.out.println("M - Monday, T - Tuesday, W - Wednesday, R - Thursday, F - Friday");
                    System.out.println("Example (Monday from 12:00pm-3:30pm): M 12:00-15:30");
                    try {
                        //This formats the string entered for the api
                        String in = getInput(": ", testing);
                        String day = in.split(" ")[0];
                        String startTime = in.split(" ")[1].split("-")[0];
                        String endTime = in.split(" ")[1].split("-")[1];
                        api.addTimeslotFilter(day, startTime, endTime);
                    } catch(Exception e){
                        System.out.println("Invalid Timeslot, Please try again");
                    }
                }
                else if (num == 4){
                    System.out.println("Please enter the Professor to filter by: ");
                    String prof = getInput(": ",testing);
                    api.addFilter(4, prof);
                }
                else if (num == 5){

                }
                // Catches non-numeric input
            } catch (Exception e) {
                System.out.println("Please enter a valid number between 1 and 6");
            }
        }
    }
}