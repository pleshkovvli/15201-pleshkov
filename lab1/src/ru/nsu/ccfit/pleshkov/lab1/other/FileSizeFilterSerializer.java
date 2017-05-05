package ru.nsu.ccfit.pleshkov.lab1.other;

import ru.nsu.ccfit.pleshkov.lab1.Serializer;

public class FileSizeFilterSerializer implements Serializer {
    private static final char LESS = 's';
    private static final char MORE = 'S';

    public FileSizeFilter make(String size) {
        if(size==null) {
            throw new IllegalArgumentException();
        }
        boolean isLess;
        long s;
        if(size.charAt(0)==MORE) {
            isLess = false;
        } else if (size.charAt(0)==LESS) {
            isLess = true;
        } else {
            throw new IllegalArgumentException();
        }
        s = Long.valueOf(size.substring(1)).longValue();
        return new FileSizeFilter(isLess,s);
    }
}
