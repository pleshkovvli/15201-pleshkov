package ru.nsu.ccfit.pleshkov.lab2.factory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

class ConfigParser {

    final static private String accessoryStorageCapacityString = "accessoryStorageCapacity";
    final static private String bodiesStorageCapacityString = "bodiesStorageCapacity";
    final static private String enginesStorageCapacityString = "enginesStorageCapacity";
    final static private String carStorageCapacityString = "carStorageCapacity";
    final static private String accessorySuppliersNumberString = "accessorySuppliersNumber";
    final static private String numberOfWorkersString = "numberOfWorkers";
    final static private String dealersNumberString = "dealersNumber";
    final static private String toLogString = "toLog";

    static ConfigData parseConfig(Path configFilePath) throws BadParseException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFilePath.toFile())))) {
            String line = reader.readLine();
            int accessoryStorageCapacity = parseLine(line, accessoryStorageCapacityString);
            line = reader.readLine();
            int bodiesStorageCapacity = parseLine(line, bodiesStorageCapacityString);
            line = reader.readLine();
            int enginesStorageCapacity = parseLine(line, enginesStorageCapacityString);
            line = reader.readLine();
            int carStorageCapacity = parseLine(line, carStorageCapacityString);
            line = reader.readLine();
            int accessorySuppliersNumber = parseLine(line, accessorySuppliersNumberString);
            line = reader.readLine();
            int numberOfWorkers = parseLine(line, numberOfWorkersString);
            line = reader.readLine();
            int dealersNumber = parseLine(line, dealersNumberString);
            line = reader.readLine();
            boolean toLog = false;
            if(line.substring(0,toLogString.length()).equals(toLogString)) {
                if(line.substring(toLogString.length()+1).equals("true")) {
                    toLog = true;
                } else if(line.substring(toLogString.length()+1).equals("false")) {
                    toLog = false;
                }
            } else {
                throw new BadParseException("Bad parse at: " + line);
            }
            return new ConfigData(accessoryStorageCapacity,bodiesStorageCapacity,
                    enginesStorageCapacity, carStorageCapacity, accessorySuppliersNumber,
                    numberOfWorkers, dealersNumber, toLog);
        } catch (IOException e) {
            throw new BadParseException("Failed to open " +e.getMessage(),e);
        }
    }

    private static int parseLine(String line, String template) throws BadParseException {
        if(line.substring(0,template.length()).equals(template)) {
            int tmp;
            try {
                tmp = Integer.parseInt(line.substring(template.length() + 1));
            } catch (NumberFormatException e) {
                throw new BadParseException("Bad parse at: " + line);
            }
            if(tmp <= 0) {
                throw new BadParseException("Bad parse at: " + line);
            }
            return tmp;
        } else {
            throw new BadParseException("Bad parse at: " + line);
        }
    }
}
