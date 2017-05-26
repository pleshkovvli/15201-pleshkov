package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.Path;

class FileModificationTimeFilter implements Filter {

    private long modificationTime;
    private boolean isLess;

    private static final char LESS = '<';
    private static final char MORE = '>';

    @Override
    public boolean isFit(Path file) {
        if(file==null) {
            throw new IllegalArgumentException();
        }
        long time = file.toFile().lastModified();
        return (isLess && (time < modificationTime)) || (!isLess && (time > modificationTime));
    }

    @Override
    public String getParam() {
        String param;
        if(isLess) {
            param = String.valueOf(LESS);
        } else {
            param = String.valueOf(MORE);
        }
        return param + (modificationTime/1000);
    }

    FileModificationTimeFilter(boolean isLess, long modificationTime) {
        this.isLess = isLess;
        this.modificationTime = modificationTime;
    }

}
