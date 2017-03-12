package ru.nsu.ccfit.pleshkov.lab1;

public class Stats {
    private int lines = 0;

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    private int files = 0;

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    Stats(int lines, int files) {
        this.lines = lines;
        this.files = files;
    }

    Stats() {
        lines = 0;
        files = 0;
    }

    public boolean equals(Stats stats) {
        return (stats.getFiles()==files) && (stats.getLines()==lines);
    }
}
