package ru.nsu.ccfit.pleshkov.lab1;

class NotFilterSerializer implements Serializer {
    public NotFilter make(String notFilter) throws ParseException {
        if(notFilter.charAt(0)!='!'
                || notFilter.charAt(1)!='('
                || notFilter.charAt(notFilter.length()-1)!=')') {
            System.out.println(notFilter);
            throw new ParseException();
        }
        try {
            notFilter = notFilter.charAt(0) + "(" + notFilter.substring(2,notFilter.length()-1).trim() + ")";
            Filter filter = FilterFactory.make(notFilter.substring(2,notFilter.length()-1));
            return new NotFilter(filter);
        } catch (ParseException e) {
            throw e;
        }
    }
}
