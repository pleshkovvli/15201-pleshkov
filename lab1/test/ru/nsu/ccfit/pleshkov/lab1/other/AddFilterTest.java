package ru.nsu.ccfit.pleshkov.lab1.other;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.ccfit.pleshkov.lab1.FilterFactory;
import ru.nsu.ccfit.pleshkov.lab1.Main;
import ru.nsu.ccfit.pleshkov.lab1.MainTest;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class AddFilterTest {
    @Test
    public void addFilterTest() {
        try {
            System.setOut(new PrintStream("testout"));
        } catch (IOException e) {
            Assert.fail();
            System.exit(1);
        }
        String[] args = {"testexamples/config10","testexamples/test10"};
        FilterFactory.addFilter('s',"ru.nsu.ccfit.pleshkov.lab1.other.FileSizeFilterSerializer");
        FilterFactory.addFilter('S',"ru.nsu.ccfit.pleshkov.lab1.other.FileSizeFilterSerializer");
        Main.main(args);
        Assert.assertTrue(MainTest.areFilesIdentical(new File("testout"),new File("testexamples/test10out")));
    }
}
