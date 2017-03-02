package ru.nsu.ccfit.pleshkov.lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Counter {
    private HashMap<Filter, Stats> filters = new HashMap<>();
    private Stats total = new Stats();

    public Statistics count(String config,String dir) throws FileNotFoundException {
        StandardConfigParser parser = new StandardConfigParser();
        for(Filter fil : parser.parse(config)) {
            filters.put(fil, new Stats());
        }
        try (Stream<Path> paths = Files.walk(Paths.get(dir)).filter(Files::isRegularFile)) {
            paths.forEach(this::action);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Statistics(filters,total,dir,config);
    }

    private void action(Path file) {
        boolean isCounted = false;
        for (Map.Entry<Filter, Stats> entry : filters.entrySet()) {
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
                entry.getValue().lines += lines;
                entry.getValue().files ++;
                if(!isCounted) {
                    isCounted = true;
                    total.lines += lines;
                    total.files ++;
                }
            }
        }
    }
}
