package ru.nsu.ccfit.pleshkov.lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StandardConfigParser implements ConfigParser {
    private HashSet<Character> set;
    private FilterFactory factory;

    @Override
    public ArrayList<Filter> parse(String path) throws FileNotFoundException {
        if(!Files.isRegularFile(Paths.get(path))) {
            throw new FileNotFoundException();
        }
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
                        list.add(factory.make(line));
                    } catch (Exception ex) {
                        ex.getCause().printStackTrace();
                    }
                } else {
                    throw new BadInput();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public StandardConfigParser() {
        set = new HashSet<Character>();
        factory = new FilterFactory();
        set.addAll(factory.getKeys());
    }
}
