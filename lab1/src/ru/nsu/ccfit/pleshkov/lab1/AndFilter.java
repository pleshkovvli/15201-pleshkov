package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.Path;
import java.util.ArrayList;

class AndFilter extends AggregateFilter {

    @Override
    public boolean isFit(Path file) {
        if(file==null) {
            throw new IllegalArgumentException();
        }
        for(Filter filter : filters) {
            if(!filter.isFit(file)) {
                return false;
            }
        }
        return true;
    }

    AndFilter(ArrayList<Filter> filters) {
        super(filters,'&');
    }
}
