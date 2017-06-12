package ru.nsu.ccfit.pleshkov.lab3.server;

class Ports {
    public int getXml() {
        return xml;
    }

    public int getObjects() {
        return objects;
    }

    final private int xml;

    Ports(int xml, int objects) {
        this.xml = xml;
        this.objects = objects;
    }

    final private int objects;
}
