package ru.nsu.ccfit.pleshkov.lab1;

abstract class AggregateFilter implements Filter {
    Filter firstFilter, secondFilter;
    char symbol;

    private static final char OPEN_BRACKET ='(';
    private static final char CLOSE_BRACKET =')';
    private static final char DIVIDER = ' ';

    @Override
    public String getParam() {
        return symbol + String.valueOf(OPEN_BRACKET) + firstFilter.getParam()
                + DIVIDER + secondFilter.getParam() + String.valueOf(CLOSE_BRACKET);
    }

    AggregateFilter(Filter firstFilter, Filter secondFilter, char sym) {
        if(firstFilter==null || secondFilter==null) {
            throw new IllegalArgumentException();
        }
        this.symbol = sym;
        this.firstFilter = firstFilter;
        this.secondFilter = secondFilter;
    }
}
