package org.lobo.census.graph;

import java.util.Map;
import java.util.TreeMap;

public class CountryData {
    private final String name;
    private Map<String, Integer> populationMap;

    public CountryData(String name) {
        this.name = name;
        this.populationMap = new TreeMap<>();
    }

    protected void add(String year, int population) {
        populationMap.put(year, population);
    }

    protected String getName() {
        return name;
    }

    protected Map<String, Integer> getPopulationMap() {
        return populationMap;
    }
}
