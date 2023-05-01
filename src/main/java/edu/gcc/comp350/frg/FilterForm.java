package edu.gcc.comp350.frg;

public class FilterForm {
    private String professor;
    private String code;
    private int minimum;
    private int maximum;
    private String department;
    private Timeslot[] timeslots;

    public FilterForm(String professor, String code, String minimum, String maximum, String department, String timeslots) {
        this.professor = professor;
        this.code = code;
        this.minimum = Integer.parseInt(minimum);
        this.maximum = Integer.parseInt(maximum);
        this.department = department;
        String[] t = timeslots.substring(1, timeslots.length()-1).split(", ");//remove brackets, split array
        this.timeslots = new Timeslot[t.length];
        for(int i=0; i<t.length; i++){
            this.timeslots[i] = new Timeslot(t[i]);
        }
    }

    public String getProfessor() {
        return professor.trim().equals("") ? null : professor;
    }

    public String getCode() {
        return code.trim().equals("") ? null : code;
    }

    public String getDepartment() {
        return department.trim().equals("") ? null : department;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public Timeslot[] getTimeslots() {
        return timeslots;
    }
}
