package ru.nsu.ccfit.pleshkov.lab1;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class StatisticsHandlerTest {
    @Test
    public void printStats() throws FileNotFoundException {
        System.setOut(new PrintStream("testout"));
        HashMap<Filter, Stats> map = new HashMap<>();
        Filter one = new FilenameExtensionFilter(".l");
        Filter two = new FileModificationTimeFilter(true, 4567*1000);
        map.put(one, new Stats(7, 1));
        map.put(two, new Stats(3, 2));
        ArrayList<Filter> args = new ArrayList<>();
        args.add(one);
        args.add(two);
        map.put(new AndFilter(args), new Stats(2, 1));
        Statistics statistics = new Statistics(map, new Stats(23,7),"testdirectory","testconfig");
        StatisticsHandler.printStats(statistics);
        Assert.assertTrue(MainTest.areFilesIdentical(new File("testout"),new File("testexamples/test2out")));
    }

}