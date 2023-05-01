package edu.gcc.comp350.frg;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringWebAPI {
    private ArrayList<Integer> loggedInUsers = new ArrayList<>();
    private Filter f = new Filter();
//    private final AtomicLong sessionID = new AtomicLong();


//    private static final String template = "Hello, %s!";
//    private final AtomicLong counter = new AtomicLong();

    @CrossOrigin
    @GetMapping("/term-test")
    @ResponseBody
    public Term termTest() {
        System.out.println("\n---------------------\n");
        Term sendTerm = new Term(30);

        System.out.println("SENDING TERM OBJECT: " + sendTerm);

        return sendTerm;
    }

    @CrossOrigin
    @GetMapping("/search")
    @ResponseBody
    public ArrayList<String> search(@RequestParam(value = "query", defaultValue = "") String query) {
        System.out.println("\n---------------------\n");

        Filter f = new Filter();  //TODO: get the current filter
        ArrayList<String> searchResultStrings = new ArrayList<>();
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

    @CrossOrigin
    @GetMapping("/calendar")
    @ResponseBody
    public ArrayList<String> calendar(@RequestParam(value = "id", defaultValue = "") String acct) {
        System.out.println("\n---------------------\n");
        try {
            if(acct.equals("")){
                throw new Exception("id left to default value");
            }
            Schedule sch = Schedule.getScheduleByIDFromDB(Integer.parseInt(acct));
            System.out.println("sending calendar results for id=" + acct);

            ArrayList<String> scheduleResultString = new ArrayList<>();
            for (Class c : sch.getClasses()) {
//                System.out.println(sch.toString());
                scheduleResultString.add(c.toString());
            }
            return scheduleResultString;
        } catch (Exception e){
            System.out.println("SpringWebAPI requested for invalid calendar id");
            System.out.println(e.toString());
            return null;
        }
    }

    @CrossOrigin
    @PostMapping("/API/setFilter")
    public void setFilter(@RequestBody FilterForm filterForm) {
        f.setProfessor(filterForm.getProfessor());//sets it to null if empty
        f.setCode(filterForm.getCode());//sets it to null if empty
        f.setMinCredits(filterForm.getMinimum());//sets it to -1 if empty
        f.setMaxCredits(filterForm.getMaximum());//sets it to -1 if empty
        f.setDepartment(filterForm.getDepartment());//sets it to null if empty
        for(Timeslot t : filterForm.getTimeslots()){
            f.addTimeslot(t);
        }
    }

    @CrossOrigin
    @PostMapping("/login")
    @ResponseBody
    public Account login(@RequestBody LoginForm loginForm) {
        System.out.println("\n---------------------\n");
        Account emptyAccount = new Account(-1, null, null, null, null, null);

        try {
            Account realAccount = Account.getAccountByEmailFromDB(loginForm.getEmail());

            System.out.println("login attempt for: " + realAccount);

            if (realAccount == null) {
                return emptyAccount;
            }

            if (realAccount.validatePassword(loginForm.getPassword())) {
                loggedInUsers.add(realAccount.getId());
                System.out.println("logged in user " + realAccount.getId());
                return realAccount;
            } else {
                return emptyAccount;
            }

        } catch (Exception e) {
            System.out.println(e);
            return emptyAccount;
        }
    }

    @CrossOrigin
    @GetMapping("/addClass")
    @ResponseBody
    public ArrayList<Boolean> addClass(@RequestParam(value = "scheduleID", defaultValue = "") String scheduleID,
                                       @RequestParam(value = "dept", defaultValue = "") String dept,
                                       @RequestParam(value = "courseNum", defaultValue = "") String courseNum,
                                       @RequestParam(value = "section", defaultValue = "") String section,
                                       @RequestParam(value = "year", defaultValue = "") String year,
                                       @RequestParam(value = "term", defaultValue = "") String term){
        System.out.println("\n---------------------\n");
        String courseCode = year + " " + term + " " +  dept + " " + courseNum + " " + section;
        System.out.println("Request Recieved to add " +  courseCode + " to schedule " + scheduleID);
        ArrayList<Boolean> result = new ArrayList<>();


        if(scheduleID.equals("") || courseCode.equals("    ")){
            System.out.println("Failed to add class due to lack of parameters");
            result.add(false);
            return result;
        }
        try {
            Schedule sch = Schedule.getScheduleByIDFromDB(Integer.parseInt(scheduleID));
            Class cls = Class.getClassFromDBbyCourseCode(courseCode);
            sch.addClass(cls);
            sch.saveSchedule();
            System.out.println("Course Added");
            result.add(true);
            return result;

        } catch (Exception e){
            System.out.println("Failed to add to schedule");
            System.out.println(e.toString());
            result.add(false);
            return result;
        }
    }

    @CrossOrigin
    @GetMapping("/removeClass")
    @ResponseBody
    public ArrayList<Boolean> removeClass(@RequestParam(value = "scheduleID", defaultValue = "") String scheduleID,
                                       @RequestParam(value = "dept", defaultValue = "") String dept,
                                       @RequestParam(value = "courseNum", defaultValue = "") String courseNum,
                                       @RequestParam(value = "section", defaultValue = "") String section,
                                          @RequestParam(value = "year", defaultValue = "") String year,
                                          @RequestParam(value = "term", defaultValue = "") String term){
        System.out.println("\n---------------------\n");
        String courseCode = year + " " + term + " " +  dept + " " + courseNum + " " + section;
        System.out.println("Request Recieved to remove " +  courseCode + "from schedule " + scheduleID);
        ArrayList<Boolean> result = new ArrayList<>();


        if(scheduleID.equals("") || courseCode.equals("    ")){
            System.out.println("Failed to remove class due to lack of parameters");
            result.add(false);
            return result;
        }
        try {
            Schedule sch = Schedule.getScheduleByIDFromDB(Integer.parseInt(scheduleID));
            ArrayList<Class> classes = sch.getClasses();
            for(int i  = 0; i < classes.size(); i++){
                if(classes.get(i).getCode().equals(courseCode)){
                    sch.removeClass(i);
                    sch.saveSchedule();
                    System.out.println("Course removed");
                    result.add(true);
                    return result;
                }
            }
            throw new Exception("No matching class in the schedule");

        } catch (Exception e){
            System.out.println("Failed to remove class schedule");
            System.out.println(e.toString());
            result.add(false);
            return result;
        }
    }

}
