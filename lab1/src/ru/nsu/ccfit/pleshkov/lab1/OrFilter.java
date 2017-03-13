package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.Path;

class OrFilter extends AggregateFilter {

    @Override
    public boolean isFit(Path file) {
        return (firstFilter.isFit(file) || secondFilter.isFit(file));
    }

    OrFilter(Filter firstFilter, Filter secondFilter)  {
        super(firstFilter,secondFilter,'|');
    }
}