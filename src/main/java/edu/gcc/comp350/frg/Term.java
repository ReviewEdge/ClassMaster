package edu.gcc.comp350.frg;

public class Term {

    private int id;
    private String name;

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
        return "Term{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if (!(other instanceof Term))return false;
        Term o = (Term) other;
        return o.id == id && o.name.equals(name);
    }
}
