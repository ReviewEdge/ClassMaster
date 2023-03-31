package edu.gcc.comp350.frg;

public class Term {

    private int id;
    private String name;

    // TODO: I'm not sure how to handle invalid term input. Shouldn't be a problem right now though,
    //  because all of the classes in the DB have a valid trm_cde
    public Term(int id) {
        this.id = id;
        if (id == 10) {
            this.name = "Fall 2020";
        } else if (id == 30) {
            this.name = "Spring 2021";
        } else {
            this.name = null;
        }
    }

    // tries to make a proper term out of an inputted name
    public Term(String name) {
        if ((name.equals("Fall 2020")) || (name.equals("Fall2020")) || (name.equals("fall2020"))) {
            this.id = 10;
            this.name = "Fall 2020";
        } else if (name.equals("Spring 2021") || (name.equals("Spring2021")) || (name.equals("spring2021"))) {
            this.id = 30;
            this.name = "Spring 2021";
        } else {
            this.id = 0;
            this.name = name;
        }
    }

    public Term(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    // TODO: not sure if other == null should default to false
    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if (!(other instanceof Term))return false;
        Term o = (Term) other;
        return o.id == id && o.name.equals(name);
    }
}
