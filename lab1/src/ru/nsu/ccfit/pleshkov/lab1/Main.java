package ru.nsu.ccfit.pleshkov.lab1;

public class Main {

    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Usage: LoC.jar <path to congif file> <directory>");
            System.exit(0);
        }
        try {
            Counter counter = new Counter();
            StatisticsHandler.printStats(counter.count(args[0],args[1]));
        } catch (Lab1Exception e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("!" + e.getMessage());
            e.printStackTrace();
        }
    }
}
