package ru.nsu.ccfit.pleshkov.lab1;

import org.junit.Assert;
import org.junit.Test;

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

}