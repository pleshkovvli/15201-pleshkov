package ru.nsu.ccfit.pleshkov.lab2.factory;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.out.println("Usage: java -jar Factory.jar /path/config.txt");
            System.exit(0);
        }
        try {
            Initializer.startFactory(Paths.get(args[0]));
        } catch (BadParseException e) {
            FactoryLogger logger = FactoryLogger.getLogger();
            if(logger != null) {
                logger.error(e.getMessage());
            }
        }
    }

}
