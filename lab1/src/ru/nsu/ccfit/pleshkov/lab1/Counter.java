package ru.nsu.ccfit.pleshkov.lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class Counter {
    private HashMap<Filter, Stats> filters;
    private Stats total;
    String th;

    public Statistics count(String config, String dir) throws Lab1Exception {
        filters = new HashMap<>();
        total = new Stats();
        StandardConfigParser parser = new StandardConfigParser();
        for(Filter fil : parser.parse(config)) {
            filters.put(fil, new Stats());
        }
        Path directory;
        directory = Paths.get(dir);
        FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                try {
                    action(file);
                } catch (Lab1RuntimeException e) {
                    System.out.println(e.getMessage());
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException io) {
                return FileVisitResult.SKIP_SUBTREE;
            }
        };

        try {
            Files.walkFileTree(directory, fv);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return new Statistics(filters,total,dir,config);
    }

    private void action(Path file) {
        th = file.toString();
        boolean isCounted = false;
        for (Map.Entry<Filter, Stats> entry : filters.entrySet()) {
            if (entry.getKey().isFit(file)) {
                int lines = 0;
                try(BufferedReader reader = new BufferedReader(new FileReader(file.toString()))) {
                    while (reader.readLine() != null) {
                        lines++;
                    }
                }  catch (IOException e) {
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
