package ru.nsu.ccfit.pleshkov.lab1;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CounterTest {
    @Test
    public void count() {
        try {
            System.setOut(new PrintStream("testout"));
        } catch (IOException e) {
            Assert.assertTrue(false);
            System.exit(1);
        }
        HashMap<Filter, Stats> map = new HashMap<>();
        Filter one = new FilenameExtensionFilter(".c");
        Filter two = new FilenameExtensionFilter(".h");
        Filter three = new FileModificationTimeFilter(true, 5656566656L*1000);
        ArrayList<Filter> args = new ArrayList<>();
        args.add(one);
        args.add(two);
        Filter four = new OrFilter(args);
        map.put(one, new Stats(8, 4));
        map.put(two, new Stats(8, 2));
        map.put(three, new Stats(21,7));
        map.put(four, new Stats(16,6));
        Counter counter = new Counter();
        Statistics statistics = null;
        try {
            statistics = counter.count("testexamples/config1","testexamples/test1");
        } catch (Lab1Exception e) {
            e.printStackTrace();
            Assert.fail();
            System.exit(1);
        }
        Assert.assertTrue(statistics.getConfigInfo().equals("testexamples/config1"));
        Assert.assertTrue(statistics.getDirectory().equals("testexamples/test1"));
        Assert.assertTrue(statistics.getTotal().equals(new Stats(21,7)));
        HashMap<Filter, Stats> map2 = statistics.getStats();
        for(Map.Entry<Filter, Stats> entry : map.entrySet()) {
            boolean isFail = true;
            for(Map.Entry<Filter, Stats> entry2 : map2.entrySet()) {
                if(entry.getKey().equals(entry2.getKey())) {
                    if(entry.getValue().getLines()!=entry2.getValue().getLines()
                            || entry.getValue().getFiles()!=entry2.getValue().getFiles()) {
                        Assert.fail();
                    } else {
                        isFail = false;
                        break;
                    }
                }
            }
            if(isFail) {
                System.out.println(entry.getKey().getParam());
                Assert.fail();
            }
        }

        for(Map.Entry<Filter, Stats> entry : map2.entrySet()) {
            boolean isFail = true;
            for(Map.Entry<Filter, Stats> entry2 : map.entrySet()) {
                if(entry.getKey().equals(entry2.getKey())) {
                    if(entry.getValue().getLines()!=entry2.getValue().getLines()
                            || entry.getValue().getFiles()!=entry2.getValue().getFiles()) {
                        Assert.fail();
                    } else {
                        isFail = false;
                        break;
                    }
                }
            }
            if(isFail) {
                Assert.fail();
            }
        }
    }
}