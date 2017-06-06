package ru.nsu.ccfit.pleshkov.lab3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
class ConfigParser {
    static final private String TYPE = "type";
    static final private String PORT = "port";
    static final private String IP = "ip";

    static Config parse(String path) throws BadParsingException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String type = parseLine(reader.readLine(),TYPE);
            String ip = parseLine(reader.readLine(),IP);
            String port = parseLine(reader.readLine(),PORT);
            return new Config(Integer.valueOf(port), InetAddress.getByName(ip),type);

        } catch (IOException e) {
            throw new BadParsingException(e);
        }
    }

    private static String parseLine(String line, String template) throws BadParsingException {
        if(line == null) {
            throw new BadParsingException("Null line");
        }
        try {
            if(line.substring(0,template.length()).equals(template)) {
                return line.substring(template.length() + 1);
            } else {
                throw new BadParsingException("Bad parse at: " + line);
            }
        } catch (RuntimeException e) {
            throw new BadParsingException("Bad parse at: " + line);
        }
    }
}