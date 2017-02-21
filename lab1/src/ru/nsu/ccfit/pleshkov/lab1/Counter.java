package ru.nsu.ccfit.pleshkov.lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Counter {
    private HashMap<Filter, Integer> filters;
    public Counter(ArrayList<Filter> fil) {
        filters = new HashMap<Filter, Integer>();
        for(Filter filter : fil) {
            filters.put(filter,0);
        }
    }
    public void count(FileIterator files) {
        for (Path file : files.files) {
            for (Map.Entry<Filter, Integer> entry : filters.entrySet()) {
                if (entry.getKey().isFit(file)) {
                    int lines = 0;
                    try(BufferedReader reader = new BufferedReader(new FileReader(file.toString()))) {
                        while (reader.readLine() != null) {
                            lines++;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        continue;
                    }
                    entry.setValue(entry.getValue() + lines);
                }
            }
        }
    }
    public void printResult() {
        for (Map.Entry<Filter, Integer> entry : filters.entrySet()) {
            System.out.println(entry.getValue());
        }
    }
}
