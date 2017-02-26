package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class FilterFactory {
    private HashMap<Character, String> map;
    public Filter make(String s) throws ClassNotFoundException,InstantiationException,
            IllegalAccessException,NoSuchMethodException, InvocationTargetException {
        Class c = Class.forName(map.get(s.charAt(0)));
        Class params[] = {"".getClass()};
        return ((Filter) c.getConstructor(params).newInstance(s));
    }
    FilterFactory() {
        map = new HashMap<>();
        map.put('.',"ru.nsu.ccfit.pleshkov.lab1.FilenameExtensionFilter");
    }
    public void addFilter(char c, String className) {
        map.put(c,className);
    }
    public Set<Character> getKeys() {
         return map.keySet();
    }
}
