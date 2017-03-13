package ru.nsu.ccfit.pleshkov.lab1;

class OrFilterSerializer extends AggregateFilterSerializer {
    protected boolean checkType(char first) {
        return (first=='|');
    }
    protected OrFilter doMake(Filter firstFilter, Filter secondFilter)  {
        return new OrFilter(firstFilter,secondFilter);
    }
}
