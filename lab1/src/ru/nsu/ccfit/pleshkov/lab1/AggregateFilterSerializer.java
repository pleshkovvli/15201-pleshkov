package ru.nsu.ccfit.pleshkov.lab1;

import java.util.Stack;

abstract class AggregateFilterSerializer implements Serializer {
    private static final char OPEN_BRACKET ='(';
    private static final char CLOSE_BRACKET =')';
    private static final char DIVIDER = ' ';

    public AggregateFilter make(String agrFilter) throws ParseException {
        if(agrFilter==null) {
            throw new IllegalArgumentException();
        }
        if(!checkType(agrFilter.charAt(0))) {
            throw new IllegalArgumentException();
        }
        Filter firstFilter, secondFilter;
        if((agrFilter.charAt(1)!=OPEN_BRACKET) || (agrFilter.charAt(agrFilter.length()-1)!=CLOSE_BRACKET)) {
            throw new IllegalArgumentException();
        }
        try {
            agrFilter = agrFilter.charAt(0) + String.valueOf(OPEN_BRACKET) +
                    agrFilter.substring(2,agrFilter.length()-1).trim() + String.valueOf(CLOSE_BRACKET);
            firstFilter = FilterFactory.make(agrFilter.substring(2,getIndex(agrFilter)));
            secondFilter = FilterFactory.make(agrFilter.substring(getIndex(agrFilter),agrFilter.length()-1).trim());
            return doMake(firstFilter,secondFilter);
        } catch (ParseException ex) {
            throw ex;
        }
    }

    abstract protected boolean checkType(char c);
    abstract protected AggregateFilter doMake(Filter firstFilter,Filter secondFilter) throws ParseException;

    private int getIndex(String expression) throws ParseException {
        Stack<Character> stack = new Stack<>();
        if(expression.charAt(3)!=OPEN_BRACKET) {
            return expression.indexOf(DIVIDER);
        }
        stack.push(expression.charAt(3));
        char[] exp = expression.toCharArray();
        int i;
        for(i = 4; i<expression.length() && !stack.isEmpty(); i++) {
            if(exp[i]==OPEN_BRACKET) {
                stack.push(exp[i]);
            } else if(exp[i]==CLOSE_BRACKET) {
                stack.pop();
            }
        }
        if(i==expression.length()) {
            throw new ParseException();
        }
        return i;
    }
}
