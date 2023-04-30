package edu.gcc.comp350.frg;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;

@RestController
public class SpringWebAPI {
    private ArrayList<Integer> loggedInUsers = new ArrayList<>();
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
