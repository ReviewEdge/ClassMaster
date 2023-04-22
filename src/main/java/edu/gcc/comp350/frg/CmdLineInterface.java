package edu.gcc.comp350.frg;

import java.io.File;
import java.util.*;

public class CmdLineInterface {

    private Scanner scn;
    private API api;
    private boolean testing;
    private Screen screen;

    // Enum used to determine which "Screen" the user is looking at
    // This defines what actions they can take and what each action does
    enum Screen {
        SCHEDULE_LIST,
        CALENDAR,
        SEARCH,
        ACCOUNT
    }

    public CmdLineInterface(API api){
        this.api = api;
        this.testing = false;

        screen = null;
    }

    /** Create a CLI object with optional testing parameters
     *
     * @param api is the interface which allows the front-end to talk to the actual structures
     * @param testing is a boolean variable to delineate if it should take user input or read from a file
     * @param filename is the file which has preloaded commands
     */
    public CmdLineInterface(API api, boolean testing, String filename){
        this.api = api;
        //For testing, Initiates a new scanner and sets it to reading from testFile
        this.testing = testing;
        if(testing){
            try {
                scn = new Scanner(new File(filename));
            } catch(Exception e){
                System.out.println(e.toString());
                System.out.println("Test File not found");
                return;
            }
        }
        screen = null;
    }

    /**
     * This method does all user interaction for ClassMaster from telling them how to format commands
     * to responding to their actions. The interface is broken into 3 main parts or "screens":
     *      The Schedule List Screen is where they can view the list of schedules they've made and (WIP) make changes to their account
     *      The Calendar Screen is where the user can view the classes for a schedule, remove classes and start a search
     *      The Search Screen is where the user can make searches and add filters to their search
     */

