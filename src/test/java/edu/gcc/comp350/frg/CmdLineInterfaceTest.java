package edu.gcc.comp350.frg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmdLineInterfaceTest {

    @Test
    void runInterfaceTest(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface.runInterface(api, true, "TestFiles/AutoCommandLineTests");
    }

    @Test
    void validateBadInput(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface.runInterface(api, true, "TestFiles/VerifyBadInput");
    }

    @Test
    void MakeASearch(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface.runInterface(api, true, "TestFiles/MakeASearch");
    }

    @Test
    void TestMultipleSchedules(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface.runInterface(api, true, "TestFiles/MultipleSchedulesTest");
    }

    @Test
    void addFilters(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface.runInterface(api, true, "TestFiles/addFiltersTest");
    }

    @Test
    void classConflictTest(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface.runInterface(api, true, "TestFiles/classConflictTest");
    }

    //Only used for cleaning up the database... NOT ACTUALLY A TEST, Just needed a way to run the cmd
//    @Test
//    void deleteScheduleCmd(){
//        Schedule.deleteScheduleByIDFromDB(1);
//    }
}