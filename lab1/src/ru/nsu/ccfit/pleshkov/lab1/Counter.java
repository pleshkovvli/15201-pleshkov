package ru.nsu.ccfit.pleshkov.lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Counter {
    private HashMap<Filter, Stats> filters;
    private Stats total;

    public Statistics count(String config, String dir) throws Lab1Exception {
        filters = new HashMap<>();
        total = new Stats();
        StandardConfigParser parser = new StandardConfigParser();
        for(Filter fil : parser.parse(config)) {
            filters.put(fil, new Stats());
        }
        Path directory;
        directory = Paths.get(dir);
        try (Stream<Path> paths = Files.walk(directory).filter(Files::isRegularFile)) {
            paths.forEach(this::action);
        } catch (IOException e) {
            throw new CountException();
        } catch (Lab1RuntimeException e) {
            throw new CountException(e);
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
                } catch (FileNotFoundException ex) {
                    throw new Lab1RuntimeException(ex);
                } catch (IOException e) {
                    throw new Lab1RuntimeException(e);
                }
                entry.getValue().setLines(entry.getValue().getLines() + lines);
                entry.getValue().setFiles(entry.getValue().getFiles() + 1);
                if(!isCounted) {
                    isCounted = true;
                    total.setLines(total.getLines() + lines);
                    total.setFiles(total.getFiles() + 1);
                }
            }
        }
    }
}
