package ru.nsu.ccfit.pleshkov.lab1;

import java.nio.file.Path;

public interface Filter {
    boolean isFit(Path file);
    String getParam();
    default boolean equalsByParam(Filter other) {
        return getParam().equals(other.getParam());
    }
}
