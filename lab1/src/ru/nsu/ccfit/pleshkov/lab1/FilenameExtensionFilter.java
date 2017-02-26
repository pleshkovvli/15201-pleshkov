package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.Path;

public class FilenameExtensionFilter implements Filter {
    @Override
    public boolean isFit(Path file) {
        String filename = file.getFileName().toString();
        int index = filename.lastIndexOf('.');
        if(index==-1) {
            return false;
        }
        return filename.substring(index).equals(extension);
    }
    private String extension;
    public FilenameExtensionFilter(String ext) {
        if(ext.charAt(0)!='.') {
            throw new IllegalArgumentException();
        }
        extension = ext;
    }


}
