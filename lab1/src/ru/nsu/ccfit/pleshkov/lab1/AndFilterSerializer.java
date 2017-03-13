package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;

class AndFilterSerializer extends AggregateFilterSerializer {
    protected boolean checkType(char first) {
        return (first=='&');
    }

    protected AndFilter doMake(Filter firstFilter, Filter secondFilter) {
        return new AndFilter(firstFilter, secondFilter);
    }
}
