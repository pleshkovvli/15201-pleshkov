package ru.nsu.ccfit.pleshkov.lab1;

import java.util.ArrayList;

class AndFilterSerializer extends AggregateFilterSerializer {
    protected boolean checkType(char first) {
        return (first=='&');
    }

    protected AndFilter doMake(ArrayList<Filter> filters) {
        return new AndFilter(filters);
    }
}
