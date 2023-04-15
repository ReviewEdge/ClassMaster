package edu.gcc.comp350.frg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmdLineInterfaceTest {

    @Test
    void runInterfaceTest(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface cli = new CmdLineInterface(api, true, "TestFiles/AutoCommandLineTests");
        cli.runInterface();
    }

    @Test
    void validateBadInput(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface cli = new CmdLineInterface(api, true, "TestFiles/VerifyBadInput");
        cli.runInterface();
    }

    @Test
    void MakeASearch(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface cli = new CmdLineInterface(api, true, "TestFiles/MakeASearch");
        cli.runInterface();
    }

    @Test
    void TestMultipleSchedules(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface cli = new CmdLineInterface(api, true, "TestFiles/MultipleSchedulesTest");
        cli.runInterface();
    }

    @Test
    void addFilters(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface cli = new CmdLineInterface(api, true, "TestFiles/addFiltersTest");
        cli.runInterface();
    }

    @Test
    void classConflictTest(){
        Main main = new Main();
        API api = new API(main);
        CmdLineInterface cli = new CmdLineInterface(api, true, "TestFiles/classConflictTest");
        cli.runInterface();
    }

    //Only used for cleaning up the database... NOT ACTUALLY A TEST, Just needed a way to run the cmd
//    @Test
//    void deleteScheduleCmd(){
//        Schedule.deleteScheduleByIDFromDB(1);
//    }
}