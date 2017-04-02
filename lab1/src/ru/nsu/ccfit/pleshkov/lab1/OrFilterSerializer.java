package ru.nsu.ccfit.pleshkov.lab1;

import java.util.ArrayList;

class OrFilterSerializer extends AggregateFilterSerializer {
    protected boolean checkType(char first) {
        return (first=='|');
    }
    protected OrFilter doMake(ArrayList<Filter> filters)  {
        return new OrFilter(filters);
    }
}
