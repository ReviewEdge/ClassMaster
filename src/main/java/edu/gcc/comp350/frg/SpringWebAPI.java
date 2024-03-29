package edu.gcc.comp350.frg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import com.github.javaparser.utils.Pair;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringWebAPI {
    private ArrayList<Integer> loggedInUsers = new ArrayList<>();
    private Filter f = new Filter();
//    private final AtomicLong sessionID = new AtomicLong();


//    private static final String template = "Hello, %s!";
//    private final AtomicLong counter = new AtomicLong();

    private Account validateLoginSecret(String loginSecret) {
        int userID;

        try {
            //TODO: this should do a security thing:
            userID = Integer.parseInt(loginSecret);
        } catch (Exception e) {
            System.out.println("received blank user ID");
            return null;
        }

        //Check if logged in
        //TODO: make this secure by using a secret
        if (!loggedInUsers.contains(userID)) {
            System.out.println("can't get schedules, user not signed in");
            return null;
        }

        try {
            return Account.getAccountByIdFromDB(userID);
        } catch (SQLException e) {
            System.out.println("Account with ID: " + userID + " was not found in database");
            return null;
        }
    }


//    @CrossOrigin
//    @GetMapping("/term-test")
//    @ResponseBody
//    public Term termTest() {
//        Term sendTerm = new Term(30);
//
//        System.out.println("SENDING TERM OBJECT: " + sendTerm);
//
//        return sendTerm;
//    }

    @CrossOrigin
    @GetMapping("/search")
    @ResponseBody
    public ArrayList<String> search(@RequestParam(value = "query", defaultValue = "") String query) {
        ArrayList<String> searchResultStrings = new ArrayList<>();
        f.setTerm(new Term(30));
        Search newSearch = new Search(query, f);

        try {
            newSearch.runQuery();
        } catch (NullPointerException e) {
            System.out.println("no search results for " + query);
            return searchResultStrings;
        }

        for (Class c : newSearch.getCurrentResults() ) {
            searchResultStrings.add(c.toString());
        }

        System.out.println("sending search results for " + query);

        return searchResultStrings;
    }

//    @CrossOrigin
//    @GetMapping("/getSchedule")
//    @ResponseBody
//    public ArrayList<String> getSchedule(@RequestParam(value = "id", defaultValue = "") String scheduleID) {
//        System.out.println("\n---------------------\n");
//        try {
//            if(scheduleID.equals("")){
//                throw new Exception("id left to default value");
//            }
//            Schedule sch = Schedule.getScheduleByIDFromDB(Integer.parseInt(scheduleID));
//            System.out.println("sending calendar results for id=" + scheduleID);
//
//            ArrayList<String> scheduleResultString = new ArrayList<>();
//            for (Class c : sch.getClasses()) {
////                System.out.println(sch.toString());
//                scheduleResultString.add(c.toString());
//            }
//            return scheduleResultString;
//        } catch (Exception e){
//            System.out.println("SpringWebAPI requested for invalid calendar id");
//            System.out.println(e.toString());
//            return null;
//        }
//    }

    @CrossOrigin
    @GetMapping("/getSchedule")
    @ResponseBody
    public String getSchedule(@RequestParam(value = "id", defaultValue = "") String scheduleID,
                              @RequestParam(value = "loginSecret", defaultValue = "") String loginSecret) {

        System.out.println("Attempting to get schedule " + scheduleID);

        // check if logged in, get account if so
        Account realAccount = validateLoginSecret(loginSecret);
        if (realAccount == null) {
            System.out.println("Tried to get schedule for non existing account");
            return null;
        }

        try {
            if(scheduleID.equals("")){
                System.out.println("received blank schedule ID. failed to get schedule.");
                return null;
            }

            Schedule sch = Schedule.getScheduleByIDFromDB(Integer.parseInt(scheduleID));
            System.out.println("sending calendar results for id=" + scheduleID);
            System.out.println(sch.toJSON());
            return sch.toJSON();

            
        } catch (Exception e) {
            System.out.println("failed to get schedule: " + scheduleID);
            System.out.println(e);
            return null;
        }
        
    }


//    @CrossOrigin
//    @PostMapping("/API/setFilter")
//    @ResponseBody
//    public ArrayList<String> setFilter(@RequestBody FilterForm filterForm) {
//        f.setProfessor(filterForm.getProfessor());//sets it to null if empty
//        f.setCode(filterForm.getCode());//sets it to null if empty
//        f.setMinCredits(filterForm.getMinimum());//sets it to -1 if empty
//        f.setMaxCredits(filterForm.getMaximum());//sets it to -1 if empty
//        f.setDepartment(filterForm.getDepartment());//sets it to null if empty
//        //reset the timeslots to match what was sent
//        f.removeAllTimeslots();
//        for(Timeslot t : filterForm.getTimeslots()){
//            f.addTimeslot(t);
//        }
//
//        ArrayList<String> searchResultStrings = new ArrayList<>();
//        Search newSearch = new Search(f);
//
//        try {
//            newSearch.runQuery();
//        } catch (NullPointerException e) {
//            System.out.println("no search results for this filter");
//            return searchResultStrings;
//        }
//
//        for (Class c : newSearch.getCurrentResults() ) {
//            searchResultStrings.add(c.toString());
//        }
//
//        System.out.println("sending search results for this filter");
//
//        return searchResultStrings;
//    }

    @CrossOrigin
    @PostMapping("/API/setFilter")
    @ResponseBody
    public ArrayList<String> setFilter(@RequestBody FilterForm filterForm) {
        f.setProfessor(filterForm.getProfessor());//sets it to null if empty
        f.setCode(filterForm.getCode());//sets it to null if empty
        f.setMinCredits(filterForm.getMinimum());//sets it to -1 if empty
        f.setMaxCredits(filterForm.getMaximum());//sets it to -1 if empty
        f.setDepartment(filterForm.getDepartment());//sets it to null if empty
        //reset the timeslots to match what was sent
        f.removeAllTimeslots();
        for(Timeslot t : filterForm.getTimeslots()){
            f.addTimeslot(t);
        }
        f.setTerm(filterForm.getTerm());

        Search newSearch = new Search(f);
        ArrayList<String> searchResultStrings = new ArrayList<>();


        try {
            newSearch.runQuery();
        } catch (NullPointerException e) {
            System.out.println("no search results for this filter");
            return null;
        }

        for (Class c : newSearch.getCurrentResults() ) {
            searchResultStrings.add(c.toJSON());
        }

        System.out.println("sending search results for this filter");

        return searchResultStrings;
    }
    
    
    
    @CrossOrigin
    @PostMapping("/signup")
    @ResponseBody
    public Integer signup(@RequestBody LoginForm loginForm) {
        try {
            Account newAccount = new Account(null, loginForm.getEmail(), loginForm.getPassword(), null);
            newAccount.saveOrUpdateAccount();
            System.out.println("Created account " + newAccount.getId());
        } catch (Exception e) {
            System.out.println("Error while creating new account for email: " + loginForm.getEmail());
            System.out.println(e);
            return -1;
        }

        return 1;
    }
    
    

    @CrossOrigin
    @PostMapping("/login")
    @ResponseBody
    public Integer login(@RequestBody LoginForm loginForm) {
        Account emptyAccount = new Account(-1, null, null, null, null, null);

        Integer accountID = new Integer(-1);

        try {
            Account realAccount = Account.getAccountByEmailFromDB(loginForm.getEmail());
            accountID = realAccount.getId();

            System.out.println("login attempt for: " + realAccount);

            if (realAccount == null) {
                return accountID;
            }

            if (realAccount.validatePassword(loginForm.getPassword())) {
                loggedInUsers.add(realAccount.getId());
                System.out.println("logged in user " + realAccount.getId());
                return accountID;
            } else {
                return accountID;
            }

        } catch (Exception e) {
            System.out.println(e);
            return accountID;
        }
    }





    @CrossOrigin
    @GetMapping("/logout")
    @ResponseBody
    public Integer logout(@RequestParam(value = "loginSecret", defaultValue = "") String loginSecret) {
        // check if logged in, get account if so
        Account realAccount = validateLoginSecret(loginSecret);
        if (realAccount == null) {
            System.out.println("Failed to logout");
            return -1;
        }

        try {
            loggedInUsers.removeIf(n -> n == realAccount.getId());
            System.out.println("logged out user " + realAccount.getId());
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }


    @CrossOrigin
    @PostMapping(value = "/addClassTest")
    @ResponseBody
    public Schedule addClas(@RequestParam AddDropForm addDropForm){
        System.out.println("\n---------------------\n");
        System.out.println("ADDING CLAS REQUEST");
        System.out.println("Request Recieved to add " +  addDropForm + " to schedule ");
        System.out.println(addDropForm);
        return new Schedule("name", new Term(30), new ArrayList<Class>());
    }


    @GetMapping("/addClass")
    @ResponseBody
    @CrossOrigin
    public String addClass(@RequestParam(value = "loginSecret", defaultValue = "") String loginSecret,
                                       @RequestParam(value = "scheduleID", defaultValue = "") String scheduleID,
                                       @RequestParam(value = "dept", defaultValue = "") String dept,
                                       @RequestParam(value = "courseNum", defaultValue = "") String courseNum,
                                       @RequestParam(value = "section", defaultValue = "") String section,
                                       @RequestParam(value = "year", defaultValue = "") String year,
                                       @RequestParam(value = "term", defaultValue = "") String term){

        System.out.println("\n---------------------\n");
        String courseCode = year + " " + term + " " +  dept + " " + courseNum + " " + section;
        System.out.println("Request Received to add " +  courseCode + " to schedule " + scheduleID);
        JSONObject result = new JSONObject();


        // check if logged in, get account if so
        Account realAccount = validateLoginSecret(loginSecret);
        if (realAccount == null) {
            System.out.println("Failed to add class, user must be logged in");
            result.append("Succeeded", "False");
            result.append("ErrorMessage", "Failed to add class due to invalid of parameters");
            return result.toString();
        }

        // check if user owns the schedule
        if (!realAccount.getScheduleIDs().contains(Integer.parseInt(scheduleID))) {
            String error = "Can't add class because user " + realAccount.getId() + " does not own schedule " + scheduleID;
            System.out.println(error);
            result.append("Succeeded", "False");
            result.append("ErrorMessage", error);
            return result.toString();
        }

        if(scheduleID.equals("") || courseCode.equals("    ")){
            String error = "Failed to add class due to invalid of parameters";
            System.out.println(error);
            result.append("Succeeded", "False");
            result.append("ErrorMessage", error);
            return result.toString();
        }
        try {
            Schedule sch = Schedule.getScheduleByIDFromDB(Integer.parseInt(scheduleID));
            Class cls = Class.getClassFromDBbyCourseCode(courseCode);
            sch.addClass(cls);
            sch.saveSchedule();
            result.append("Succeeded", "True");
            result.append("ErrorMessage", "Class Added");

        } catch (Exception e){
            System.out.println("Failed to add to schedule");
            System.out.println(e.toString());
            result.append("Succeeded", "False");
            result.append("ErrorMessage", e.getMessage());

        }
        return result.toString();

    }

    @CrossOrigin
    @GetMapping("/removeClass")
    @ResponseBody
    public String removeClass(@RequestParam(value = "loginSecret", defaultValue = "") String loginSecret,
                                          @RequestParam(value = "scheduleID", defaultValue = "") String scheduleID,
                                          @RequestParam(value = "dept", defaultValue = "") String dept,
                                          @RequestParam(value = "courseNum", defaultValue = "") String courseNum,
                                          @RequestParam(value = "section", defaultValue = "") String section,
                                          @RequestParam(value = "year", defaultValue = "") String year,
                                          @RequestParam(value = "term", defaultValue = "") String term){
        String courseCode = year + " " + term + " " +  dept + " " + courseNum + " " + section;
        System.out.println("Request Received to remove " +  courseCode + "from schedule " + scheduleID);
        JSONObject result = new JSONObject();

        // check if logged in, get account if so
        Account realAccount = validateLoginSecret(loginSecret);
        if (realAccount == null) {
            String error = "Failed to remove class, user must be logged in";
            System.out.println(error);
            result.append("Succeeded", "False");
            result.append("ErrorMessage", error);
            return result.toString();
        }

        // check if user owns the schedule
        if (!realAccount.getScheduleIDs().contains(Integer.parseInt(scheduleID))) {
            String error = "Can't remove class because user " + realAccount.getId() + " does not own schedule " + scheduleID;
            System.out.println(error);
            result.append("Succeeded", "False");
            result.append("ErrorMessage", error);
            return result.toString();
        }

        if(scheduleID.equals("") || courseCode.equals("    ")){
            System.out.println("Failed to remove class due to invalid of parameters");
            result.append("Succeeded", "False");
            result.append("ErrorMessage", "Failed to remove class due to invalid of parameters");
            return result.toString();
        }
        try {
            Schedule sch = Schedule.getScheduleByIDFromDB(Integer.parseInt(scheduleID));
            ArrayList<Class> classes = sch.getClasses();

            for(int i  = 0; i < classes.size(); i++){
                if(classes.get(i).getCode().equals(courseCode)){
                    sch.removeClass(i);
                    sch.saveSchedule();
                    result.append("Succeeded", "True");
                    result.append("ErrorMessage", "Class Removed");
                }
            }
            throw new Exception("No matching class in the schedule");

        } catch (Exception e){
            System.out.println("Failed to remove from schedule");
            System.out.println(e.toString());
            result.append("Succeeded", "False");
            result.append("ErrorMessage", e.getMessage());
        }
        return result.toString();

    }




    @CrossOrigin
    @GetMapping("/getMySchedules")
    @ResponseBody
    public ArrayList<ArrayList<String>> getMySchedules(@RequestParam(value = "loginSecret", defaultValue = "") String loginSecret) {
        ArrayList<ArrayList<String>> groupy = new ArrayList<>();

        // check if logged in, get account if so
        Account realAccount = validateLoginSecret(loginSecret);
        if (realAccount == null) {
            System.out.println("User not properly logged in, can't get schedules");
            return groupy;
        }

        System.out.println("Getting schedules for user " + realAccount.getId());

        try {
            // get schedule IDs and classes
            groupy = realAccount.getMySchedulesTuples();
        } catch (Exception e) {
            System.out.println("Error while trying to get schedules for " + realAccount.getId());
            return groupy;
        }

        System.out.println("sending schedule for user " + realAccount.getId());

        return groupy;
    }



    @PostMapping("/makeNewSchedule")
    @ResponseBody
    @CrossOrigin
    public ArrayList<String> makeNewSchedule(@RequestBody NewScheduleForm nsF) {
        ArrayList<String> group = new ArrayList<>();

        // check if logged in, get account if so
        Account realAccount = validateLoginSecret(nsF.getLoginSecret());
        if (realAccount == null) {
            System.out.println("User not properly logged in, can't get schedules");
            return group;
        }


        try {
            Schedule newSc = new Schedule(nsF.getName(), new Term(nsF.getTerm()), null);
            newSc.saveSchedule();
            realAccount.addSchedule(newSc);
            realAccount.saveOrUpdateAccount();
            System.out.println("Created schedule " + newSc.getName() + " for user " + realAccount.getId());
            group.add(Integer.toString(newSc.getId()));
            group.add(newSc.getName());
            group.add(newSc.getTerm().toString());
        } catch (SQLException e) {
            System.out.println(e);
            return group;
        } catch (Exception e) {
            System.out.println(e);
            return group;
        }

        return group;
    }

}
