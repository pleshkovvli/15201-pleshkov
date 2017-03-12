package ru.nsu.ccfit.pleshkov.lab1;

public interface Serializer {
    Filter make(String param) throws ParseException;
}
