package ru.nsu.ccfit.pleshkov.lab1;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class MainTest {
    @Test
    public void commonTest() {
        try {
            System.setOut(new PrintStream("testout"));
        } catch (IOException e) {
            Assert.fail();
            System.exit(1);
        }
        String[] args = {"testexamples/config1","testexamples/test1"};
        Main.main(args);
        Assert.assertTrue(areFilesIdentical(new File("testout"),new File("testexamples/test1out")));
    }

    @Test
    public void wrongFilter() {
        try {
            System.setOut(new PrintStream("testout"));
        } catch (IOException e) {
            Assert.fail();
            System.exit(1);
        }
        String[] args = {"testexamples/config3","testexamples/test1"};
        Main.main(args);
        Assert.assertTrue(areFilesIdentical(new File("testout"),new File("testexamples/test3out")));
    }

    @Test
    public void wrongConfig() {
        try {
            System.setOut(new PrintStream("testout"));
        } catch (IOException e) {
            Assert.fail();
            System.exit(1);
        }
        String[] args = {"nonsence","testexamples/test1"};
        Main.main(args);
        Assert.assertTrue(areFilesIdentical(new File("testout"),new File("testexamples/test4out")));
    }

    @Test
    public void wrongDirectory() {
        try {
            System.setOut(new PrintStream("testout"));
        } catch (IOException e) {
            Assert.fail();
            System.exit(1);
        }
        String[] args = {"testexamples/config3","nonsence"};
        Main.main(args);
        Assert.assertTrue(areFilesIdentical(new File("testout"),new File("testexamples/test5out")));
    }

    static public boolean areFilesIdentical(File file1, File file2) {
        if(file1.length() != file2.length()) {
            return false;
        }

        try(InputStream in1 =new BufferedInputStream(new FileInputStream(file1));
            InputStream in2 =new BufferedInputStream(new FileInputStream(file2))) {
                int value1,value2;
                do {
                    value1 = in1.read();
                    value2 = in2.read();
                    if(value1 !=value2){
                        return false;
                    }
                } while(value1 >=0);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}