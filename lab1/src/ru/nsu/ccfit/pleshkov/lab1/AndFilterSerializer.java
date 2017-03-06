package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;

public class AndFilterSerializer extends AggregateFilterSerializer {
    protected boolean checkType(char first) {
        return (first=='&');
    }

    protected AndFilter doMake(Filter firstFilter, Filter secondFilter) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return new AndFilter(firstFilter, secondFilter);
    }
}
