package ru.nsu.ccfit.pleshkov.lab1;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class StandardConfigParser implements ConfigParser {
    private HashSet<Character> set = new HashSet<Character>();

    @Override
    public ArrayList<Filter> parse(String path) throws Lab1Exception {
        Path dir;
        try {
            dir = Paths.get(path);
        } catch (InvalidPathException e) {
            throw new InputException(e);
        }
        if(!Files.isReadable(dir) || !Files.isRegularFile(dir)) {
            throw new InputException(dir + " is not a readable file");
        }
        set.addAll(FilterFactory.getKeys());
        ArrayList<Filter> list = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty()) {
                    continue;
                }
                if(set.contains(line.charAt(0))) {
                    try {
                        boolean isNew = true;
                        for(Filter fil : list) {
                            if(fil.getParam().equals(line)) {
                                isNew = false;
                                break;
                            }
                        }
                        if(isNew) {
                            Filter filter = FilterFactory.make(line);
                            list.add(filter);
                        }
                    } catch (ParseException ex) {
                        throw new ParseException(ex.getMessage() + line);
                    }
                } else {
                    throw new ParseException("Unknown filter: " + line);
                }
            }
        } catch (FileNotFoundException ex) {
            throw new InputException(path + " is not found");
        } catch (IOException e) {
            throw new InputException(e);
        }
        return list;
    }

}
