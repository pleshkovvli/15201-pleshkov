package ru.nsu.ccfit.pleshkov.lab1.other;

import ru.nsu.ccfit.pleshkov.lab1.Filter;

import java.nio.file.Path;

public class FileSizeFilter implements Filter {
    private long size;
    private boolean isLess;

    private static final char LESS = 's';
    private static final char MORE = 'S';

    @Override
    public boolean isFit(Path file) {
        if(file==null) {
            throw new IllegalArgumentException();
        }
        long s = file.toFile().length();
        return (isLess && (s < size)) || (!isLess && (s > size));
    }

    @Override
    public String getParam() {
        String param;
        if(isLess) {
            param = String.valueOf(LESS);
        } else {
            param = String.valueOf(MORE);
        }
        return param + size;
    }

    FileSizeFilter(boolean isLess, long size) {
        this.isLess = isLess;
        this.size = size;
    }
}
