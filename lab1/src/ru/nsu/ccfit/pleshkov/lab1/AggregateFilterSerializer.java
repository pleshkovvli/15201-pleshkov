package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;

abstract public class AggregateFilterSerializer implements Serializer {
    public AggregateFilter make(String agrFilter) throws ParseException {
        if(!checkType(agrFilter.charAt(0))) {
            throw new IllegalArgumentException();
        }
        Filter firstFilter, secondFilter;
        if((agrFilter.charAt(1)!='(') || (agrFilter.charAt(agrFilter.length()-1)!=')')) {
            throw new IllegalArgumentException();
        }
        try {
            firstFilter = FilterFactory.make(agrFilter.substring(2,agrFilter.indexOf(" ")));
            secondFilter = FilterFactory.make(agrFilter.substring(agrFilter.indexOf(" ") + 1,agrFilter.length()-1));
            return doMake(firstFilter,secondFilter);
        } catch (ParseException ex) {
            throw ex;
        }
    }

    abstract protected boolean checkType(char c);
    abstract protected AggregateFilter doMake(Filter firstFilter,Filter secondFilter) throws ParseException;
}
