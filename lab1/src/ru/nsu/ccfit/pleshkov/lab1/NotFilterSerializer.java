package ru.nsu.ccfit.pleshkov.lab1;

class NotFilterSerializer implements Serializer {
    private static final char OPEN_BRACKET ='(';
    private static final char CLOSE_BRACKET =')';
    private static final char SYMBOL = '!';

    public NotFilter make(String notFilter) throws ParseException {
        if(notFilter==null) {
            throw new IllegalArgumentException();
        }
        if(notFilter.charAt(0)!=SYMBOL
                || notFilter.charAt(1)!=OPEN_BRACKET
                || notFilter.charAt(notFilter.length()-1)!=CLOSE_BRACKET) {
            System.out.println(notFilter);
            throw new ParseException();
        }
        try {
            notFilter = notFilter.charAt(0) + String.valueOf(OPEN_BRACKET) +
                    notFilter.substring(2,notFilter.length()-1).trim() + String.valueOf(CLOSE_BRACKET);
            Filter filter = FilterFactory.make(notFilter.substring(2,notFilter.length()-1));
            return new NotFilter(filter);
        } catch (ParseException e) {
            throw e;
        }
    }
}
