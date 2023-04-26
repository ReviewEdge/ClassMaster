package edu.gcc.comp350.frg;

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

}
