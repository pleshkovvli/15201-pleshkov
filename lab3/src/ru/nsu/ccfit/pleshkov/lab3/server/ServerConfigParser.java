package ru.nsu.ccfit.pleshkov.lab3.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerConfigParser {
    final static private String XML = "xml";
    final static private String OBJ = "objects";

    static Ports parse(String path) throws BadParsingException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line = reader.readLine();
            int xml = parseLine(line,XML);
            line = reader.readLine();
            int obj = parseLine(line,OBJ);
            return new Ports(xml,obj);
        } catch (IOException e) {
            throw new BadParsingException(e);
        }
    }

    private static int parseLine(String line, String template) throws BadParsingException {
        if(line == null) {
            throw new BadParsingException("Null argument");
        }
        if(line.substring(0,template.length()).equals(template)) {
            int tmp;
            try {
                tmp = Integer.parseInt(line.substring(template.length() + 1));
            } catch (NumberFormatException e) {
                throw new BadParsingException("Bad parse at: " + line);
            }
            if(tmp <= 0 || tmp > 65536) {
                throw new BadParsingException("Bad parse at: " + line);
            }
            return tmp;
        } else {
            throw new BadParsingException("Bad parse at: " + line);
        }
    }
}
