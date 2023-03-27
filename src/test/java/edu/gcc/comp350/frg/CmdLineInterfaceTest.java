package edu.gcc.comp350.frg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmdLineInterfaceTest {

    @Test
    void runInterfaceTest(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface.runInterface(api, true);
    }

}