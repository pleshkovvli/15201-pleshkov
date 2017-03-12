package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;

public class NotFilterSerializer implements Serializer {
    public NotFilter make(String notFilter) throws ParseException {
        if(notFilter.charAt(0)!='!'
                || notFilter.charAt(1)!='('
                || notFilter.charAt(notFilter.length()-1)!=')') {
            throw new IllegalArgumentException();
        }
        try {
            Filter filter = FilterFactory.make(notFilter.substring(2,notFilter.length()-1));
            return new NotFilter(filter);
        } catch (ParseException e) {
            throw e;
        }
    }
}
