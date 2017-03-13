package ru.nsu.ccfit.pleshkov.lab1;

import java.util.*;

public class StatisticsHandler {

    static public void printStats(Statistics stats) {
        if(stats==null) {
            System.out.println("Null stat!");
            return;
        }
        System.out.println("Directory     — " + stats.getDirectory());
        System.out.println("Configuration — " + stats.getConfigInfo());
        if(stats.getTotal()==null) {
            System.out.println("Null total!");
        } else {
            System.out.println("Total — " + stats.getTotal().getLines() + " lines in " + stats.getTotal().getFiles() + " files.");
        }
        if(stats.getStats()==null) {
            System.out.println("Empty stats!");
            return;
        }
        TreeSet<Map.Entry<Filter, Stats> > sortedSet = new TreeSet<>(new StatsComparator());
        sortedSet.addAll(stats.getStats().entrySet());
        int maxFilter = 0;
        int maxLines = 0;
        int maxFiles = 0;
        for (Map.Entry<Filter, Stats> entry : sortedSet) {
            maxFilter = Math.max(entry.getKey().getParam().length(),maxFilter);
            maxLines = Math.max(entry.getValue().getLines(),maxLines);
            maxFiles = Math.max(entry.getValue().getFiles(),maxFiles);
        }
        String format = "%-" + maxFilter + "s — %-" + String.valueOf(maxLines).length() +
                "d lines in %-" + String.valueOf(maxFiles).length() +"d files.\n";
        char[] array = new char[maxFilter+1];
        Arrays.fill(array,'—');
        System.out.println(array);
        for (Map.Entry<Filter, Stats> entry : sortedSet.descendingSet()) {
            if(entry.getValue().getLines()>0) {
                System.out.format(format, entry.getKey().getParam(),
                        entry.getValue().getLines(), entry.getValue().getFiles());
            }
        }
    }

    public StatisticsHandler() {
    }

    static private class StatsComparator implements Comparator<Map.Entry<Filter, Stats> > {
        @Override
        public int compare(Map.Entry<Filter, Stats> first, Map.Entry<Filter, Stats> second) {
            if(first.getValue().getLines() < second.getValue().getLines()) {
                return -1;
            } else if(first.getValue().getLines() > second.getValue().getLines()) {
                return 1;
            } else if(first.getValue().getFiles() < second.getValue().getFiles()) {
                return -1;
            } else if(first.getValue().getFiles() > second.getValue().getFiles()) {
                return 1;
            } else {
                return String.CASE_INSENSITIVE_ORDER.compare(first.getKey().getParam(),second.getKey().getParam());
            }
        }
    }
}
