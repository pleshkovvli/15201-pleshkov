package ru.nsu.ccfit.pleshkov.lab2.factory;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class InitializerTest {
    @Test
    public void parserTest() throws BadParseException {
        Initializer.startFactory(Paths.get("testexamples/config7"));
        Assert.assertEquals(Initializer.getAccessoryStorageCapacity(),123);
        Assert.assertEquals(Initializer.getBodiesStorageCapacity(),456);
        Assert.assertEquals(Initializer.getEnginesStorageCapacity(),234);
        Assert.assertEquals(Initializer.getCarStorageCapacity(),345);
        Assert.assertEquals(Initializer.getAccessorySuppliersNumber(),21);
        Assert.assertEquals(Initializer.getNumberOfWorkers(),15);
        Assert.assertEquals(Initializer.getDealersNumber(),8);
        Assert.assertEquals(Initializer.isToLog(),false);
    }

    @Test
    public void badParsing() throws FileNotFoundException {
        try {
            Initializer.startFactory(Paths.get("testexamples/config8"));
            Assert.fail();
        } catch (BadParseException e) {
            Assert.assertTrue(e.getMessage().equals("Bad parse at: bodiesStorageCapacity=-43"));
        }
    }
}