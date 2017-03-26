package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.Path;

class AndFilter extends AggregateFilter {

    @Override
    public boolean isFit(Path file) {
        if(file==null) {
            throw new IllegalArgumentException();
        }
        return (firstFilter.isFit(file) && secondFilter.isFit(file));
    }

    AndFilter(Filter firstFilter, Filter secondFilter) {
        super(firstFilter,secondFilter,'&');
    }
}
