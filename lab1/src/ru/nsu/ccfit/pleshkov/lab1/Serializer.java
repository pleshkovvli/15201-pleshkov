package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;

public interface Serializer {
    Filter make(String param) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;
}
