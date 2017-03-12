package ru.nsu.ccfit.pleshkov.lab1;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

public class StatisticsHandlerTest {
    @Test
    public void printStats() {
        try {
            System.setOut(new PrintStream("testout"));
        } catch (IOException e) {
            Assert.assertTrue(false);
            System.exit(1);
        }
        HashMap<Filter, Stats> map = new HashMap<>();
        Filter one = new FilenameExtensionFilter(".l");
        Filter two = new FileModificationTimeFilter(true, 4567*1000);
        map.put(one, new Stats(7, 1));
        map.put(two, new Stats(3, 2));
        map.put(new AndFilter(one,two), new Stats(2, 1));
        Statistics statistics = new Statistics(map, new Stats(23,7),"testdirectory","testconfig");
        StatisticsHandler.printStats(statistics);
        Assert.assertTrue(MainTest.areFilesIdentical(new File("testout"),new File("testexamples/test2out")));
    }

}