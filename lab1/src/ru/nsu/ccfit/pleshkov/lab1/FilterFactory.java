package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class FilterFactory {
    static private HashMap<Character, String> map;

    static {
        map = new HashMap<>();
        map.put('.',"ru.nsu.ccfit.pleshkov.lab1.FilenameExtensionFilter");
        map.put('>',"ru.nsu.ccfit.pleshkov.lab1.FileModificationTimeFilter");
        map.put('<',"ru.nsu.ccfit.pleshkov.lab1.FileModificationTimeFilter");
        map.put('!',"ru.nsu.ccfit.pleshkov.lab1.NotFilter");
        map.put('&',"ru.nsu.ccfit.pleshkov.lab1.AndFilter");
        map.put('|',"ru.nsu.ccfit.pleshkov.lab1.OrFilter");
    }

    static public Filter make(String s) throws ClassNotFoundException,InstantiationException,
            IllegalAccessException,NoSuchMethodException, InvocationTargetException {
        Class c = Class.forName(map.get(s.charAt(0)));
        Class params[] = {String.class};
        return ((Filter) c.getConstructor(params).newInstance(s));
    }

    static public void addFilter(char c, String className) {
        map.put(c,className);
    }

    static public Set<Character> getKeys() {
         return map.keySet();
    }
}
