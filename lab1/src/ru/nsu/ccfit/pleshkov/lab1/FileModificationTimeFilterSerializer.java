package ru.nsu.ccfit.pleshkov.lab1;

class FileModificationTimeFilterSerializer implements Serializer {
    public FileModificationTimeFilter make(String time) {
        boolean isLess;
        long modificationTime;
        if(time.charAt(0)=='>') {
            isLess = false;
        } else if (time.charAt(0)=='<') {
            isLess = true;
        } else {
            throw new IllegalArgumentException();
        }
        modificationTime = Long.valueOf(time.substring(1)).longValue() * 1000;
        return new FileModificationTimeFilter(isLess,modificationTime);
    }
}
