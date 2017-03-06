package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;

abstract public class AggregateFilter implements Filter {
    protected Filter firstFilter, secondFilter;
    protected char symbol;

    @Override
    public String getParam() {
        return symbol + "(" + firstFilter.getParam()
                + " " + secondFilter.getParam() + ")";
    }

    public AggregateFilter(Filter firstFilter, Filter secondFilter, char sym) throws ClassNotFoundException,InstantiationException,
    IllegalAccessException,NoSuchMethodException, InvocationTargetException {
        this.symbol = sym;
        this.firstFilter = firstFilter;
        this.secondFilter = secondFilter;
    }
}
