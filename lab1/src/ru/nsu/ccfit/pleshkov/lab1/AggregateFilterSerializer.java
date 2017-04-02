package ru.nsu.ccfit.pleshkov.lab1;

import java.util.ArrayList;
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
            ArrayList<Filter> filters = new ArrayList<>();
            agrFilter = agrFilter.substring(2, agrFilter.length() - 1).trim();
            while(agrFilter.length()>0) {
                String first = getIndex(agrFilter);
                filters.add(FilterFactory.make(first.trim()));
                agrFilter = agrFilter.trim().substring(first.length()).trim();
            }
            return doMake(filters);
        } catch (ParseException ex) {
            throw ex;
        }
    }

    abstract protected boolean checkType(char c);
    abstract protected AggregateFilter doMake(ArrayList<Filter> filters) throws ParseException;

    private String getIndex(String expression) throws ParseException {
        Stack<Character> stack = new Stack<>();
        if(!expression.trim().contains(String.valueOf(DIVIDER))) {
            return expression;
        }
        if(expression.charAt(1)!=OPEN_BRACKET) {
            return expression.substring(0,expression.indexOf(DIVIDER));
        }
        stack.push(expression.charAt(3));
        char[] exp = expression.toCharArray();
        int i;
        for(i = 2; i<expression.length() && !stack.isEmpty(); i++) {
            if(exp[i]==OPEN_BRACKET) {
                stack.push(exp[i]);
            } else if(exp[i]==CLOSE_BRACKET) {
                stack.pop();
            }
        }
        return expression.substring(0,i);
    }
}
