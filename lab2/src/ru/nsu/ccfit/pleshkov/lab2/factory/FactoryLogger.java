package ru.nsu.ccfit.pleshkov.lab2.factory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class FactoryLogger implements Observer {
    private static FactoryLogger instance;

    public static synchronized FactoryLogger getLogger() throws IOException {
        if(instance == null) {
            instance = new FactoryLogger();
        }
        return instance;
    }

    private static final String LOG_FILE_NAME = "factory.log";
    private static final String DELIMITER = ": ";
    private static final String SPACE = " ";
    private static final String COMMA = ", ";
    private static final String DEALER = "Dealer";
    private static final String AUTO = "Auto";
    private static final String BODY = "Body";
    private static final String MOTOR = "Motor";
    private static final String ACCESSORY = "Accessory";

    boolean isToLog() {
        return toLog;
    }

    void setToLog(boolean toLog) {
        this.toLog = toLog;
    }

    private boolean toLog;

    private static Logger logger;

    private FactoryLogger() throws IOException {
        PropertyConfigurator.configure("log4j.properties");
        if(!Files.exists(Paths.get(LOG_FILE_NAME))) {
            Files.createFile(Paths.get(LOG_FILE_NAME));
        }
        logger = Logger.getLogger(FactoryLogger.class);
    }

    public void update(int newData) {

    }

    void log(int DealerNumber, Car car)  {
        if(!toLog) {
            return;
        }
        try {
            logger.info(DEALER + SPACE + String.valueOf(DealerNumber) + DELIMITER
                    + AUTO + SPACE + car.getID() + SPACE + "("
                    + BODY + DELIMITER + car.getBodyID() + COMMA
                    + MOTOR + DELIMITER + car.getEngineID() + COMMA
                    + ACCESSORY + SPACE + car.getAccessoryID() + ")");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    void log(String message) {
        if(!toLog) {
            return;
        }
        try {
            logger.info(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    void error(String message) {
        if(!toLog) {
            return;
        }
        try {
            logger.error(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    void simpleLog(String s) {
        logger.info(s);
    }
}
