package edu.gcc.comp350.frg;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;

@RestController
public class SpringWebAPI {

//    private static final String template = "Hello, %s!";
//    private final AtomicLong counter = new AtomicLong();

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
}