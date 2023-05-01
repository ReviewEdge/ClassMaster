package edu.gcc.comp350.frg;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;

@RestController
public class SpringWebAPI {

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
    @GetMapping("/schedule")
    @ResponseBody
    public Schedule schedule(@RequestParam(value = "query", defaultValue = "") String query) throws Exception {

        Schedule mySchedule = null;

        if (query.equals("")){
            ArrayList<Class> someclasses = new ArrayList<>();
             mySchedule = new Schedule("my schedule",new Term(10),someclasses);
        }


        /*
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
        */

        ArrayList<Class> testClasses = new ArrayList<>();
        Class acct = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 A");
        Class acct2 = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 B");
        testClasses.add(acct);
        testClasses.add(acct2);

        return  new Schedule("testSched", new Term("Spring 2020"), testClasses);
    }

    @CrossOrigin
    @GetMapping("/schedule-name")
    @ResponseBody
    public String schedName() {
        String help = "help me homie";
        return "{'name': 'hello?', 'body': 'Notice my data pls'}";
    }

    @CrossOrigin
    @GetMapping("/class-list")
    @ResponseBody
    public String classList() {
        ArrayList<Class> testClasses = new ArrayList<>();
        Class acct = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 A");
        Class acct2 = Class.getClassFromDBbyCourseCode("2020 10 ACCT 201 B");
        testClasses.add(acct);
        testClasses.add(acct2);

        return  new Schedule("testSched", new Term(10), testClasses).toString();
    }



}
