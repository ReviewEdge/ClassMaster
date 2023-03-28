package edu.gcc.comp350.frg;

import java.util.ArrayList;

public class Search {

    private String query;
    private Filter filter;
    private ArrayList<Class> currentResults;

    public Search(String query, Filter filter) {
        this.query = query;
        this.filter = filter;
        this.currentResults = new ArrayList<Class>();
        useCodeFilter();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Filter getFilter() {
        return filter;
    }

    public Class getClass(int i){
        return currentResults.get(i);
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public ArrayList<Class> getCurrentResults() {
        return currentResults;
    }

    // just doing this to get search working
    public void useCodeFilter() {
        currentResults.add(Class.getClassFromDBbyCourseCode(this.filter.getCode()));
    }
}