    public void runInterface(){

        boolean accountFlag = true;

        System.out.println("Welcome to ClassMaster!");
//        System.out.println("Your Journey starts at edu.gcc.comp350.frg");
//        System.out.println("And Its COMPLETELY FREE, until you bribe us");

        if(accountFlag){
            screen = Screen.ACCOUNT;
        }
        else {
            //Loads schedules which have been saved to the database (ignored if testing)
            //ONLY IF ACCOUNTS ARE DISABLED, Else see above
            api.setDummyAccount();

            if(!testing) {
                api.loadSavedSchedules();
            }
            screen = Screen.SCHEDULE_LIST;
            displayScheduleList();
        }

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
            else if (screen == Screen.ACCOUNT){
                inputSymbol = "Please enter your login information or sign up: (Type help for commands & syntax)\n";
            }

            //Prompts the user for their next command
            cmd = getInput("" + inputSymbol + ": ");
            //Parses it in to be read as commands and parameters
            String[] cmdSplit = cmd.split(" ");

            // Loop if there was no command entered
            if(cmdSplit.length == 0){
                continue;
            }

            // Quits if "quit" is entered
            if(cmdSplit[0].toLowerCase().contains("quit")){
                notQuit = false;
                handleQuitting();
            }

            // Handles the Help Cmd for each screen, outputting legal commands for the given screen
            else if(cmdSplit[0].toLowerCase().contains("help")) {
                displayHelp();
            }
            else if(screen == Screen.SCHEDULE_LIST){
                handleScheduleListScreen(cmdSplit);
            }
            else if(screen == Screen.CALENDAR){
                handleCalendarScreen(cmdSplit);
            }
            else if(screen == Screen.SEARCH){
                handleSearchScreen(cmdSplit);
            }
            else if(screen == Screen.ACCOUNT){
                handleAccountScreen(cmdSplit);
            }
        }
    }

    /**
     * Handles determining the command for the Schedule screen
     * @param cmdSplit Array of User input split on spaces
     */
    private void handleScheduleListScreen(String[] cmdSplit) {
        // Loads the selected Schedule and activates the calendar view screen
        if (cmdSplit[0].toLowerCase().contains("load")){
            loadSchedule(cmdSplit);
        }
        // Handles the createSchedule command: creates a schedule with parameters "name" and "semester"
        else if (cmdSplit[0].toLowerCase().contains("createschedule")){
            handleScheduleCreation(cmdSplit);
        }
        // Handles the viewSchedule Command: Displaying all schedules for the account
        else if (cmdSplit[0].toLowerCase().contains("viewschedules")){
            displayScheduleList();
        }
        // Handles the viewSchedule Command: Displaying all schedules for the account
        else if (cmdSplit[0].toLowerCase().contains("logout")){
            handleLogout();
        }
        // If the command is unknown, show an error msg
        else{
            System.out.println("Unknown Command, Type \"Help\" for a list of commands");
        }
    }

    /**
     * Handles determining the command for the Calendar screen
     * @param cmdSplit Array of User input split on spaces
     */
    private void handleCalendarScreen(String[] cmdSplit) {
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
            getCourseDescription(cmdSplit);
        }
        //Handles the remove # command: removes class # from the schedule
        else if (cmdSplit[0].toLowerCase().contains("remove")){ // Make Exception messages clearer
            handleRemoveCourse(cmdSplit);
        }
        //Handles renaming the Schedule
        else if (cmdSplit[0].toLowerCase().contains("renameschedule")){
            handleRenamingSchedule(cmdSplit);
        }
        // Handles the back command: Returns to the Schedule List screen
        else if (cmdSplit[0].toLowerCase().contains("back")){
            System.out.println("Returning to Schedule List");
            displayScheduleList();
            screen = Screen.SCHEDULE_LIST;
        }
        // If the command is unknown, show an error msg
        else{
            System.out.println("Unknown Command, Type \"Help\" for a list of commands");
        }
    }

    /**
     * Handles determining the command for the Search screen
     * @param cmdSplit Array of User input split on spaces
     */
    private void handleSearchScreen(String[] cmdSplit) {
        //handles the viewSchedule command: returns to the calendar view and displays the current schedule
        if (cmdSplit[0].toLowerCase().contains("viewschedule")){
            System.out.println("Returning to Calendar: ");
            displayCalendar(api.getCurrentSchedule());
            screen = Screen.CALENDAR;
        }
        //Handles the search command: makes a search for the parameter
        else if (cmdSplit[0].toLowerCase().contains("search")){
            handleSearch(cmdSplit);
        }
        // Handles addFilter command: delegated to the helper method
        else if (cmdSplit[0].toLowerCase().contains("addfilter")){
            handleAddFilter();
        }
        // Handles the clearFilter command: clears all current filters
        else if (cmdSplit[0].toLowerCase().contains("clearfilter")){
            System.out.println("ClearingFilter");
            api.clearFilters();
        }
        // Handles the removeFilter command: Removes all filters of a certain type
        else if (cmdSplit[0].toLowerCase().contains("removefilter")){
            handleRemovingFilter();
        }
        // Handles the addClass command: adds the class at position # to the current schedule
        else if (cmdSplit[0].toLowerCase().contains("addclass")){
            handleAddClass(cmdSplit);
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

    /**
     * Handles determining the command for the Account screen
     * @param cmdSplit Array of User input split on spaces
     */
    private void handleAccountScreen(String[] cmdSplit) {
        //handles the viewSchedule command: returns to the calendar view and displays the current schedule
        if (cmdSplit[0].toLowerCase().contains("login")){
            System.out.println("Verifying...");
            handleLogin(cmdSplit);
        }
        //Handles the search command: makes a search for the parameter
        else if (cmdSplit[0].toLowerCase().contains("createaccount")){
            System.out.println("Creating Account...");
            handleAccountCreation(cmdSplit);
        }
        // If the command is unknown, show an error msg
        else{
            System.out.println("Unknown Command, Type \"Help\" for a list of commands");
        }
    }

    /**
     * Handles the account creation process
     *
     * @param cmdSplit Array of User input split on spaces
     */
    private void handleAccountCreation(String[] cmdSplit) {
        if(cmdSplit.length < 3){
            System.out.println("Invalid createAccount command, Type \"Help\" for proper syntax");
        }
        String username = cmdSplit[1];
        String password = cmdSplit[2];
        try{
            api.createAccount(username, password);
            api.loadSavedSchedules();
            screen = Screen.SCHEDULE_LIST;
            displayScheduleList();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Handles the login process
     *
     * @param cmdSplit Array of User input split on spaces
     */
    private void handleLogin(String[] cmdSplit){
        if(cmdSplit.length < 3){
            System.out.println("Invalid login command, Type \"Help\" for proper syntax");
        }
        String username = cmdSplit[1];
        String password = cmdSplit[2];
        try{
            api.loginAccount(username, password);
            api.loadSavedSchedules();
            screen = Screen.SCHEDULE_LIST;
            displayScheduleList();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the logout process
     */
    private void handleLogout() {
        System.out.println("Logging out");
        api.logout();
        screen = Screen.ACCOUNT;
    }

    /**
     * Handles adding a class to the schedule by parsing the user input and telling the api
     * @param cmdSplit Array of User input split on spaces
     */
    private void handleAddClass(String[] cmdSplit) {
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
                        System.out.println(e.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println("Could not interpret second argument as a number, Please try again");
                }
            } else {
                System.out.println("Invalid addClass parameters, Please try again or use Help");
            }
        }
    }

    /**
     * Handles removing a filter, getting correct user input and telling the api
     */
    private void handleRemovingFilter() {
        System.out.println("\nWhat type of filter would you like to remove: ");
        System.out.println("Will Remove ALL filters of that type");
        System.out.println("1: Course Name (Any fragment)");
        System.out.println("2: Course Code (Any fragment, ie. ACCT 201, or COMP)");
        System.out.println("3: Timeslots (ie. Only classes that are held during 10:00am-3:00pm on Monday");
        System.out.println("4: Professor");
//        System.out.println("5: Credits (ie. No more than 3, No less than 2)"); //TODO WIP/Future issue
        // Change error Message when changing these
        System.out.println("Back: return to Search");
        while(true) {
            try {
                String outputFilterType = getInput("Enter a number: ");
                System.out.println();
                if (outputFilterType.equalsIgnoreCase("quit") || outputFilterType.equalsIgnoreCase("back")) {
                    System.out.println("Returning to search");
                    return;
                }
                int num = Integer.parseInt(outputFilterType);
                api.removeFilter(num);
                System.out.println("Filter Removed, Returning to search");
                return;
            } catch (Exception e) {
                System.out.println("Please enter a valid number between 1 and 4");

            }
        }
    }

    /**
     * Handles searching: parsing the search and sending it to the api.
     * Then printing the latest search results
     *
     * @param cmdSplit Array of User input split on spaces
     */
    private void handleSearch(String[] cmdSplit) {
        if(cmdSplit.length > 1){
            StringBuilder s = new StringBuilder();
            for(int i = 1; i < cmdSplit.length; i++){
                s.append(cmdSplit[i]);
                if(i < cmdSplit.length - 1){
                    s.append(" ");
                }
            }
            try {
                api.makeSearch(s.toString());
                displaySearch();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("Invalid Search parameters, Please try again or use Help");
        }
    }

    /** Handles renaming a schedule, aka parsing user input and telling the api
     *
     * @param cmdSplit Array of User input split on spaces
     */
    private void handleRenamingSchedule(String[] cmdSplit) {
        try {
            String name = cmdSplit[1];
            System.out.println("Renaming schedule to " + name);
            api.renameCurrentSchedule(name);
            displayCalendar(api.getCurrentSchedule());
        }catch(Exception e){
            System.out.println("Invalid renameSchedule Parameter, Please include a new name");
        }
    }

    /**
     * Handles removing a course from the current schedule
     *
     * @param cmdSplit Array of User input split on spaces
     */
    private void handleRemoveCourse(String[] cmdSplit) {
        try {
            int i = Integer.parseInt(cmdSplit[1]);
            System.out.println("Removing Class " + i +": ");
            api.removeClass(i-1);
            displayCalendar(api.getCurrentSchedule());
        }catch(Exception e){
            System.out.println("Invalid remove Parameter, Please include a valid index number for the class");
        }
    }

    /**
     * Prints the description for the course corresponding to the user input
     *
     * @param cmdSplit Array of User input split on spaces
     */
    private void getCourseDescription(String[] cmdSplit) {
        try {
            System.out.println("Getting Description: ");
            int i = Integer.parseInt(cmdSplit[1]);
            System.out.println(api.getClassInfo(i));
        }catch(Exception e){
            System.out.println("Invalid getDescription Parameter, Please include a valid index number for the class");
        }
    }

    /** Handles reading in the user input for schedule creation and
     *  communicating with the API
     *
     * @param cmdSplit Array of User input split on spaces
     */
    private void handleScheduleCreation(String[] cmdSplit) {
        if(cmdSplit.length >= 4){ // Change this from a try catch to normal if, Why'd I do that
            String semester = cmdSplit[2] + " " + cmdSplit[3];
            try {
                Schedule sch = api.createSchedule(cmdSplit[1], semester);
                api.loadSchedule(api.getNumSchedules()); //Could be faster by passing in a Schedule, here at least
                displayCalendar(sch);
                screen = Screen.CALENDAR;

            } catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        else {
            System.out.println("Invalid createSchedule Parameter, Please include a name and semester");
            System.out.println("All possible semesters are: " + Term.getValidSemesters());
        }
    }

    /**
     * Displays all the commands possible for the current screen the user's viewing
     */
    private void displayHelp() {
        if(screen == Screen.SCHEDULE_LIST) {
            System.out.println("Schedule Page Help:");
            System.out.println("- CreateSchedule \"Name\": Creates a schedule with the given name, and semester: (in the form: spring 2023)");
            System.out.println("- ViewSchedules: Shows the list of all your schedules");
            System.out.println("- Load \"number\": Loads the schedule of the given corresponding number from the list");
            System.out.println("- Logout: Logs out of the current Account");
        }
        else if(screen == Screen.CALENDAR) {
            System.out.println("Calendar Page Help:");
            System.out.println("- ViewCalendar: Displays the list of classes");
            System.out.println("- GetDescription \"number\": Displays the description of the class at position \"number\"");
            System.out.println("- RenameSchedule \"NewName\": Changes the current schedule's name to \"NewName\"");
//                        System.out.println("- changeSemester \"NewSemester\": Changes the current schedule's year to \"NewSemester\""); // Shouldn't really be possible
            System.out.println("- Remove \"number\": Removes the class at position \"number\" from the schedule");
            System.out.println("- MakeSearch: Opens a search bar");
            System.out.println("- Back: Returns to the list of schedules");
        }
        else if(screen == Screen.SEARCH) {
            System.out.println("Search Page Help:");
            System.out.println("- Search \"SearchTerms\": Makes a search for the \"SearchTerms\"");
            System.out.println("- AddClass \"number\": Adds the class at position \"number\" to the Schedule");
            System.out.println("- AddFilter : Adds a filter (Prompts for which filter/ parameters)");
            System.out.println("- RemoveFilter: Removes a single filter type (Prompts for which filter type)");
            System.out.println("- ClearFilter: Removes all current filters");
            System.out.println("- Back: Returns to the Calendar");
        }
        else if(screen == Screen.ACCOUNT) {
            System.out.println("Account Page Help:");
            System.out.println("- Login \"Username\" \"Password\": Logs in to the account \"username\" with the password \"Password\"");
            System.out.println("- createAccount \"Username\" \"Password\": Creates an account with a username \"username\" and password \"Password\"");
//            System.out.println("  (Account creation currently not supported, Will be available at 6:00pm Thursday night on myGCC");
        }
        System.out.println("- Quit: Exits the program");

    }

    /**
     * Handles anything that needs to be done upon quitting the application
     */
    private void handleQuitting() {
        System.out.println("Thank you for using ClassMaster");
        api.quit(testing);
        if(testing){
            scn.close();
        }
    }

    /** Loads the schedule specified by the user input
     *
     * @param cmdSplit Array of user input split on spaces
     */
    private void loadSchedule(String[] cmdSplit) {
        //Errors if the command wasn't format correctly, then loops
        if(cmdSplit.length > 1) {
            int i = Integer.parseInt(cmdSplit[1]);
            try {
                api.loadSchedule(i);
                screen = Screen.CALENDAR;
                displayCalendar(api.getCurrentSchedule());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            System.out.println("Invalid Load Parameter, Please use a number");
        }
    }

    /**
     *  This method prompts the user for input and returns the next line they enter
     *
     * @param s The string to print out as a prompt for user entry
     * @return the next command to run
     */
    public String getInput(String s){
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
    public String askTest(){
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

    public void displayCalendar(Schedule schedule){
        //TODO make it clearer which classes are which index for remove
        // Prints the header
        System.out.println();
        System.out.println(schedule.toString());
        System.out.println("Weekly View: ");
        StringBuilder str = new StringBuilder();
        str.append("Monday").append(" ".repeat(22));
        str.append("Tuesday").append(" ".repeat(20));
        str.append("Wednesday").append(" ".repeat(20));
        str.append("Thursday").append(" ".repeat(20));
        str.append("Friday").append(" ".repeat(20));
        str.append("\n");
        str.append("-".repeat(130));
        str.append("\n");
        ArrayList<Class> cls = schedule.getClasses();

        // Determines what classes meet on what days of the week
        ArrayList<LinkedList<Class>> daysOfTheWeek = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            daysOfTheWeek.add(new LinkedList<>());
        }

        for(Class c: cls){
            for(Timeslot t: c.getTimeSlots()) {
                daysOfTheWeek.get(t.getDay().ordinal() - 1).add(c);
            }
        }

        daysOfTheWeek = sortDaysByTime(daysOfTheWeek);

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
            str.append("\n");
        }

        // Prints the string
        System.out.println(str);
    }

    /**
     *  Sorts each day's classes by time
     * @param days The ArrayList of LinkedLists which holds all the classes in the schedule that match that day's index
     * @return the new days array
     */
    private ArrayList<LinkedList<Class>> sortDaysByTime(ArrayList<LinkedList<Class>> days){

        for(int i = 0 ; i < days.size(); i++){
            // Create a copy and then sort it
            LinkedList<Class> sortedDay = new LinkedList<>();
            LinkedList<Class> dayOfInterest = new LinkedList<>();
            for(Class c: days.get(i)){
                dayOfInterest.add(new Class(c));
            }
            int k = 0;
            while(k < dayOfInterest.size()){
                int nextClass = 0;
                Timeslot earliest = dayOfInterest.get(nextClass).getTimeOnDay(i+1);
                for(int j = 0; j < dayOfInterest.size(); j++){
                    Timeslot ts = dayOfInterest.get(j).getTimeOnDay(i+1);
                    if(ts.compareTo(earliest) < 0) {
                        earliest = ts;
                        nextClass = j;
                    }
                }
                sortedDay.add(dayOfInterest.remove(nextClass));
            }
            days.set(i, sortedDay);
        }


        return days;
    }

    /**
     * Prints the schedules in the list with index markers
     */
    public void displayScheduleList(){
        ArrayList<Schedule> schedules = api.getSchedules();
        System.out.println("Schedules:");
        for(int i = 1; i <= schedules.size(); i++){
            System.out.println(i + ": " + schedules.get(i-1).getName());
        }    }

    /**
     *  Displays the list of classes with index markers
     */
    public void displaySearch(){
        ArrayList<Class> classes = api.getSearchResults();
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
     */
    public void handleAddFilter(){
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
                String outputFilterType = getInput("Enter a number: ");
                System.out.println();
                if(outputFilterType.equalsIgnoreCase("quit") || outputFilterType.equalsIgnoreCase("back")){
                    return;
                }
                int num = Integer.parseInt(outputFilterType);

                //These handle each type of filter to be added
                if(num == 1){
                    System.out.println("Please enter the Course Name to filter by: ");
                    String courseName = getInput(": ");
                    api.addFilter(1, courseName);
                }
                else if (num == 2) {
                    System.out.println("Please enter the Course code to filter by: ");
                    String courseCode = getInput(": ");
                    api.addFilter(2, courseCode);
                }
                else if (num == 3) {
                    System.out.println("Please enter the Day for the Timeslot in the form");
                    System.out.println("DayLetter startHour:Mins-endHour:Mins -> Hours should be in 24hr time and dayLetters corrospond below");
                    System.out.println("M - Monday, T - Tuesday, W - Wednesday, R - Thursday, F - Friday");
                    System.out.println("Example (Monday from 12:00pm-3:30pm): M 12:00-15:30");
                    try {
                        //This formats the string entered for the api
                        String in = getInput(": ");
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
                    String prof = getInput(": ");
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