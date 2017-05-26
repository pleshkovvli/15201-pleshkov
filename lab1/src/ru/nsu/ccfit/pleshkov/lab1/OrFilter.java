package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.Path;
import java.util.ArrayList;

class OrFilter extends AggregateFilter {

    @Override
    public boolean isFit(Path file) {
        if(file==null) {
            throw new IllegalArgumentException();
        }
        for(Filter filter : filters) {
            if(filter.isFit(file)) {
                return true;
            }
        }
        return false;
    }

    OrFilter(ArrayList<Filter> filters)  {
        super(filters,'|');
    }
}