package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;

public class OrFilterSerializer extends AggregateFilterSerializer {
    protected boolean checkType(char first) {
        return (first=='|');
    }
    protected OrFilter doMake(Filter firstFilter, Filter secondFilter) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return new OrFilter(firstFilter,secondFilter);
    }
}
