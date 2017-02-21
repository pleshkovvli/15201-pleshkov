package ru.nsu.ccfit.pleshkov.lab1;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        FileIterator fileIterator = new FileIterator("/home/vl/os");
        FilenameExtensionFilter filter = new FilenameExtensionFilter("c");
        ArrayList<Filter> filterArrayList = new ArrayList<Filter>();
        filterArrayList.add(filter);
        Counter counter = new Counter(filterArrayList);
        counter.count(fileIterator);
        counter.printResult();

    }
}
