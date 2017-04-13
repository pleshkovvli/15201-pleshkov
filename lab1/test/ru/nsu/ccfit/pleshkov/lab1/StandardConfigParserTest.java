package ru.nsu.ccfit.pleshkov.lab1;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

public class StandardConfigParserTest {
    @Test
    public void parse() {
        StandardConfigParser parser = new StandardConfigParser();
        HashSet<Filter> fil = new HashSet<>();
        fil.add(new FilenameExtensionFilter(".c"));
        fil.add(new FilenameExtensionFilter(".h"));
        fil.add(new FileModificationTimeFilter(true, 5656566656L*1000));
        ArrayList<Filter> args = new ArrayList<>();
        args.add(new FilenameExtensionFilter(".c"));
        args.add(new FilenameExtensionFilter(".h"));
        fil.add(new OrFilter(args));
        HashSet<Filter> filters = null;
        try {
            filters = new HashSet<>(parser.parse("testexamples/config1"));
        } catch (Lab1Exception e) {
            Assert.fail();
        }
        for(Filter entry : fil) {
            boolean isFail = true;
            for(Filter entry2 : filters) {
                if(entry.equalsByParam(entry2)) {
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
                if(entry.equalsByParam(entry2)) {
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
        ArrayList<Filter> args = new ArrayList<>();
        args.add(new NotFilter(new FileModificationTimeFilter(false,2323*1000)));
        args.add(new FilenameExtensionFilter(".ui"));
        ArrayList<Filter> args1 = new ArrayList<>();
        args1.add(new NotFilter(new OrFilter(args)));
        args1.add(new FileModificationTimeFilter(true,56*1000));
        Filter one = new AndFilter(args1);
        StandardConfigParser parser = new StandardConfigParser();
        try {
            ArrayList<Filter> filters = parser.parse("testexamples/config8");
            if(filters.size()!=1) {
                Assert.fail();
            }
            Assert.assertTrue(one.equalsByParam(filters.get(0)));
        } catch (Lab1Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void hardAggregateParse() {
        ArrayList<Filter> args = new ArrayList<>();
        args.add(new FilenameExtensionFilter(".h"));
        args.add(new FilenameExtensionFilter(".c"));
        ArrayList<Filter> args1 = new ArrayList<>();
        args1.add(new FileModificationTimeFilter(false,6000*1000));
        StandardConfigParser parser = new StandardConfigParser();
        ArrayList<Filter> args2 = new ArrayList<Filter>();
        args2.add(new FilenameExtensionFilter(".java"));
        args1.add(new NotFilter(new OrFilter(args2)));
        ArrayList<Filter> args3 = new ArrayList<Filter>();
        args3.add(new FileModificationTimeFilter(true,10000*1000));
        args1.add(new AndFilter(args3));
        args.add(new AndFilter(args1));
        Filter one = new OrFilter(args);
        try {
            ArrayList<Filter> filters = parser.parse("testexamples/config9");
            if(filters.size()!=1) {
                Assert.fail();
            }
            Assert.assertTrue(one.equalsByParam(filters.get(0)));
        } catch (Lab1Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}