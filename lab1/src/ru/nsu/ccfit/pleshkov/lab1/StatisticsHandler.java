package ru.nsu.ccfit.pleshkov.lab1;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class StatisticsHandler {
    private Statistics stats;

    public void printStats() {
        System.out.println("Directory: " + stats.getDirectory());
        System.out.println("Configuration: " + stats.getConfigInfo());
        System.out.println("Total: " + stats.getTotal().getLines() + " lines in " + stats.getTotal().getFiles() + " files.");
        SortedSet<Map.Entry<Filter, Stats> > sortedSet = new TreeSet<>(new StatisticsHandler.MyComapator());
        sortedSet.addAll(stats.getStats().entrySet());
        for (Map.Entry<Filter, Stats> entry : sortedSet) {
            System.out.println(entry.getKey().getParam() +" : "
                    + entry.getValue().getLines() + " lines in "
                    + entry.getValue().getFiles() + " files.");
        }
    }

    public StatisticsHandler(Statistics stat) {
        stats = stat;
    }

    class MyComapator implements Comparator<Map.Entry<Filter, Stats> > {
        @Override
        public int compare(Map.Entry<Filter, Stats> first, Map.Entry<Filter, Stats> second) {
            if(first.getValue().getFiles() < second.getValue().getFiles()) {
                return -1;
            } else if (first.getValue().getFiles() > second.getValue().getFiles()) {
                return 1;
            } else if(first.getValue().getLines() < second.getValue().getLines()) {
                return -1;
            } else if (first.getValue().getLines() > second.getValue().getLines()) {
                return 1;
            } else {
                return 1;
            }
        }
    }
}
