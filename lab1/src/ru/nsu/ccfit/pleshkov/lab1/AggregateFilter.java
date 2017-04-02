package ru.nsu.ccfit.pleshkov.lab1;

import java.util.ArrayList;

abstract class AggregateFilter implements Filter {
    ArrayList<Filter> filters = new ArrayList<>();
    char symbol;

    private static final char OPEN_BRACKET ='(';
    private static final char CLOSE_BRACKET =')';
    private static final char DIVIDER = ' ';

    @Override
    public String getParam() {
        String param = "";
        for(Filter filter : filters) {
            param = param + filter.getParam() + String.valueOf(DIVIDER);
        }
        param = param.trim();
        return symbol + String.valueOf(OPEN_BRACKET) + param + String.valueOf(CLOSE_BRACKET);
    }

    AggregateFilter(ArrayList<Filter> filters, char sym) {
        this.symbol = sym;
        if(filters.contains(null) || filters.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.filters = filters;
    }
}
