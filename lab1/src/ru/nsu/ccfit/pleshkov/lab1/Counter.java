package ru.nsu.ccfit.pleshkov.lab1;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

public class Counter {
    private static Logger logger = Logger.getLogger(Counter.class.toString());
    private HashMap<Filter, Stats> filters;
    private Stats total;

    private static final String LOG_FILE_NAME = "errors.log";

    public Statistics count(String config, String dir) throws Lab1Exception {
        if(config==null || dir==null) {
            throw new InputException("Null args!");
        }
        filters = new HashMap<>();
        total = new Stats();
        Path directory;
        try {
            directory = Paths.get(dir);
        } catch (InvalidPathException e) {
            throw new InputException(e);
        }
        try {
            if (!Paths.get(config).toFile().exists()) {
                throw new InputException("Config file " + config + " doesn't exists");
            }
        } catch (InvalidPathException e) {
            throw new InputException(e);
        }
        if(!directory.toFile().exists()) {
            throw new InputException("Directory doesn't exists");
        }

        StandardConfigParser parser = new StandardConfigParser();
        for(Filter fil : parser.parse(config)) {
            filters.put(fil, new Stats());
        }
        FileHandler fh;
        try {
            if(!Files.exists(Paths.get(LOG_FILE_NAME))) {
                Files.createFile(Paths.get(LOG_FILE_NAME));
            }
            fh = new FileHandler(LOG_FILE_NAME);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            throw new InputException("Failed to create logfile");
        }
        FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                try {
                    if(!file.toFile().isFile()) {
                        return FileVisitResult.CONTINUE;
                    }
                    action(file);
                } catch (Lab1RuntimeException e) {
                    logger.info(e.getMessage());
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException io) {
                logger.info(io.getMessage());
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if(dir.toFile().length()==0) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                try {
                    return super.preVisitDirectory(dir, attrs);
                } catch (IOException io) {
                    logger.info(io.getMessage());
                    return FileVisitResult.SKIP_SUBTREE;
                }
            }
        };

        try {
            Files.walkFileTree(directory, fv);
        } catch (Exception e) {
            throw new CountException("Fatal error: " + e.getMessage());
        }
        logger.removeHandler(fh);
        return new Statistics(filters,total,dir,config);
    }

    private void action(Path file) {
        boolean isCounted = false;
        for (Map.Entry<Filter, Stats> entry : filters.entrySet()) {
            if (entry.getKey().isFit(file)) {
                int lines = 0;
                try(LineNumberReader  lnr = new LineNumberReader(new FileReader(file.toString()))) {
                    lnr.skip(Long.MAX_VALUE);
                    lines = lnr.getLineNumber();
                } catch (FileNotFoundException e) {
                        throw new Lab1RuntimeException("File not found: " + file.toString() + e.getMessage());
                } catch (IOException e) {
                    throw new Lab1RuntimeException("Failed: " + e.getMessage());
                }
                entry.getValue().addLines(lines);
                entry.getValue().addFiles(1);
                if(!isCounted) {
                    isCounted = true;
                    total.addLines(lines);
                    total.addFiles(1);
                }
            }
        }
    }
}
