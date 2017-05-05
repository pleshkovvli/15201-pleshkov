package ru.nsu.ccfit.pleshkov.lab1;

class Pair {
    String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    private String className;

    Serializer getSerializer() {
        return serializer;
    }

    void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    private Serializer serializer;

    Pair(String className, Serializer serializer) {
        this.className = className;
        this.serializer = serializer;
    }
}
