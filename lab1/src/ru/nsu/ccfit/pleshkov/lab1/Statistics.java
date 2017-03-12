package ru.nsu.ccfit.pleshkov.lab1;

import java.util.*;

public class Statistics {

    public HashMap<Filter, Stats> getStats() {
        return stats;
    }

    private HashMap<Filter, Stats> stats;

    public Stats getTotal() {
        return total;
    }

    private Stats total;

    public String getDirectory() {
        return directory;
    }

    private String directory;

    public String getConfigInfo() {
        return configInfo;
    }

    private String configInfo;

    public Statistics(HashMap<Filter, Stats> map, Stats ttl, String dir, String config) {
        stats = new HashMap<>(map);
        directory = dir;
        configInfo = config;
        total = ttl;

    }

    public int getLines(Map.Entry<Filter, Stats> stats) {
        return stats.getValue().getLines();
    }
}
