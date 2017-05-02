package ru.nsu.ccfit.pleshkov.lab2.factory;

import java.util.concurrent.atomic.AtomicLong;

class IDGenerator {
    private AtomicLong currentID = new AtomicLong(1);

    long getID() {
        return currentID.getAndIncrement();
    }
}
