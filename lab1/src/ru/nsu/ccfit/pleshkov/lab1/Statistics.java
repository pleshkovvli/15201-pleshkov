package ru.nsu.ccfit.pleshkov.lab1;

import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private HashMap<Filter, Stats> stats;
    private Stats total;
    private String directory;
    private String configInfo;

    public void printStats() {
        System.out.println("Directory: " + directory);
        System.out.println("Configuration: " + configInfo);
        System.out.println("Tolal: " + total.lines + " lines in " + total.files + " files.");
        for (Map.Entry<Filter, Stats> entry : stats.entrySet()) {
            System.out.println(entry.getKey().getParam() +" : "
                    + entry.getValue().lines + " lines in "
                    + entry.getValue().files + " files.");
        }
    }

    public Statistics(HashMap<Filter, Stats> map, Stats ttl, String dir, String config) {
        stats = new HashMap<>(map);
        directory = dir;
        configInfo = config;
        total = ttl;
    }

}
