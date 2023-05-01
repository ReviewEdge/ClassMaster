package edu.gcc.comp350.frg;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

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
        Term sendTerm = new Term(10);

        System.out.println("SENDING TERM OBJECT: " + sendTerm);

        return sendTerm;
    }

    @CrossOrigin
    @GetMapping("/search")
    @ResponseBody
    public ArrayList<String> search(@RequestParam(value = "query", defaultValue = "") String query) {
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
//        System.out.println(acct);
        try {
            if(acct.equals("")){
                throw new Exception("id left to default value");
            }
            Schedule sch = Schedule.getScheduleByIDFromDB(Integer.parseInt(acct));
            System.out.println("sending calendar results for id=" + acct);

            ArrayList<String> scheduleResultString = new ArrayList<>();
            for (Class c : sch.getClasses()) {
                System.out.println(sch.toString());
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

}
