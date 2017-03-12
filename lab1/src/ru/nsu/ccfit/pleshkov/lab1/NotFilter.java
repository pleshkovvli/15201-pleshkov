package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class NotFilter implements Filter {
    private Filter filter;

    @Override
    public boolean isFit(Path file) {
        return !filter.isFit(file);
    }

    @Override
    public String getParam() {
        return "!(" + filter.getParam() + ")";
    }

    public NotFilter(Filter filter) {
        this.filter = filter;
    }
}
