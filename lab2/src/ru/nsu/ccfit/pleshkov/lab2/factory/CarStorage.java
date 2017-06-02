package ru.nsu.ccfit.pleshkov.lab2.factory;

class CarStorage extends Storage<Car> {
    final private Object viewLock = new Object();
    private boolean isChanged = true;

    @Override
    public Car dequeue() throws InterruptedException {
        Car car = super.dequeue();
        synchronized (viewLock) {
            isChanged = true;
            viewLock.notifyAll();
        }
        return car;
    }

    public void control() throws InterruptedException {
        synchronized (viewLock) {
            while(!isChanged) {
                viewLock.wait();
            }
            isChanged = false;
        }
    }

    CarStorage(int capacity) {
        super(capacity);
    }

}
