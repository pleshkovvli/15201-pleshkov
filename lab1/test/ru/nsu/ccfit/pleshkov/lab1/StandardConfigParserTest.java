package ru.nsu.ccfit.pleshkov.lab1;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

public class StandardConfigParserTest {
    @Test
    public void parse() {
        StandardConfigParser parser = new StandardConfigParser();
        HashSet<Filter> fil = new HashSet<>();
        fil.add(new FilenameExtensionFilter(".c"));
        fil.add(new FilenameExtensionFilter(".h"));
        fil.add(new FileModificationTimeFilter(true, 5656566656L*1000));
        fil.add(new OrFilter(new FilenameExtensionFilter(".c"), new FilenameExtensionFilter(".h")));
        HashSet<Filter> filters = null;
        try {
            filters = new HashSet<>(parser.parse("testexamples/config1"));
        } catch (Lab1Exception e) {
            Assert.fail();
        }
        for(Filter entry : fil) {
            boolean isFail = true;
            for(Filter entry2 : filters) {
                if(entry.equals(entry2)) {
                    isFail = false;
                }
            }
            if(isFail) {
                System.out.println(entry.getParam());
                Assert.fail();
            }
        }
        for(Filter entry : filters) {
            boolean isFail = true;
            for(Filter entry2 : fil) {
                if(entry.equals(entry2)) {
                    isFail = false;
                }
            }
            if(isFail) {
                System.out.println(entry.getParam());
                Assert.fail();
            }
        }
    }

    @Test
    public void hardParse() {
        Filter one = new AndFilter(new NotFilter(new OrFilter(new NotFilter(new FileModificationTimeFilter(false,2323*1000)),
                new FilenameExtensionFilter(".ui"))), new FileModificationTimeFilter(true,56*1000));
        StandardConfigParser parser = new StandardConfigParser();
        try {
            ArrayList<Filter> filters = parser.parse("testexamples/config8");
            if(filters.size()!=1) {
                Assert.fail();
            }
            Assert.assertTrue(one.equals(filters.get(0)));
        } catch (Lab1Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}