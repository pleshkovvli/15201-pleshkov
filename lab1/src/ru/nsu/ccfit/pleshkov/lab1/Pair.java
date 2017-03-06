package ru.nsu.ccfit.pleshkov.lab1;

public class Pair {
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    private String className;

    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    private Serializer serializer;

    public Pair(String className, Serializer serializer) {
        this.className = className;
        this.serializer = serializer;
    }
}
