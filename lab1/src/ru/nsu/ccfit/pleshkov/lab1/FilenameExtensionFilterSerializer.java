package ru.nsu.ccfit.pleshkov.lab1;

public class FilenameExtensionFilterSerializer implements Serializer {
    public FilenameExtensionFilter make(String ext) {
        if(ext==null) {
            throw new IllegalArgumentException();
        }
        if(ext.charAt(0)!='.') {
            throw new IllegalArgumentException();
        }
        return new FilenameExtensionFilter(ext);
    }
}
