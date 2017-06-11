package ru.nsu.ccfit.pleshkov.lab3;

import java.io.Serializable;

public class User implements Serializable, UserElement {
    final private String name;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public User(String name, String type) {
        this.name = name;
        this.type = type;
    }

    final private String type;
}
