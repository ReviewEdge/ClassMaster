package edu.gcc.comp350.frg;

public class NewScheduleForm {
    private String loginSecret;
    private String name;
    private String term;

    public NewScheduleForm(String loginSecret, String name, String term) {
        this.loginSecret = loginSecret;
        this.name = name;
        this.term = term;
    }

    public String getLoginSecret() {
        return loginSecret;
    }

    public String getName() {
        return name;
    }

    public String getTerm() {
        return term;
    }
}
