package ru.nsu.ccfit.pleshkov.lab1;

import java.util.*;

public class Statistics {
    private HashMap<Filter, Stats> stats;
    private Stats total;
    private String directory;
    private String configInfo;

    public void printStats() {
        System.out.println("Directory: " + directory);
        System.out.println("Configuration: " + configInfo);
        System.out.println("Tolal: " + total.lines + " lines in " + total.files + " files.");
        SortedSet<Map.Entry<Filter, Stats> > sortedSet = new TreeSet<>(new Statistics.MyComapator());
        sortedSet.addAll(stats.entrySet());
        for (Map.Entry<Filter, Stats> entry : sortedSet) {
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

    static public int getLines(Map.Entry<Filter, Stats> stats) {
        return stats.getValue().lines;
    }

    class MyComapator implements Comparator<Map.Entry<Filter, Stats> > {
        @Override
        public int compare(Map.Entry<Filter, Stats> first, Map.Entry<Filter, Stats> second) {
            if(first.getValue().files < second.getValue().files) {
                return -1;
            } else if (first.getValue().files > second.getValue().files) {
                return 1;
            } else if(first.getValue().lines < second.getValue().lines) {
                return -1;
            } else if (first.getValue().lines > second.getValue().lines) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
