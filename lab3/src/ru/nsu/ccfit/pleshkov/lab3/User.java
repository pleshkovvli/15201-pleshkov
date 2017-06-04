package ru.nsu.ccfit.pleshkov.lab3;

class User {
    final private String name;

    public String getName() {
        return name;
    }

    String getType() {
        return type;
    }

    User(String name, String type) {

        this.name = name;
        this.type = type;
    }

    final private String type;
}
