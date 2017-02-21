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
        return filename.substring(index+1).equals(extension);
    }
    private String extension;
    public FilenameExtensionFilter(String ext) {
        extension = ext;
    }
}
