package ru.nsu.ccfit.pleshkov.lab1;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        try {
            Counter counter = new Counter("/home/vl/os","/home/vl/os/foo/config");
            counter.count();
            counter.printResult();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
