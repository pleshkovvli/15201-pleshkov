package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;

abstract class AggregateFilter implements Filter {
    Filter firstFilter, secondFilter;
    char symbol;

    @Override
    public String getParam() {
        return symbol + "(" + firstFilter.getParam()
                + " " + secondFilter.getParam() + ")";
    }

    AggregateFilter(Filter firstFilter, Filter secondFilter, char sym) {
        this.symbol = sym;
        this.firstFilter = firstFilter;
        this.secondFilter = secondFilter;
    }
}
