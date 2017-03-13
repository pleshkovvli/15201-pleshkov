package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

class AndFilter extends AggregateFilter {

    @Override
    public boolean isFit(Path file) {
        return (firstFilter.isFit(file) && secondFilter.isFit(file));
    }

    AndFilter(Filter firstFilter, Filter secondFilter) {
        super(firstFilter,secondFilter,'&');
    }
}
