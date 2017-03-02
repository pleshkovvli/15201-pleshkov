package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class OrFilter extends AggregateFilter {

    @Override
    public boolean isFit(Path file) {

        return (firstFilter.isFit(file) || secondFilter.isFit(file));
    }

    public OrFilter(String orFilter) throws ClassNotFoundException,InstantiationException,
            IllegalAccessException,NoSuchMethodException, InvocationTargetException {
        super(orFilter);
        if(orFilter.charAt(0)!='|') {
            throw new IllegalArgumentException();
        }
    }
}