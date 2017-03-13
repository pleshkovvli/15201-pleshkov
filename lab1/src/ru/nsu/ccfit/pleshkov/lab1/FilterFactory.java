package ru.nsu.ccfit.pleshkov.lab1;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class FilterFactory {
    static private HashMap<Character, Pair> map;

    static {
        map = new HashMap<>();
        map.put('.', new Pair("ru.nsu.ccfit.pleshkov.lab1.FilenameExtensionFilterSerializer", null));
        map.put('>', new Pair("ru.nsu.ccfit.pleshkov.lab1.FileModificationTimeFilterSerializer", null));
        map.put('<', new Pair("ru.nsu.ccfit.pleshkov.lab1.FileModificationTimeFilterSerializer", null));
        map.put('!', new Pair("ru.nsu.ccfit.pleshkov.lab1.NotFilterSerializer", null));
        map.put('&', new Pair("ru.nsu.ccfit.pleshkov.lab1.AndFilterSerializer", null));
        map.put('|', new Pair("ru.nsu.ccfit.pleshkov.lab1.OrFilterSerializer", null));
    }

    static public Filter make(String s) throws ParseException {
        try {
            Serializer seria = getSerializer(s.charAt(0));
            Filter filter = seria.make(s);
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new ParseException();
        }
    }

    static private synchronized Serializer getSerializer(char key) throws ClassNotFoundException, NoSuchMethodException,
    InstantiationException, IllegalAccessException, InvocationTargetException {
        Pair pair = map.get(key);
        if(pair==null) {
            throw new IllegalArgumentException();
        }
        if(pair.getSerializer()==null) {
            map.get(key).setSerializer(
                    (Serializer) Class.forName(pair.getClassName()).getDeclaredConstructor().newInstance());
        }
        return map.get(key).getSerializer();
    }

    static public void addFilter(char c, String className) {
        map.put(c, new Pair(className,null));
    }

    static public Set<Character> getKeys() {
         return map.keySet();
    }
}
