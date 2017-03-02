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

    public AggregateFilter(String agrFilter) throws ClassNotFoundException,InstantiationException,
    IllegalAccessException,NoSuchMethodException, InvocationTargetException {
        symbol = agrFilter.charAt(0);
        if(agrFilter.charAt(1)!='(' || agrFilter.charAt(agrFilter.length()-1)!=')') {
            throw new IllegalArgumentException();
        }
        firstFilter = FilterFactory.make(agrFilter.substring(2,agrFilter.indexOf(" ")));
        secondFilter = FilterFactory.make(agrFilter.substring(agrFilter.indexOf(" ") + 1,agrFilter.length()-1));
    }
}
