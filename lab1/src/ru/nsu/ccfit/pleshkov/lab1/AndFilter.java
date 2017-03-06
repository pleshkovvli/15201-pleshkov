package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class AndFilter extends AggregateFilter {

    @Override
    public boolean isFit(Path file) {
        return (firstFilter.isFit(file) && secondFilter.isFit(file));
    }

    public AndFilter(Filter firstFilter, Filter secondFilter) throws ClassNotFoundException,InstantiationException,
            IllegalAccessException,NoSuchMethodException, InvocationTargetException {
        super(firstFilter,secondFilter,'&');
    }
}
