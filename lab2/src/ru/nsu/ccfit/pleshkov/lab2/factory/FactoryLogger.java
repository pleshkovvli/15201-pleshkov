package ru.nsu.ccfit.pleshkov.lab2.factory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FactoryLogger implements Observer {
    private static final String LOG_FILE_NAME = "factory.log";
    private static final String DELIMITER = ": ";
    private static final String SPACE = " ";
    private static final String COMMA = ", ";
    private static final String DEALER = "Dealer";
    private static final String AUTO = "Auto";
    private static final String BODY = "Body";
    private static final String MOTOR = "Motor";
    private static final String ACCESSORY = "Accessory";

    public boolean isToLog() {
        return toLog;
    }

    public void setToLog(boolean toLog) {
        this.toLog = toLog;
    }

    private boolean toLog;

    private static Logger logger;

    public FactoryLogger() throws IOException {
        PropertyConfigurator.configure("log4j.properties");
        if(!Files.exists(Paths.get(LOG_FILE_NAME))) {
            Files.createFile(Paths.get(LOG_FILE_NAME));
        } else {
            try(PrintWriter writer = new PrintWriter(Paths.get(LOG_FILE_NAME).toFile())) {
                writer.print("");
                writer.close();
            }
        }
        logger = Logger.getLogger(FactoryLogger.class);
    }

    public void update(int newData) {

    }

    public void log(int DealerNumber, Car car)  {
        if(!toLog) {
            return;
        }
        logger.info(DEALER + SPACE + String.valueOf(DealerNumber) + DELIMITER
                + AUTO + SPACE + car.getID() + SPACE + "("
                + BODY + DELIMITER + car.getBodyID() + COMMA
                + MOTOR + DELIMITER + car.getEngineID() + COMMA
                + ACCESSORY + SPACE + car.getAccessoryID() + ")");
    }
}
