package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.Path;

class NotFilter implements Filter {
    private Filter filter;

    private static final char OPEN_BRACKET ='(';
    private static final char CLOSE_BRACKET =')';
    private static final char SYMBOL = '!';

    @Override
    public boolean isFit(Path file) {
        if(file==null) {
            throw new IllegalArgumentException();
        }
        return !filter.isFit(file);
    }

    @Override
    public String getParam() {
        return String.valueOf(SYMBOL) + String.valueOf(OPEN_BRACKET) + filter.getParam()
                + String.valueOf(CLOSE_BRACKET);
    }

    public NotFilter(Filter filter) {
        this.filter = filter;
    }
}
