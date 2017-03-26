package ru.nsu.ccfit.pleshkov.lab1;

class FileModificationTimeFilterSerializer implements Serializer {
    private static final char LESS = '<';
    private static final char MORE = '>';

    public FileModificationTimeFilter make(String time) {
        if(time==null) {
            throw new IllegalArgumentException();
        }
        boolean isLess;
        long modificationTime;
        if(time.charAt(0)==MORE) {
            isLess = false;
        } else if (time.charAt(0)==LESS) {
            isLess = true;
        } else {
            throw new IllegalArgumentException();
        }
        modificationTime = Long.valueOf(time.substring(1)).longValue() * 1000;
        return new FileModificationTimeFilter(isLess,modificationTime);
    }
}
