package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.Path;

public class FileModificationTimeFilter implements Filter {

    private long modificationTime;
    private boolean isLess;

    @Override
    public boolean isFit(Path file) {
        long time = file.toFile().lastModified();
        return (isLess && (time < modificationTime)) || (!isLess && (time > modificationTime));
    }

    @Override
    public String getParam() {
        String param;
        if(isLess) {
            param = "<";
        } else {
            param = ">";
        }
        return param + (modificationTime/1000);
    }

    public FileModificationTimeFilter(boolean isLess, long modificationTime) {
        this.isLess = isLess;
        this.modificationTime = modificationTime;
    }

}
