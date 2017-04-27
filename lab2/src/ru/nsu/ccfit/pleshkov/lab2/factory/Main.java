package ru.nsu.ccfit.pleshkov.lab2.factory;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getLogger("d");
        try {
            Initializer.startFactory(Paths.get("/home/vl/lab2cnf"));
        } catch (BadParseException e) {
            System.out.println("Bad parse at:");
        }
    }

}
