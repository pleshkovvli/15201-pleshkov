package ru.nsu.ccfit.pleshkov.lab1;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Usage: LoC.jar <path to congif file> <directory>");
            System.exit(0);
        }
        try {
            Counter counter = new Counter();
            Statistics statistics = counter.count(args[0],args[1]);
            statistics.printStats();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
