package ru.nsu.ccfit.pleshkov.lab2.factory;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.pattern.PropertiesPatternConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.out.println("Usage: java -jar Factroy.jar ./path/config.txt");
            System.exit(0);
        }
        try {
            Initializer.startFactory(Paths.get(args[0]));
        } catch (BadParseException e) {
            System.out.println(e.getMessage());
        }
    }

}
