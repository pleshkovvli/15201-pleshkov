package ru.nsu.ccfit.pleshkov.lab1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FileIterator {
    public ArrayList<Path> files;
    public FileIterator(String path) {
        files = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(path)).filter(Files::isRegularFile)) {
            paths.forEach(files::add);
            files.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
