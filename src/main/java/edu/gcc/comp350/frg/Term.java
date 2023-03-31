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

    public Term(String name) {
        this.name = name;
        if (name.equals("Fall 2020")) {
            this.id = 10;
        } else if (name.equals("Spring 2020")) {
            this.id = 30;
        } else {
            this.id = 0;
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
