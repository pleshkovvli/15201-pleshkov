package ru.nsu.ccfit.pleshkov.lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Counter {
    private HashMap<Filter, Integer> filters;
    private FileIterator files;
    public void count() {
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
    public Counter(String dir, String config) throws FileNotFoundException {
        filters = new HashMap<>();
        StandardConfigParser parser = new StandardConfigParser();
        for(Filter fil : parser.parse(config)) {
            filters.put(fil,0);
        }
        files = new FileIterator(dir);
    }
}
