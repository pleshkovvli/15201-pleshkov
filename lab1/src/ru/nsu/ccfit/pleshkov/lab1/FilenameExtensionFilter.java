package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.Path;

public class FilenameExtensionFilter implements Filter {
    private String extension;

    @Override
    public boolean isFit(Path file) {
        if(file==null) {
            throw new IllegalArgumentException();
        }
        String filename = file.getFileName().toString();
        int index = filename.lastIndexOf('.');
        if(index==-1) {
            return  false;
        }
        return filename.substring(index).equals(extension);
    }

    @Override
    public String getParam() {
        return extension;
    }

    public FilenameExtensionFilter(String ext) {
        extension = ext;
    }
}
