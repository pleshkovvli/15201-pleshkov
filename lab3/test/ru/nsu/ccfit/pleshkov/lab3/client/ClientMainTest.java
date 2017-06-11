package ru.nsu.ccfit.pleshkov.lab3.client;

import org.junit.Test;

public class ClientMainTest {
    @Test
    public void test() throws InterruptedException {
        new ClientGUI(null,null,null,null,null).forceLogin("");
    }
}