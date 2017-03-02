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

    public NotFilter(String notFilter) throws ClassNotFoundException,InstantiationException,
            IllegalAccessException,NoSuchMethodException, InvocationTargetException {
        if(notFilter.charAt(0)!='!'
                || notFilter.charAt(1)!='('
                || notFilter.charAt(notFilter.length()-1)!=')') {
            throw new IllegalArgumentException();
        }
        filter = FilterFactory.make(notFilter.substring(2,notFilter.length()-1));
    }
}
