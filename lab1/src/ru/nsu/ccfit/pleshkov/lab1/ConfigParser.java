package ru.nsu.ccfit.pleshkov.lab1;

import java.util.ArrayList;

public interface ConfigParser {
    ArrayList<Filter> parse(String path) throws Lab1Exception;
}
